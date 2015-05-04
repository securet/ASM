package com.securet.ssm.services.rest;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.query.jpa.impl.JPAQuery;
import com.securet.ssm.components.mail.MailService;
import com.securet.ssm.components.sms.SMSService;
import com.securet.ssm.persistence.SequenceGeneratorHelper;
import com.securet.ssm.persistence.objects.Enumeration;
import com.securet.ssm.persistence.objects.IssueType;
import com.securet.ssm.persistence.objects.SecureTObject.SimpleObject;
import com.securet.ssm.persistence.objects.ServiceType;
import com.securet.ssm.persistence.objects.Site;
import com.securet.ssm.persistence.objects.Ticket;
import com.securet.ssm.persistence.objects.querydsl.jpa.JPATicket;
import com.securet.ssm.persistence.views.SimpleTicketArchive;
import com.securet.ssm.services.ticket.BaseTicketService;
import com.securet.ssm.services.vo.HPToolInput;
import com.securet.ssm.services.vo.ListObjects;
import com.securet.ssm.services.vo.TicketFilter;

@RestController
@Repository
@Service
public class TicketRestfulService extends BaseTicketService{

	private static final List<String> columnNames = new ArrayList<String>();

	static{
	}
	
	private static final List<String> historyColumnNames = new ArrayList<String>();
	static{
		historyColumnNames.add("ticketId");
		historyColumnNames.add("description");
		historyColumnNames.add("modifiedBy");
		historyColumnNames.add("lastUpdatedTimestamp");
	}

	private Logger _logger = LoggerFactory.getLogger(TicketRestfulService.class);

	@Autowired
	private MailService mailService;

	@Autowired
	private SMSService smsService;


