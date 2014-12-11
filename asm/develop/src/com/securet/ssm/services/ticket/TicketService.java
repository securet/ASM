package com.securet.ssm.services.ticket;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.xml.crypto.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
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
import com.securet.ssm.persistence.objects.SecureTObject;
import com.securet.ssm.persistence.objects.ServiceType;
import com.securet.ssm.persistence.objects.Site;
import com.securet.ssm.persistence.objects.Ticket;
import com.securet.ssm.persistence.objects.TicketArchive;
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

import freemarker.template.utility.StringUtil;

@Controller
@Repository
@Service
public class TicketService extends SecureTService {

	private static final int MAX_SHORT_DESC = 80;
	public static final List<String> TICKET_STATUS = new ArrayList<String>();
	static{
		TICKET_STATUS.add("OPEN");
		TICKET_STATUS.add("WORK_IN_PROGRESS");
		TICKET_STATUS.add("RESOLVED");
		TICKET_STATUS.add("CLOSED");
	}

	// should make a query planner, to many queries - TODO - data table query planner for custom queries 
	private static final String CLIENT_USER_TICKET_NATIVE_QUERY = "SELECT t.* from ticket t INNER JOIN client_user_site cus ON t.siteId=cus.siteId WHERE cus.userId=(?1)";
	private static final String VENDOR_SITE_TICKET_NATIVE_QUERY = "SELECT t.* from ticket t INNER JOIN vendor_service_asset vsa ON t.assetId=vsa.assetId WHERE t.ticketType!='LOG' AND vsa.userId=(?1)";

	private static final String TICKET_NATIVE_QUERY = "SELECT t.* from ticket t ";
	private static final String TICKET_COUNT_NATIVE_QUERY = "SELECT COUNT(t.ticketId) from ticket t ";
	
	private static final String SERVICE_ISSUE_TYPE_JOIN_CLAUSE = " INNER JOIN service_type st ON t.serviceTypeId=st.serviceTypeId INNER JOIN issue_type it ON t.issueTypeId=it.issueTypeId";
	
	//client queries
	private static final String CLIENT_USER_SITE_JOIN_CLAUSE = " INNER JOIN client_user_site cus ON t.siteId=cus.siteId ";
	private static final String CLIENT_USER_FILTER = " cus.userId=(?1) ";
	
	//vendor queries
	private static final String VENDOR_SERVICE_ASSET_JOIN_CLAUSE = " INNER JOIN vendor_service_asset vsa ON t.assetId=vsa.assetId";
	private static final String VENDOR_USER_FILTER = " t.ticketType!='LOG' AND vsa.userId=(?1) ";
	
	//Common Join and Filters
	private static final String TICKET_STATUS_FILTER = " t.statusId IN (?2) ";
	private static final String TICKET_COMMON_FILTER_CLAUSE=" (t.statusId like (?3) OR t.Description like (?4) OR  st.name like (?5) OR it.name like (?6) OR t.ticketType like (?7)) ";  
	private static final int TICKET_COMMON_FILTER_PARAMS_SIZE=5;  
	
	//Queries to show navigation filters 
	private static final String COUNT_CLIENT_USER_TICKET_NATIVE_QUERY = "SELECT COUNT(t.ticketId) from ticket t INNER JOIN client_user_site cus ON t.siteId=cus.siteId WHERE cus.userId=(?1)";
	private static final String COUNT_VENDOR_SITE_TICKET_NATIVE_QUERY = "SELECT COUNT(t.ticketId) from ticket t INNER JOIN vendor_service_asset vsa ON t.assetId=vsa.assetId WHERE t.ticketType!='LOG' AND vsa.userId=(?1)";
	
	private static final String EMAIL_CREATE_TICKET_NOTIFICATION = "EMAIL_CREATE_TICKET_NOTIFICATION";
	private static final String SMS_CREATE_TICKET_NOTIFICATION = "SMS_CREATE_TICKET_NOTIFICATION";

	private static final String EMAIL_UPDATE_TICKET_NOTIFICATION = "EMAIL_UPDATE_TICKET_NOTIFICATION";
	private static final String SMS_UPDATE_TICKET_NOTIFICATION = "SMS_UPDATE_TICKET_NOTIFICATION";

	private static final String LOG = "LOG";
	private static final String COMPLAINT = "COMPLAINT";

	private static final String OPEN = "OPEN";
	private static final String WORK_IN_PROGRESS = "WORK_IN_PROGRESS";
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
	public String listTickets(@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser,@RequestParam(value="filterStatus",required=false) String filterStatus,Model model){
		boolean isReporter = !customUser.getAuthorities().contains(SecureTAuthenticationSuccessHandler.resolverAuthority);
		model.addAttribute("isReporter", isReporter);
		model.addAttribute("userName", customUser.getUsername());
		if(filterStatus!=null && !filterStatus.isEmpty()){
			model.addAttribute("filterStatus", filterStatus);
		}
		return DefaultService.TICKET+"listTickets";
	}

