package com.securet.ssm.services.rest;

import java.util.ArrayList;
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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.securet.ssm.components.mail.MailService;
import com.securet.ssm.components.sms.SMSService;
import com.securet.ssm.persistence.objects.SecureTObject.SimpleObject;
import com.securet.ssm.persistence.objects.Ticket;
import com.securet.ssm.persistence.objects.TicketArchive;
import com.securet.ssm.persistence.views.SimpleTicket;
import com.securet.ssm.services.admin.AdminService;
import com.securet.ssm.services.ticket.BaseTicketService;
import com.securet.ssm.services.vo.DataTableCriteria;
import com.securet.ssm.services.vo.ListObjects;

@RestController
@Repository
@Service
public class TicketRestfulService extends BaseTicketService{

	private static final List<String> columnNames = new ArrayList<String>();
	static{
		columnNames.add("ticketId");
		columnNames.add("statusId");
		columnNames.add("siteId");
		columnNames.add("site");
		columnNames.add("serviceTypeId");
		columnNames.add("serviceType");
		columnNames.add("createdTimestamp");
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
	private AdminService adminService;

	@Autowired
	private MailService mailService;

	@Autowired
	private SMSService smsService;

	
	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

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
				Ticket userTicket = getUserTicket(ticketId, loggedInUser);
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
	public Object getTicketsForUser(@AuthenticationPrincipal User user,@RequestParam(value="filter",required=false) String statusFilter, @ModelAttribute DataTableCriteria columns){
		String status = "error";
		Object messages = null;
		Object data = null;
		if(columns.getLength()==0){
			columns.setLength(100);
		}
		if(columns.getStart()==0){
			columns.setStart(1);
		}
		ListObjects  userTickets = null;
		if(user!=null){
			userTickets = listUserTickets(columns, statusFilter, user, false);
			ObjectMapper mapper = new ObjectMapper();
			//Object t = mapper.convertValue(userTickets.getData(), Ticket.class);
			userTickets.setColumnsNames(columnNames);
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
		Query ticketArchiveQuery = entityManager.createNamedQuery("getLatestSimpleTicketArchivesForTicketId");
		ticketArchiveQuery.setParameter("ticketId", ticketId);
		List ticketHistory = ticketArchiveQuery.getResultList();
		ListObjects listObjects = new ListObjects();
		listObjects.setData(ticketHistory);
		listObjects.setRecordsFiltered(ticketHistory.size());
		listObjects.setRecordsTotal(ticketHistory.size());
		listObjects.setColumnsNames(historyColumnNames);
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new Hibernate4Module());
        mapper.getSerializationConfig().withView(SimpleObject.class);
        String str3 = mapper.writeValueAsString(ticketHistory);
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
		validateAndSetDefaultsForTicket(ticket, result);
		if(!result.hasErrors()){
			createTicketAndNotify(ticket, ticketAttachments, user, mailService, smsService);
			cleanTicketForResponse(ticket);
			data = ticket;
			status="success";
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
				}catch(Exception e){
					_logger.error("Something went wrong",e);
					status="error";
					FieldError fieldError = new FieldError("ticket", "ticket", "Something went wrong, check with server logs");
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
}
