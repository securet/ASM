package com.securet.ssm.services.ticket;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.securet.ssm.components.authentication.SecureTAuthenticationSuccessHandler;
import com.securet.ssm.components.mail.MailService;
import com.securet.ssm.components.sms.SMSService;
import com.securet.ssm.persistence.SequenceGeneratorHelper;
import com.securet.ssm.persistence.objects.Asset;
import com.securet.ssm.persistence.objects.Enumeration;
import com.securet.ssm.persistence.objects.MailTemplate;
import com.securet.ssm.persistence.objects.ServiceType;
import com.securet.ssm.persistence.objects.Site;
import com.securet.ssm.persistence.objects.Ticket;
import com.securet.ssm.persistence.objects.TicketAttachment;
import com.securet.ssm.persistence.objects.User;
import com.securet.ssm.persistence.objects.VendorServiceAsset;
import com.securet.ssm.services.ActionHelpers;
import com.securet.ssm.services.DefaultService;
import com.securet.ssm.services.SecureTService;
import com.securet.ssm.services.admin.AdminService;
import com.securet.ssm.services.vo.DataTableCriteria;
import com.securet.ssm.services.vo.ListObjects;
import com.securet.ssm.services.vo.DataTableCriteria.ColumnCriterias;
import com.securet.ssm.utils.SecureTUtils;

@Controller
@Repository
@Service
public class TicketService extends SecureTService {

	private static final String BASE_CLIENT_USER_NATIVE_QUERY = "SELECT t.* from ticket t INNER JOIN client_user_site cus ON t.siteId=cus.siteId WHERE cus.userId=(?1)";
	private static final String EMAIL_CREATE_TICKET_NOTIFICATION = "EMAIL_CREATE_TICKET_NOTIFICATION";
	private static final String SMS_CREATE_TICKET_NOTIFICATION = "SMS_CREATE_TICKET_NOTIFICATION";
	private static final String LOG = "LOG";
	private static final String COMPLAINT = "COMPLAINT";

	private static final String OPEN = "OPEN";
	private static final String CLOSED = "CLOSED";
	
	private static final String ALL_OK = "ALL OK";
	private static final String TICKET_PREFIX = "C";

	private static final Logger _logger = LoggerFactory.getLogger(TicketService.class);
	private static List<String> dataViewNames = null;  

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

	public static List<String> getDataViewNames() {
		if(dataViewNames==null){
			dataViewNames=new ArrayList<String>();
			dataViewNames.add("getServiceTypeForView");
		}
		return dataViewNames;
	}
	
	@RequestMapping("/tickets/newTicket")
	public String newTicket(Model model,@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser){
		String userId = customUser.getUsername();
		model.addAttribute("formObject",new Ticket());
		loadDefaults(model, userId);
		makeUIData(entityManager,model,"Ticket");
		return DefaultService.TICKET+"newTicket";
	}

	private void loadDefaults(Model model, String userId) {
		List userAssignedSites = fetchQueriedObjects("getUserAssignedSites", "userId", userId);
		List serviceTypes = entityManager.createNamedQuery("getServiceTypeForView").getResultList();
		List severity = entityManager.createNamedQuery("getSeverityForView").getResultList();
		model.addAttribute("userAssignedSites", userAssignedSites);
		model.addAttribute("serviceTypes", serviceTypes);
		model.addAttribute("severity", severity);
	}

	@RequestMapping(value="/tickets/getVendorsAndIssueTypes",produces="application/json")
	public @ResponseBody Map<String,Object> getVendorsAndIssueTypes(@RequestParam String serviceTypeId, @RequestParam String siteId){
		int serviceTypeIdInt=0;
		int siteIdInt=0;
		try{
			serviceTypeIdInt = Integer.parseInt(serviceTypeId);
			siteIdInt = Integer.parseInt(siteId);
		}catch(NumberFormatException e){
			_logger.error("Incorrect site/serviceType id :"+siteId+"/"+serviceTypeId,e);
		}
		return loadVendorsAndIssueTypes(serviceTypeIdInt, siteIdInt);
	}