	@Transactional
	@RequestMapping(value="/tickets/listUserTickets",produces="application/json")
	public @ResponseBody ListObjects listUserTickets(@ModelAttribute DataTableCriteria columns, @RequestParam("entityName") String entityName,@RequestParam("operator") String operator,@RequestParam(value="filterStatus",required=false) String filterStatus,@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser,HttpServletRequest request){
		boolean isReporter = !customUser.getAuthorities().contains(SecureTAuthenticationSuccessHandler.resolverAuthority);
		StringBuilder ticketQueryStr = new StringBuilder();
		//ticketQueryStr.append(TICKET_NATIVE_QUERY);
		ticketQueryStr.append(SERVICE_ISSUE_TYPE_JOIN_CLAUSE);
		if(isReporter){
			ticketQueryStr.append(CLIENT_USER_SITE_JOIN_CLAUSE).append(DataTableCriteria.WHERE);
			ticketQueryStr.append(DataTableCriteria.START_BRACKET).append(TICKET_STATUS_FILTER).append(DataTableCriteria.AND).append(DataTableCriteria.SPACE);
			ticketQueryStr.append(CLIENT_USER_FILTER).append(DataTableCriteria.END_BRACKET);
		}else{
			ticketQueryStr.append(VENDOR_SERVICE_ASSET_JOIN_CLAUSE).append(" WHERE ");;
			ticketQueryStr.append(DataTableCriteria.START_BRACKET).append(TICKET_STATUS_FILTER).append(DataTableCriteria.AND).append(DataTableCriteria.SPACE);
			ticketQueryStr.append(VENDOR_USER_FILTER).append(DataTableCriteria.END_BRACKET);
		}
		
		String textToSearch = DataTableCriteria.getDefaultTextToSearch(columns);
		boolean textSearchEnabled = textToSearch!=null && !textToSearch.isEmpty();
		if(textSearchEnabled){
			ticketQueryStr.append(DataTableCriteria.SPACE).append(DataTableCriteria.AND).append(DataTableCriteria.SPACE);
			ticketQueryStr.append(TICKET_COMMON_FILTER_CLAUSE);
		}

		StringBuilder ticketListQueryStr = new StringBuilder();
		ticketListQueryStr.append(TICKET_NATIVE_QUERY).append(ticketQueryStr.toString());
		Query ticketListQuery = entityManager.createNativeQuery(ticketListQueryStr.toString(), Ticket.class);

		StringBuilder ticketCountQueryStr = new StringBuilder();
		ticketCountQueryStr.append(TICKET_COUNT_NATIVE_QUERY).append(ticketQueryStr.toString());
		Query ticketCountQuery = entityManager.createNativeQuery(ticketCountQueryStr.toString());
		ticketListQuery.setParameter(1, customUser.getUsername());
		ticketCountQuery.setParameter(1, customUser.getUsername());
		if(filterStatus!=null && !filterStatus.isEmpty()){
			ticketListQuery.setParameter(2, filterStatus);
			ticketCountQuery.setParameter(2, filterStatus);
		}else{
			ticketListQuery.setParameter(2, TICKET_STATUS);
			ticketCountQuery.setParameter(2, TICKET_STATUS);
		}
			
		int paramCount = ticketListQuery.getParameters().size();
		if(textSearchEnabled){
			textToSearch = DataTableCriteria.PERCENTILE+textToSearch+DataTableCriteria.PERCENTILE;
			for(int i=3;i<=paramCount;i++){
				ticketListQuery.setParameter(i, textToSearch);
				ticketCountQuery.setParameter(i, textToSearch);
			}
		}
		
		Map<String,Query> jpaQueriesToRun = new HashMap<String, Query>();
		jpaQueriesToRun.put(DataTableCriteria.DATA_QUERY, ticketListQuery);
		jpaQueriesToRun.put(DataTableCriteria.COUNT_QUERY, ticketCountQuery);

		if(_logger.isDebugEnabled())_logger.debug("quries to run "+jpaQueriesToRun);
		return ActionHelpers.listSimpleObjectFromQuery(entityManager, columns, jpaQueriesToRun);
	}

	
	@Transactional
	@RequestMapping(value="/tickets/saveTicket",method=RequestMethod.POST)
	public String saveTicket(@RequestParam(required=false) List<MultipartFile> ticketAttachments,@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser,@Valid @ModelAttribute("formObject") Ticket formObject, BindingResult result,Model model){
		validateAndSetDefaultsForTicket(formObject,result);		
		if(!result.hasErrors()){
			createTicket(formObject,customUser.getUsername());
			saveAttachments(formObject,ticketAttachments,true);
			if(!isLog(formObject)){
				sendNotifications(formObject, EMAIL_CREATE_TICKET_NOTIFICATION, SMS_CREATE_TICKET_NOTIFICATION);
			}
			model.addAttribute("saved", true);
		}else{
			loadDefaults(model, customUser.getUsername());
			//preload all the necessary data...
			preloadTicketForm(formObject, model);
			return DefaultService.TICKET+"newTicket";
		}
		//all is fine.. load the user tickets
		return listTickets(customUser,null, model);
	}