	public MailService getMailService() {
		return mailService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public SMSService getSmsService() {
		return smsService;
	}

	public void setSmsService(SMSService smsService) {
		this.smsService = smsService;
	}
	
	@RequestMapping("/rest/ticket/forId")
	public Object getTicket(@RequestParam(value="ticketId",required=false) String ticketId,@AuthenticationPrincipal User loggedInUser){
		_logger.debug("ticketId sent as "+ticketId);
		String status = "error";
		Object messages = null;
		Object data = null;
		if(loggedInUser!=null){
			if(ticketId==null || ticketId.isEmpty()){
				messages = new FieldError("ticket", "ticketId", "TicketId cannot be empty");
			}else{
				Ticket userTicket = getUserTicket(ticketId, loggedInUser,mailService,smsService);
				if(userTicket==null){
					messages = new FieldError("ticket", "ticketId", "TicketId not found or no access to ticket");
				}else{
					status="success";
					//remove the fields not necessary - should use jackson mappers - TODO - use jackson mappers!!
					cleanTicketForResponse(userTicket);
					data=userTicket;
				}
			}
		}
		return new SecureTJSONResponse(status, messages, data);
	}

	@RequestMapping("/rest/ticket/forUser")
	public Object getTicketsForUser(@AuthenticationPrincipal User user, @ModelAttribute TicketFilter ticketFilter){
		String status = "error";
		Object messages = null;
		Object data = null;
		if(ticketFilter.getLength()==0){
			ticketFilter.setLength(100);
		}
		if(ticketFilter.getStart()<0){
			ticketFilter.setStart(0);
		}
		ListObjects  userTickets = null;
		if(user!=null){
			userTickets = listUserTicketsByTicketFilter(ticketFilter, user, false);
			ObjectMapper mapper = new ObjectMapper();
			//Object t = mapper.convertValue(userTickets.getData(), Ticket.class);
//			userTickets.setColumnsNames(columnNames);
//			Object simpleTicket = mapper.convertValue(userTickets.getData(), new TypeReference<List<SimpleTicket>>() {});
			data=userTickets;
			status="success";
		}
		return new SecureTJSONResponse(status,null,data);
	}
	
	@RequestMapping("/rest/ticket/history")
    @JsonView(SimpleObject.class)
	public Object ticketHistory(@AuthenticationPrincipal User user,@RequestParam(value="ticketId",required=false) String ticketId) throws JsonProcessingException{
		//Query ticketArchiveQuery = entityManager.createNativeQuery("SELECT ta.ticketId, ta.description, ta.modifiedBy, ta.lastUpdatedTimestamp FROM ticket_archive ta WHERE ta.ticketId=(?1) ORDER BY ta.lastUpdatedTimestamp");
		//fetch the ticket too along with archives
		List ticketHistory = new ArrayList();
		Ticket ticket = entityManager.find(Ticket.class, ticketId);
		ListObjects listObjects = new ListObjects();
		if(ticket!=null){
			//send same response type as SimpleArchive
			String organizationName = null;
			if(ticket.getResolver()!=null){
				organizationName=ticket.getResolver().getOrganization().getName();
			}
			SimpleTicketArchive simpleTicketArchive = new SimpleTicketArchive(ticket.getTicketId(),ticket.getDescription(),ticket.getStatus().getEnumerationId(),ticket.getLastUpdatedTimestamp(),ticket.getModifiedBy().getUserId(),organizationName);
			ticketHistory.add(simpleTicketArchive);

			Query ticketArchiveQuery = entityManager.createNamedQuery("getLatestSimpleTicketArchivesForTicketId");
			ticketArchiveQuery.setParameter("ticketId", ticketId);
			ticketHistory.addAll(ticketArchiveQuery.getResultList());
		}
		listObjects.setData(ticketHistory);
		listObjects.setRecordsFiltered(ticketHistory.size());
		listObjects.setRecordsTotal(ticketHistory.size());
		listObjects.setColumnsNames(historyColumnNames);
		
		return new SecureTJSONResponse("success",null,listObjects);
	}

	@RequestMapping("/rest/getVendorAndIssueTypes")
	public Object getVendorAndIssueTypes(@RequestParam("siteId") int siteId, @RequestParam("serviceTypeId") int serviceTypeId){
		Map<String,Object> vendorAndIssueType = loadVendorsAndIssueTypes(serviceTypeId,siteId);
		com.securet.ssm.persistence.objects.User user =  (com.securet.ssm.persistence.objects.User)vendorAndIssueType.get("vendors");
		cleanUser(user);
		return new SecureTJSONResponse("success", null, vendorAndIssueType);
	}

	@Transactional
	@RequestMapping(value="/rest/ticket/create",method=RequestMethod.POST)
	public Object createTicket(@RequestParam(required=false) List<MultipartFile> ticketAttachments,@AuthenticationPrincipal User user ,@Valid @ModelAttribute("ticket") Ticket ticket,BindingResult result){
		String status = "error";
		Object messages = null;
		Object data = null;
		validateAndSetDefaultsForTicket("ticket",ticket, result);
		if(!result.hasErrors() && user!=null){
			try{
				createTicketAndNotify(ticket, ticketAttachments, user, mailService, smsService);
				cleanTicketForResponse(ticket);
				data = ticket;
				status="success";
				FieldError fieldError = new FieldError("ticket", "ticket", "Ticket successfully created: "+ticket.getTicketId());
				result.addError(fieldError);
				messages=simplifyErrorMessages(result.getFieldErrors());
			}catch(Exception e){//handling every exceptions to ensure apps do not see stack...
				_logger.error("something went wrong",e);
				FieldError fieldError = new FieldError("ticket", "ticket", "Oops some configuration is missing");
				result.addError(fieldError);
				messages=simplifyErrorMessages(result.getFieldErrors());
			}
		}else{
			messages=simplifyErrorMessages(result.getFieldErrors());
		}
		return new SecureTJSONResponse(status, messages, data);
	}
	
	@Transactional
	@RequestMapping(value="/rest/ticket/update",method=RequestMethod.POST)
	public Object updateTicket(	@RequestParam(required=false) List<MultipartFile> ticketAttachments,@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser,@ModelAttribute("ticket") Ticket ticket, BindingResult result){
		String status = "error";
		Object messages = null;
		Object data = null;
		if(customUser!=null){
			if(ticket.getStatus()==null || ticket.getStatus().getEnumerationId().isEmpty()){
				FieldError fieldError = new FieldError("ticket", "status.enumerationId", "Please select a valid status");
				result.addError(fieldError);
			}
			if(ticket.getDescription()==null || ticket.getDescription().isEmpty()){
				FieldError fieldError = new FieldError("ticket", "description", "Description cannot be empty");
				result.addError(fieldError);
			}
			if(!result.hasErrors()){
				try{
					updateTicketAndNotify(ticket,ticketAttachments, customUser,mailService,smsService );
					cleanTicketForResponse(ticket);
					status="success";
					data = ticket;
					FieldError fieldError = new FieldError("ticket", "ticket", "Ticket successfully updated: "+ticket.getTicketId());
					result.addError(fieldError);
					messages=simplifyErrorMessages(result.getFieldErrors());
				}catch(Exception e){//handling every exceptions to ensure apps do not see stack...
					_logger.error("Something went wrong",e);
					status="error";
					FieldError fieldError = new FieldError("ticket", "ticket", "Oops something is wrong with this ticket");
					result.addError(fieldError);
					messages=simplifyErrorMessages(result.getFieldErrors());
				}
			}else{
				messages=simplifyErrorMessages(result.getFieldErrors());
			}
		}
		return new SecureTJSONResponse(status, messages, data);
	}
	private void cleanTicketForResponse(Ticket userTicket) {
		cleanUser(userTicket.getCreatedBy());
		cleanUser(userTicket.getModifiedBy());
		cleanUser(userTicket.getReporter());
		cleanUser(userTicket.getResolver());
		if(userTicket.getAsset()!=null){
			userTicket.getAsset().setSite(null);
		}
		if(userTicket.getIssueType()!=null){
			userTicket.getIssueType().setServiceType(null);
		}
	}
	
	private void cleanUser(com.securet.ssm.persistence.objects.User user){
		if(user!=null){
			user.setUserLogin(null);
			user.setRoles(null);
			user.setPermissions(null);
		}
	}

	@Transactional
	@RequestMapping(value="/rest/ticket/hpToolMessage",method=RequestMethod.POST)
	public Object hpToolMessage(@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser,@ModelAttribute("hpInput") HPToolInput hpToolInput, BindingResult result){
		//all tickets 
		Ticket ticket = new Ticket();
		
		//ticketType
		setTicketType(ticket, BaseTicketService.COMPLAINT);
		
		//service type
		ServiceType serviceType = new ServiceType();
		serviceType.setServiceTypeId(8);
		ticket.setServiceType(serviceType);
		
		Query siteQuery = entityManager.createNamedQuery("getSiteByName");
		siteQuery.setParameter("name",hpToolInput.getTerminalID());
		Site site =  (Site) siteQuery.getSingleResult();
		entityManager.detach(site);

		Query issueTypeQuery = entityManager.createNamedQuery("getIssueTypeForName");
		issueTypeQuery.setParameter("issueName", "%"+hpToolInput.getFault()+"%");
		issueTypeQuery.setMaxResults(1);
		IssueType issueType =  (IssueType)issueTypeQuery.getSingleResult();
		entityManager.detach(issueType);
		
		//find if we have ticket for that site with same issue type...  
		JPAQuery searchTicket = new JPAQuery(entityManager);
		JPATicket jpaTicket = JPATicket.ticket;
		searchTicket.from(jpaTicket)
		.where(jpaTicket.site.name.eq(hpToolInput.getTerminalID()).and(jpaTicket.issueType.name.eq(hpToolInput.getFault()))
				.and(jpaTicket.status.enumerationId.ne("RESOLVED").and(jpaTicket.status.enumerationId.ne("CLOSED"))));
		
		Ticket ticketFound = searchTicket.singleResult(jpaTicket);
		if(ticketFound!=null){
			ticket = ticketFound!=null?ticketFound:ticket;
			ticket.setAutoUpdateTimeFields(false);
			closeHPToolTicket(hpToolInput, ticket);
			entityManager.persist(ticket);
			entityManager.flush();
			Map<String,Object> bodyParameters = new HashMap<String, Object>();
			bodyParameters.put("ticket", ticket);
			sendNotifications(mailService, smsService, EMAIL_UPDATE_TICKET_NOTIFICATION, SMS_UPDATE_TICKET_NOTIFICATION, bodyParameters);
		}else{
			ticket.setAutoUpdateTimeFields(false);
			
			//site assignment
			ticket.setSite(site);
			
			//issue Type
			ticket.setIssueType(issueType);
			com.securet.ssm.persistence.objects.User reporter = new com.securet.ssm.persistence.objects.User();
			
			//reporter userId..
			reporter.setUserId(customUser.getUsername());
			
			ticket.setReporter(reporter);
			ticket.setCreatedBy(reporter);
			ticket.setModifiedBy(reporter);
			
			//assign vendor and asset
			assignAssetAndVendor("hpTool", ticket, result);
			
			
			Enumeration severity = new Enumeration();
			severity.setEnumerationId("MAJOR");
			ticket.setSeverity(severity );
			
			ticket.setSource("HP_TOOL");
			
			Enumeration status = new Enumeration();
			status.setEnumerationId("OPEN");
			ticket.setStatus(status);
			
			SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy HH:mm:ss a");
			if(hpToolInput.getStartedAt()!=null && !hpToolInput.getStartedAt().trim().isEmpty()){
				Date startedAt;
				try {
					startedAt = sdf.parse(hpToolInput.getStartedAt());
					ticket.setCreatedTimestamp(new Timestamp(startedAt.getTime()));
					ticket.setLastUpdatedTimestamp(new Timestamp(startedAt.getTime()));
				} catch (ParseException e) {
					_logger.debug("Error parsing date: ",e);
				}
			}

			closeHPToolTicket(hpToolInput, ticket);
		
			//get a new sequence, ALL tickets should be prefixed with C
			long ticketSequenceId = SequenceGeneratorHelper.getNextSequence("Ticket",entityManager);
			String ticketId = TICKET_PREFIX+ticketSequenceId;
			if(_logger.isDebugEnabled())_logger.debug("Ticket Id generated as "+ticketId);
			ticket.setTicketId(ticketId);
			ticket.setTicketMasterId(ticketId);
			ticket.setDescription(hpToolInput.getFault());
			entityManager.persist(ticket);
			entityManager.flush();
			Map<String,Object> bodyParameters = new HashMap<String, Object>();
			bodyParameters.put("ticket", ticket);
			sendNotifications(mailService, smsService, EMAIL_CREATE_TICKET_NOTIFICATION, SMS_UPDATE_TICKET_NOTIFICATION, bodyParameters);
		}
		return ticket;
	}

	private void closeHPToolTicket(HPToolInput hpToolInput, Ticket ticketFound) {
		SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy HH:mm:ss a");
		if(hpToolInput.getEndedAt()!=null && !hpToolInput.getEndedAt().trim().isEmpty()){
			
			Enumeration status = new Enumeration();
			status.setEnumerationId("RESOLVED");
			ticketFound.setStatus(status);
			Date endedAt;
			try {
				endedAt = sdf.parse(hpToolInput.getEndedAt());
				ticketFound.setLastUpdatedTimestamp(new Timestamp(endedAt.getTime()));
			} catch (ParseException e) {
				_logger.debug("Error parsing date: ",e);
			}
		}
	}
}