	private Map<String, Object> loadVendorsAndIssueTypes(int serviceTypeId, int siteId) {
		Map<String,Object> responseObjects = new HashMap<String, Object>(); 
		List issueTypes = fetchQueriedObjects("getIssueTypeForService", "serviceTypeId",Integer.valueOf(serviceTypeId));
		responseObjects.put("issueTypes", issueTypes);
		Query vendorsQuery =  entityManager.createNamedQuery("getVendorByServiceType");
		vendorsQuery.setParameter("serviceTypeId", serviceTypeId);
		vendorsQuery.setParameter("siteId", siteId);
		vendorsQuery.setMaxResults(1);
		List vendors=vendorsQuery.getResultList();//we will use the first result always.. check if we do pick the latest
		if(vendors!=null && vendors.size()>0){
			responseObjects.put("vendors", vendors.get(0));
		}else{
			responseObjects.put("vendors", null);
		}
		return responseObjects;
	}

	@RequestMapping("/tickets/listTickets")
	public String listTickets(@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser,Model model){
		boolean isReporter = !customUser.getAuthorities().contains(SecureTAuthenticationSuccessHandler.resolverAuthority);
		model.addAttribute("isReporter", isReporter);
		model.addAttribute("userName", customUser.getUsername());
		return DefaultService.TICKET+"listTickets";
	}

	@Transactional
	@RequestMapping(value="/tickets/listUserTickets",produces="application/json")
	public @ResponseBody ListObjects loadSimpleObjects(@ModelAttribute DataTableCriteria columns, @RequestParam("entityName") String entityName,@RequestParam("operator") String operator,@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser,HttpServletRequest request){
		boolean isReporter = !customUser.getAuthorities().contains(SecureTAuthenticationSuccessHandler.resolverAuthority);
		//add the user filter
		Map<ColumnCriterias,String> userColumn = new HashMap<DataTableCriteria.ColumnCriterias, String>();
		if(isReporter){
			userColumn.put(ColumnCriterias.data, "reporter.userId");
		}else{
			userColumn.put(ColumnCriterias.data, "resolver.userId");
		}
		userColumn.put(ColumnCriterias.searchable,"true");
		userColumn.put(ColumnCriterias.searchValue,customUser.getUsername());
		columns.getColumns().add(userColumn);
		if(_logger.isDebugEnabled())_logger.debug("load objects for "+entityName);
		return ActionHelpers.loadSimpleObjects(entityManager, columns, entityName, operator, request);
	}

	
	@Transactional
	@RequestMapping(value="/tickets/saveTicket",method=RequestMethod.POST)
	public String saveTicket(@RequestParam(required=false) List<MultipartFile> ticketAttachments,@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser,@Valid @ModelAttribute("formObject") Ticket formObject, BindingResult result,Model model){
		validateAndSetDefaultsForTicket(formObject,result);		
		if(!result.hasErrors()){
			createTicket(formObject,customUser.getUsername());
			saveAttachments(formObject,ticketAttachments);
			if(!isLog(formObject)){
				sendNotifications(formObject);
			}
		}else{
			loadDefaults(model, customUser.getUsername());
			//preload all the necessary data...
			preloadTicketForm(formObject, model);
			return DefaultService.TICKET+"newTicket";
		}
		//all is fine.. load the user tickets
		return listTickets(customUser, model);
	}


	@Transactional
	@RequestMapping("/tickets/modifyTicket")
	public String editTicket(@RequestParam("id") String ticketId,@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser, Model model){
		//find if the ticket is part of the user or his organization group and then allow them to edit
		Ticket currentTicket = null;
		if(customUser.getAuthorities().contains(SecureTAuthenticationSuccessHandler.resolverAuthority)){
			
		}else{
			//consider admin as the client as of now
			List<Ticket> userTickets =  getClientUserTicket(customUser,ticketId);
			//if tickets found, the user shoud be allowed to view, else.. show him his tickets list
			if(userTickets.size()>0){
				currentTicket = userTickets.get(0);
				_logger.debug("no of attachments : "+currentTicket.getAttachments().size());
			}
		}
		
		if(currentTicket==null){
			return listTickets(customUser,model);
		}
		model.addAttribute("formObject",currentTicket);
		return DefaultService.TICKET+"modifyTicket";
	}

	private List<Ticket> getClientUserTicket(org.springframework.security.core.userdetails.User customUser, String ticketId){
		StringBuilder queryString = new StringBuilder(BASE_CLIENT_USER_NATIVE_QUERY);
		if(ticketId!=null){
			queryString.append(" AND ").append("t.ticketId=(?2)");
		}
		Query clientTickets = entityManager.createNativeQuery(queryString.toString(),Ticket.class);
		clientTickets.setParameter(1, customUser.getUsername());
		if(ticketId!=null){
			clientTickets.setParameter(2, ticketId);
		}
		
		return clientTickets.getResultList();
	}
	