	@Transactional
	@RequestMapping("/tickets/modifyTicket")
	public String editTicket(@RequestParam("id") String ticketId,@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser, Model model){
		//find if the ticket is part of the user or his organization group and then allow them to edit
		Ticket currentTicket = null;
		//consider admin as the client as of now
		String selectedQuery = CLIENT_USER_TICKET_NATIVE_QUERY;
		boolean isResolver = customUser.getAuthorities().contains(SecureTAuthenticationSuccessHandler.resolverAuthority);
		if(isResolver){
			//find the logged in user, assigned service types and sites 
			selectedQuery=VENDOR_SITE_TICKET_NATIVE_QUERY;			
		}
		List<Ticket> userTickets =  getUserTickets(customUser,selectedQuery,ticketId);
		//if tickets found, the user should be allowed to view, else.. show him his tickets list - error message??
		if(userTickets.size()>0){
			currentTicket = userTickets.get(0);
			if(isResolver && currentTicket.getStatus().getEnumerationId().equals(OPEN)){
				//set the status to WORK IN PROGRESS
				currentTicket = updateTicket(currentTicket, WORK_IN_PROGRESS, currentTicket.getDescription(), customUser.getUsername());
			}
			_logger.debug("no of attachments : "+currentTicket.getAttachments().size());
		}
		if(currentTicket==null){
			return listTickets(customUser,null,model);
		}
		return loadEditTicketModel(model, currentTicket);
	}

	private String loadEditTicketModel(Model model, Ticket currentTicket) {
		//also load the archive...
		List<SecureTObject> ticketArchives = fetchQueriedObjects("getLatestTicketArchivesForTicketId", "ticketId", currentTicket.getTicketId());
		model.addAttribute("ticketArchives", ticketArchives);// pagination???
		model.addAttribute("formObject",currentTicket);
		return DefaultService.TICKET+"modifyTicket";
	}

	@Transactional
	@RequestMapping(value="/tickets/updateTicket",method=RequestMethod.POST)
	public String updateTicket(@RequestParam(required=false) List<MultipartFile> ticketAttachments,@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser,@Valid @ModelAttribute("formObject") Ticket formObject, BindingResult result,Model model){
		if(formObject.getStatus()==null || formObject.getStatus().getEnumerationId().isEmpty()){
			FieldError fieldError = new FieldError("formObject", "status.enumerationId", "Please select a valid status");
			result.addError(fieldError);
		}
		if(!result.hasErrors()){
			if(ticketAttachments!=null && ticketAttachments.size()>0){
				saveAttachments(formObject, ticketAttachments,false);
			}
			formObject = updateTicket(formObject, formObject.getStatus().getEnumerationId(), formObject.getDescription(), customUser.getUsername());
			if(!isLog(formObject)){
				sendNotifications(formObject, EMAIL_UPDATE_TICKET_NOTIFICATION, SMS_UPDATE_TICKET_NOTIFICATION);
			}
		}else{
			String responseString = editTicket(formObject.getTicketId(), customUser, model);
			return responseString;
		}
		return listTickets(customUser,null, model);
	}

	
	private List<Ticket> getUserTickets(org.springframework.security.core.userdetails.User customUser,String query, String ticketId){
		StringBuilder queryString = new StringBuilder(query);
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
			setShortDescription(formObject);
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

	private void setShortDescription(Ticket formObject) {
		String description = formObject.getDescription();
		if(description.length()<MAX_SHORT_DESC){
			formObject.setShortDesc(description);
		}else{
			formObject.setShortDesc(description.substring(0, MAX_SHORT_DESC));	
		}
	}

	private boolean isLog(Ticket formObject) {
		return formObject.getTicketType().getEnumerationId().equals(LOG);
	}

	private Ticket updateTicket(Ticket ticket, String newStatus, String description,String modifiedUser){
		///load the ticket first 
		Ticket storedTicket = entityManager.find(Ticket.class, ticket.getTicketId());
		TicketArchive ticketArchive = new TicketArchive(storedTicket);
		ticket.setDescription(description);
		
		User currentUser = new User();
		currentUser.setUserId(modifiedUser);
		storedTicket.setModifiedBy(currentUser);
		
		Enumeration status = new Enumeration();
		status.setEnumerationId(newStatus);
		storedTicket.setStatus(status);
		storedTicket.setDescription(ticket.getDescription());
		setShortDescription(storedTicket);
		
		entityManager.persist(ticketArchive);
		entityManager.merge(storedTicket);
		entityManager.flush();
		entityManager.refresh(storedTicket);

		return storedTicket;
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
	}

	private void saveAttachments(Ticket formObject, List<MultipartFile> ticketAttachments,boolean refresh) {
		int index = 0;
		boolean savedAttachments = false;
		if(formObject.getAttachments()!=null){
			index = formObject.getAttachments().size();
		}else{
			formObject.setAttachments(new ArrayList<TicketAttachment>());
		}
		for(MultipartFile attachment : ticketAttachments){
			if(!attachment.isEmpty()){//check if attachments are empty...
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
					savedAttachments=true;
				}
			}
		}
		if(refresh && savedAttachments){
			entityManager.refresh(formObject);
		}
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

	private void sendNotifications(Ticket formObject,String emailTemplate, String smsTemplate) {
		sendEmail(formObject,emailTemplate);
		sendSMS(formObject,smsTemplate);
	}

	private void sendSMS(Ticket formObject,String templateName) {
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
			mailTemplateQuery.setParameter("templateName", templateName);
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

	private void sendEmail(Ticket formObject,String templateName) {
		//get the user email... this can run in background.. 
		Map<String,Object> mailContext = new HashMap<String,Object>();
		StringBuilder toAddress = new StringBuilder();
		if(formObject.getReporter().getEmailId()!=null){
			toAddress.append(formObject.getReporter().getEmailId()).append(",");
		}
		if(formObject.getResolver().getEmailId()!=null){
			toAddress.append(formObject.getResolver().getEmailId());
		}
		if(toAddress.length()>0){
			_logger.error("No email address found.. so not notifying for ticket: "+ formObject.getTicketId());
			return;
		}
		Query mailTemplateQuery = entityManager.createNamedQuery("getMailTemplateByName");
		mailTemplateQuery.setParameter("templateName", templateName);
		MailTemplate mailTemplate = (MailTemplate)mailTemplateQuery.getSingleResult();
		mailContext.put("to",toAddress.toString());

		mailContext.put("contentType",mailTemplate.getContentType());
		mailContext.put("from",mailTemplate.getFrom());
		mailContext.put("subject",mailTemplate.getSubject());
		mailContext.put("template",mailTemplate.getTemplateFileName());
		
		Map<String,Object> bodyParameters = new HashMap<String, Object>();
		bodyParameters.put("ticket", formObject);
		mailContext.put("bodyParameters",bodyParameters);
		try{
			mailService.sendMail(mailContext);
		}catch(MailSendException e){
			_logger.error("Could not send email for ticket :"+formObject.getTicketId() +" check the stack trace", e);
		}
	}

	public static void fetchTicketStats(EntityManager entityManager, org.springframework.security.core.userdetails.User customUser, ModelMap map) {
		boolean isReporter = !customUser.getAuthorities().contains(SecureTAuthenticationSuccessHandler.resolverAuthority);
		StringBuilder baseQuery = new StringBuilder();
		if(isReporter){
			baseQuery.append(COUNT_CLIENT_USER_TICKET_NATIVE_QUERY);
		}else{
			baseQuery.append(COUNT_VENDOR_SITE_TICKET_NATIVE_QUERY);
		}
		baseQuery.append(" AND ").append("t.statusId=(?2)");
		_logger.debug("query to get tickets stats:" + baseQuery.toString());
		for(String status:TICKET_STATUS){
			Query query = entityManager.createNativeQuery(baseQuery.toString());
			query.setParameter(1, customUser.getUsername());
			query.setParameter(2, status);
			Number result = (Number) query.getSingleResult ();
			map.put(status.toLowerCase()+"TicketsCount",result);	
		}
	}
	
	private Map<ColumnCriterias,String> addSearchableFieldToColumnCriteria(String fieldPath, String value){
		Map<ColumnCriterias,String> userColumn = new HashMap<DataTableCriteria.ColumnCriterias, String>();
		userColumn.put(ColumnCriterias.data, fieldPath);
		userColumn.put(ColumnCriterias.searchable,"true");
		userColumn.put(ColumnCriterias.searchValue,value);
		return userColumn;
	}
	
}