	private void createTicket(Ticket formObject, String reporterUserId) {
			//get a new sequence, ALL tickets should be prefixed with C
			long ticketSequenceId = SequenceGeneratorHelper.getNextSequence("Ticket",entityManager);
			String ticketId = TICKET_PREFIX+ticketSequenceId;
			if(_logger.isDebugEnabled())_logger.debug("Ticket Id generated as "+ticketId);
			formObject.setTicketId(ticketId);
			formObject.setTicketMasterId(ticketId);
			//Identify asset using site and service
			Query vendorAssetQuery = entityManager.createNamedQuery("getVendorServiceAssetByServiceType");
			vendorAssetQuery.setParameter("serviceTypeId", formObject.getServiceType().getServiceTypeId());
			vendorAssetQuery.setParameter("siteId", formObject.getSite().getSiteId());
			List<VendorServiceAsset> vendorServiceAssets = vendorAssetQuery.getResultList();
			if(vendorServiceAssets!=null && vendorServiceAssets.size()>0){
				VendorServiceAsset vendorServiceAsset =vendorServiceAssets.get(0); 
				entityManager.detach(vendorServiceAsset);

				//set the asset
				Asset asset = vendorServiceAsset.getAsset();
				formObject.setAsset(asset);
				
				//set the vendor
				User vendorUser = vendorServiceAsset.getVendorUser();
				formObject.setResolver(vendorUser);
				
			}
			//reporter, resolver, status, createdBy
			User reporter = new User();
			reporter.setUserId(reporterUserId);
			formObject.setReporter(reporter);
			formObject.setCreatedBy(reporter);
			formObject.setModifiedBy(reporter);
			
			Enumeration status = new Enumeration();
			if(isLog(formObject)){
				status.setEnumerationId(CLOSED);
			}else{
				status.setEnumerationId(OPEN);
			}
			formObject.setStatus(status);
			entityManager.persist(formObject);
	}

	private boolean isLog(Ticket formObject) {
		return formObject.getTicketType().getEnumerationId().equals(LOG);
	}

	private ServiceType setTicketType(Ticket formObject) {
		//if service Type text is ALL OK - then it is a LOG, otherwise every ticket will default to COMPLAINT
		ServiceType serviceType = entityManager.find(ServiceType.class, formObject.getServiceType().getServiceTypeId());
		if(serviceType.getName().equals(ALL_OK)){
			//this is a log
			setTicketType(formObject, LOG);
			//also close the ticket
			
		}else{
			setTicketType(formObject, COMPLAINT);
		}
		return serviceType;
	}

	private void setTicketType(Ticket formObject, String enumerationId) {
		Enumeration ticketType = new Enumeration();
		ticketType.setEnumerationId(enumerationId);
		formObject.setTicketType(ticketType);
	}

	private void validateAndSetDefaultsForTicket(Ticket formObject,BindingResult result) {
		if(formObject.getSite().getSiteId()==0){
			FieldError fieldError = new FieldError("formObject", "site.siteId", "Please Select a Site");
			result.addError(fieldError);
		}
		if(formObject.getServiceType()==null || formObject.getServiceType().getServiceTypeId()==0){
			FieldError fieldError = new FieldError("formObject", "serviceType.serviceTypeId", "Please Select a Service Type");
			result.addError(fieldError);
		}else{
			ServiceType serviceType = setTicketType(formObject);
			formObject.setServiceType(serviceType);
		}
		
		if(!result.hasErrors() && !isLog(formObject)){
			if(formObject.getIssueType()==null || formObject.getIssueType().getIssueTypeId()==0){
				FieldError fieldError = new FieldError("formObject", "issueType.issueTypeId", "Please Select a Issue Type");
				result.addError(fieldError);
			}
			if(formObject.getSeverity().getEnumerationId()==null || formObject.getSeverity().getEnumerationId().isEmpty()){
				FieldError fieldError = new FieldError("formObject", "severity.enumerationId", "Please Select Severity");
				result.addError(fieldError);
			}
		}
		if(formObject.getDescription()==null || formObject.getDescription().isEmpty()){
			FieldError fieldError = new FieldError("formObject", "description", "Please enter the description");
			result.addError(fieldError);
		}
	}

	private void saveAttachments(Ticket formObject, List<MultipartFile> ticketAttachments) {
		int index = 0;
		if(formObject.getAttachments()!=null){
			index = formObject.getAttachments().size();
		}else{
			formObject.setAttachments(new ArrayList<TicketAttachment>());
		}
		for(MultipartFile attachment : ticketAttachments){
			String fileName = formObject.getTicketId()+"_"+index+"_"+attachment.getOriginalFilename();
			String attachmentPath = SecureTService.ASSETS_SSMUPLOADS_TICKETATTACHMENTS+fileName;
			String savedPath = SecureTUtils.saveToFile(attachment, SecureTService.ASSETS_SSMUPLOADS_TICKETATTACHMENTS,fileName);
			_logger.debug("Path for saved attachment: "+attachmentPath);
			if(savedPath!=null){
				TicketAttachment ticketAttachment = new TicketAttachment();
				ticketAttachment.setTicket(formObject);
				ticketAttachment.setAttachmentName(attachment.getOriginalFilename());
				ticketAttachment.setAttachmentPath(attachmentPath);
				entityManager.persist(ticketAttachment);
			}
		}
		entityManager.refresh(formObject);
		_logger.debug("formObject: "+formObject.getAttachments());
	}

	private void preloadTicketForm(Ticket formObject, Model model) {
		int siteId = formObject.getSite().getSiteId();
		int serviceTypeId = formObject.getServiceType()!=null?formObject.getServiceType().getServiceTypeId():0;
		boolean siteExists = siteId>0;
		boolean serviceTypeExists = serviceTypeId>0;
		if(siteExists){
			Query siteQuery = entityManager.createNamedQuery("getSiteById");
			siteQuery.setParameter("id", siteId);
			Site site = (Site)siteQuery.getSingleResult();
			model.addAttribute("selectedSite", site);
		}
		if(siteExists && serviceTypeExists){
			//load the user and issue types
			Map<String,Object> vendorsAndIssueTypes = loadVendorsAndIssueTypes(serviceTypeId, siteId);
			model.addAllAttributes(vendorsAndIssueTypes);
		}
		
	}

	private void sendNotifications(Ticket formObject) {
		sendEmail(formObject);
		sendSMS(formObject);
	}

	private void sendSMS(Ticket formObject) {
		List<String> receiverContacts = new ArrayList<String>();
		switch (formObject.getStatus().getEnumerationId()) {
		case "OPEN":
		case "CLOSED":
			receiverContacts.add(formObject.getResolver().getMobile());
			break;
		case "WORK_IN_PROGRESS":
		case "RESOLVED":
			receiverContacts.add(formObject.getReporter().getMobile());
			break;
		default:
			break;
		}
		if(receiverContacts.size()>0){
			Map<String,Object> smsContext = new HashMap<String, Object>();
			Query mailTemplateQuery = entityManager.createNamedQuery("getMailTemplateByName");
			mailTemplateQuery.setParameter("templateName", SMS_CREATE_TICKET_NOTIFICATION);
			MailTemplate mailTemplate = (MailTemplate)mailTemplateQuery.getSingleResult();
			Map<String,Object> bodyParameters = new HashMap<String, Object>();
			bodyParameters.put("ticket", formObject);
			smsContext.put("bodyParameters", bodyParameters);
			smsContext.put("template",mailTemplate.getTemplateFileName());
			for(String contactNumber : receiverContacts){
				smsContext.put("contactNumber", contactNumber);
				try {
					smsService.sendSMS(smsContext);
				} catch (UnsupportedEncodingException e) {
					_logger.error("Could not send sms for the context:" + smsContext,e);
				}
			}
		}
	}

	private void sendEmail(Ticket formObject) {
		//get the user email... this can run in background.. 
		Map<String,Object> mailContext = new HashMap<String,Object>();
		StringBuilder toAddress = new StringBuilder(); 
		toAddress.append(formObject.getReporter().getEmailId()).append(",").append(formObject.getResolver().getEmailId());
		Query mailTemplateQuery = entityManager.createNamedQuery("getMailTemplateByName");
		mailTemplateQuery.setParameter("templateName", EMAIL_CREATE_TICKET_NOTIFICATION);
		MailTemplate mailTemplate = (MailTemplate)mailTemplateQuery.getSingleResult();
		mailContext.put("to",toAddress.toString());

		mailContext.put("contentType",mailTemplate.getContentType());
		mailContext.put("from",mailTemplate.getFrom());
		mailContext.put("subject",mailTemplate.getSubject());
		mailContext.put("template",mailTemplate.getTemplateFileName());
		
		Map<String,Object> bodyParameters = new HashMap<String, Object>();
		bodyParameters.put("ticket", formObject);
		mailContext.put("bodyParameters",bodyParameters);

		mailService.sendMail(mailContext);
	}
}