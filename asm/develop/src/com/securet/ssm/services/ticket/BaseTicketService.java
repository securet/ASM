package com.securet.ssm.services.ticket;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSendException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import com.securet.ssm.components.authentication.SecureTAuthenticationSuccessHandler;
import com.securet.ssm.components.mail.MailService;
import com.securet.ssm.components.sms.SMSService;
import com.securet.ssm.persistence.SequenceGeneratorHelper;
import com.securet.ssm.persistence.objects.Asset;
import com.securet.ssm.persistence.objects.Enumeration;
import com.securet.ssm.persistence.objects.MailTemplate;
import com.securet.ssm.persistence.objects.ServiceType;
import com.securet.ssm.persistence.objects.Ticket;
import com.securet.ssm.persistence.objects.TicketArchive;
import com.securet.ssm.persistence.objects.TicketAttachment;
import com.securet.ssm.persistence.objects.User;
import com.securet.ssm.persistence.objects.VendorServiceAsset;
import com.securet.ssm.services.ActionHelpers;
import com.securet.ssm.services.SecureTService;
import com.securet.ssm.services.vo.DataTableCriteria;
import com.securet.ssm.services.vo.ListObjects;
import com.securet.ssm.utils.SecureTUtils;

public class BaseTicketService extends SecureTService{

	
	private static final Logger _logger = LoggerFactory.getLogger(BaseTicketService.class); 
	public static final String WORK_IN_PROGRESS_DESC = "Work in progress";
	public static final int MAX_SHORT_DESC = 80;
	public static final List<String> TICKET_STATUS = new ArrayList<String>();
	static{
		TICKET_STATUS.add("OPEN");
		TICKET_STATUS.add("WORK_IN_PROGRESS");
		TICKET_STATUS.add("RESOLVED");
		TICKET_STATUS.add("CLOSED");
	}

	// should make a query planner, to many queries - TODO - data table query planner for custom queries 
	public static final String CLIENT_USER_TICKET_NATIVE_QUERY = "SELECT t.* from ticket t INNER JOIN client_user_site cus ON t.siteId=cus.siteId WHERE cus.userId=(?1)";
	public static final String VENDOR_SITE_TICKET_NATIVE_QUERY = "SELECT t.* from ticket t INNER JOIN vendor_service_asset vsa ON t.assetId=vsa.assetId AND t.serviceTypeId=vsa.serviceTypeId WHERE t.ticketType!='LOG' AND vsa.userId=(?1)";

	public static final String TICKET_NATIVE_QUERY = "SELECT t.* from ticket t ";
	private static final String TICKET_SIMPLE_FIELDS_NATIVE_QUERY = "SELECT t.ticketId,t.statusId,t.siteId,s.name site,t.serviceTypeId, st.name serviceType, t.createdTimestamp  from ticket t ";
	public static final String TICKET_COUNT_NATIVE_QUERY = "SELECT COUNT(DISTINCT t.ticketId) from ticket t ";
	
	public static final String SERVICE_TYPE_JOIN_CLAUSE = " INNER JOIN service_type st ON t.serviceTypeId=st.serviceTypeId";
	public static final String SITE_JOIN_CLAUSE = " INNER JOIN site s ON t.siteId=s.siteId ";
	public static final String ISSUE_TYPE_JOIN_CLAUSE = " LEFT JOIN issue_type it ON t.issueTypeId=it.issueTypeId ";
	
	//client queries
	public static final String CLIENT_USER_SITE_JOIN_CLAUSE = " INNER JOIN client_user_site cus ON t.siteId=cus.siteId ";
	public static final String CLIENT_USER_FILTER = " cus.userId=(?1) ";
	
	//vendor queries
	public static final String VENDOR_SERVICE_ASSET_JOIN_CLAUSE = " INNER JOIN vendor_service_asset vsa ON t.assetId=vsa.assetId AND t.serviceTypeId=vsa.serviceTypeId ";
	public static final String VENDOR_USER_FILTER = " t.ticketType!='LOG' AND vsa.userId=(?1) ";
	
	//Common Join and Filters
	public static final String TICKET_STATUS_FILTER = " t.statusId IN (?2) ";
	public static final String TICKET_COMMON_FILTER_CLAUSE=" (t.statusId like (?3) OR t.Description like (?4) OR  st.name like (?5) OR it.name like (?6) OR t.ticketType like (?7)  OR t.ticketId like (?8)) ";
	public static final String TICKET_DEFAULT_SORT_BY = " ORDER BY t.lastUpdatedTimestamp desc";
	public static final String TICKET_GROUP_BY = " GROUP BY t.ticketId ";
	public static final int TICKET_COMMON_FILTER_PARAMS_SIZE=5;  
	
	//Queries to show navigation filters 
	public static final String COUNT_CLIENT_USER_TICKET_NATIVE_QUERY = "SELECT COUNT(DISTINCT t.ticketId) from ticket t INNER JOIN client_user_site cus ON t.siteId=cus.siteId WHERE cus.userId=(?1)";
	public static final String COUNT_VENDOR_SITE_TICKET_NATIVE_QUERY = "SELECT COUNT(DISTINCT t.ticketId) from ticket t INNER JOIN vendor_service_asset vsa ON t.assetId=vsa.assetId AND t.serviceTypeId=vsa.serviceTypeId WHERE t.ticketType!='LOG' AND vsa.userId=(?1)";
	
	public static final String EMAIL_CREATE_TICKET_NOTIFICATION = "EMAIL_CREATE_TICKET_NOTIFICATION";
	public static final String SMS_CREATE_TICKET_NOTIFICATION = "SMS_CREATE_TICKET_NOTIFICATION";

	public static final String EMAIL_UPDATE_TICKET_NOTIFICATION = "EMAIL_UPDATE_TICKET_NOTIFICATION";
	public static final String SMS_UPDATE_TICKET_NOTIFICATION = "SMS_UPDATE_TICKET_NOTIFICATION";

	public static final String LOG = "LOG";
	public static final String COMPLAINT = "COMPLAINT";

	public static final String OPEN = "OPEN";
	public static final String WORK_IN_PROGRESS = "WORK_IN_PROGRESS";
	public static final String RESOLVED = "RESOLVED";
	public static final String CLOSED = "CLOSED";
	
	public static final String ALL_OK = "ALL OK";
	public static final String TICKET_PREFIX = "C";
	private static final int MAX_PARAM_COUNT = 8;

	public Ticket getUserTicket(String ticketId, org.springframework.security.core.userdetails.User customUser, MailService mailService, SMSService smsService) {
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
				entityManager.detach(currentTicket);
				Enumeration status = new Enumeration();
				status.setEnumerationId(WORK_IN_PROGRESS);
				currentTicket.setStatus(status);
				currentTicket.setDescription(WORK_IN_PROGRESS_DESC);
				currentTicket = updateTicketAndNotify(currentTicket, null, customUser, mailService,smsService);
			}
			_logger.debug("no of attachments : "+currentTicket.getAttachments().size());
		}
		return currentTicket;
	}

	public List<Ticket> getUserTickets(org.springframework.security.core.userdetails.User customUser,String query, String ticketId){
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
	
	public ListObjects listUserTickets(DataTableCriteria columns, String filterStatus, org.springframework.security.core.userdetails.User customUser,boolean allFields) {
		boolean isReporter = false;
		String textToSearch = DataTableCriteria.getDefaultTextToSearch(columns);
		StringBuilder ticketQueryStr = new StringBuilder();
		//ticketQueryStr.append(TICKET_NATIVE_QUERY);
		ticketQueryStr.append(SERVICE_TYPE_JOIN_CLAUSE);
		ticketQueryStr.append(SITE_JOIN_CLAUSE);
		String userFilterQuery = "";
		if(customUser!=null){
			isReporter = !customUser.getAuthorities().contains(SecureTAuthenticationSuccessHandler.resolverAuthority);
			if(isReporter){
				ticketQueryStr.append(CLIENT_USER_SITE_JOIN_CLAUSE);
				userFilterQuery=CLIENT_USER_FILTER;
			}else{
				ticketQueryStr.append(VENDOR_SERVICE_ASSET_JOIN_CLAUSE);
				userFilterQuery=VENDOR_USER_FILTER;
			}
		}
		ticketQueryStr.append(ISSUE_TYPE_JOIN_CLAUSE).append(DataTableCriteria.WHERE);
		ticketQueryStr.append(DataTableCriteria.START_BRACKET).append(TICKET_STATUS_FILTER);
		if(!userFilterQuery.isEmpty()){
			ticketQueryStr.append(DataTableCriteria.AND).append(DataTableCriteria.SPACE).append(userFilterQuery);
		}
		ticketQueryStr.append(DataTableCriteria.END_BRACKET);
		
		boolean textSearchEnabled = textToSearch!=null && !textToSearch.isEmpty();
		if(textSearchEnabled){
			ticketQueryStr.append(DataTableCriteria.SPACE).append(DataTableCriteria.AND).append(DataTableCriteria.SPACE);
			ticketQueryStr.append(TICKET_COMMON_FILTER_CLAUSE);
		}
		
		StringBuilder ticketListQueryStr = new StringBuilder();

		if(allFields){
			ticketListQueryStr.append(TICKET_NATIVE_QUERY);
		}else{
			ticketListQueryStr.append(TICKET_SIMPLE_FIELDS_NATIVE_QUERY);
		}
		ticketListQueryStr.append(ticketQueryStr.toString());
		ticketListQueryStr.append(TICKET_GROUP_BY);
		ticketListQueryStr.append(TICKET_DEFAULT_SORT_BY);
		
		Query ticketListQuery = null;
		if(allFields){
			ticketListQuery = entityManager.createNativeQuery(ticketListQueryStr.toString(), Ticket.class);
		}else if(customUser!=null){
			if(isReporter){
				if(textSearchEnabled){
					ticketListQuery = entityManager.createNamedQuery("getFilteredClientUserTickets");
				}else{
					ticketListQuery = entityManager.createNamedQuery("getClientUserTickets");
				}
			}else{
				if(textSearchEnabled){
					ticketListQuery = entityManager.createNamedQuery("getFilteredVendorUserTickets");
				}else{
					ticketListQuery = entityManager.createNamedQuery("getVendorUserTickets");
				}
			}
		}
		StringBuilder ticketCountQueryStr = new StringBuilder();
		ticketCountQueryStr.append(TICKET_COUNT_NATIVE_QUERY).append(ticketQueryStr.toString());
		Query ticketCountQuery = entityManager.createNativeQuery(ticketCountQueryStr.toString());
		if(customUser!=null){
			ticketListQuery.setParameter(1, customUser.getUsername());
			ticketCountQuery.setParameter(1, customUser.getUsername());
		}
		if(filterStatus!=null && !filterStatus.isEmpty()){
			ticketListQuery.setParameter(2, filterStatus);
			ticketCountQuery.setParameter(2, filterStatus);
		}else{
			ticketListQuery.setParameter(2, TICKET_STATUS);
			ticketCountQuery.setParameter(2, TICKET_STATUS);
		}
			
		//int paramCount = ticketListQuery.getParameters().size();
		if(textSearchEnabled){
			textToSearch = DataTableCriteria.PERCENTILE+textToSearch+DataTableCriteria.PERCENTILE;
			for(int i=3;i<=MAX_PARAM_COUNT;i++){
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

	public void createTicketAndNotify(Ticket formObject, List<MultipartFile> ticketAttachments, org.springframework.security.core.userdetails.User customUser, MailService mailService, SMSService smsService) {
		createTicket(formObject,customUser.getUsername());
		saveAttachments(formObject,ticketAttachments,true);
		formObject = manageTicketPeristence(formObject);
		if(!isLog(formObject)){
			Map<String,Object> bodyParameters = new HashMap<String, Object>();
			bodyParameters.put("ticket", formObject);
			sendNotifications(mailService,smsService, EMAIL_CREATE_TICKET_NOTIFICATION, SMS_CREATE_TICKET_NOTIFICATION,bodyParameters);
		}
	}

	private Ticket manageTicketPeristence(Ticket formObject) {
		entityManager.flush();
		entityManager.clear();
		formObject = entityManager.find(Ticket.class, formObject.getTicketId());
		entityManager.detach(formObject);
		return formObject;
	}
	
	public void createTicket(Ticket formObject, String reporterUserId) {
			//get a new sequence, ALL tickets should be prefixed with C
			long ticketSequenceId = SequenceGeneratorHelper.getNextSequence("Ticket",entityManager);
			String ticketId = TICKET_PREFIX+ticketSequenceId;
			if(_logger.isDebugEnabled())_logger.debug("Ticket Id generated as "+ticketId);
			formObject.setTicketId(ticketId);
			formObject.setTicketMasterId(ticketId);
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
				formObject.setSeverity(null);
			}else{
				status.setEnumerationId(OPEN);
			}
			formObject.setStatus(status);
			entityManager.persist(formObject);
	}

	public void setShortDescription(Ticket formObject) {
		String description = formObject.getDescription();
		if(description.length()<MAX_SHORT_DESC){
			formObject.setShortDesc(description);
		}else{
			formObject.setShortDesc(description.substring(0, MAX_SHORT_DESC));	
		}
	}

	public boolean isLog(Ticket formObject) {
		return formObject.getTicketType().getEnumerationId().equals(LOG);
	}

	public Ticket updateTicketAndNotify(Ticket formObject, List<MultipartFile> ticketAttachments, org.springframework.security.core.userdetails.User customUser, MailService mailService, SMSService smsService) {
		Ticket receivedTicket = formObject;
		//check previous status from history
		Query statusQuery = entityManager.createNamedQuery("getTicketStatusForId");
		statusQuery.setParameter("ticketId", formObject.getTicketId());
		String previousStatus = (String)statusQuery.getSingleResult();
		
		formObject = updateTicket(formObject, formObject.getStatus().getEnumerationId(), formObject.getDescription(), customUser.getUsername());
		if(ticketAttachments!=null && ticketAttachments.size()>0){
			saveAttachments(formObject, ticketAttachments,false);
		}
		receivedTicket.setAttachments(formObject.getAttachments());
		formObject = manageTicketPeristence(formObject);
		if(!isLog(formObject)){
			Map<String,Object> bodyParameters = new HashMap<String, Object>();
			bodyParameters.put("ticket", formObject);
			bodyParameters.put("previousStatus", previousStatus);
			sendNotifications(mailService,smsService, EMAIL_UPDATE_TICKET_NOTIFICATION, SMS_UPDATE_TICKET_NOTIFICATION,bodyParameters);
		}
		return formObject;
	}

	public Ticket updateTicket(Ticket ticket, String newStatus, String description,String modifiedUser){
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
	
	public ServiceType setTicketType(Ticket formObject) {
		//if service Type text is ALL OK - then it is a LOG, otherwise every ticket will default to COMPLAINT
		ServiceType serviceType = entityManager.find(ServiceType.class, formObject.getServiceType().getServiceTypeId());
		if(serviceType!=null && serviceType.getName().equals(ALL_OK)){
			//this is a log
			setTicketType(formObject, LOG);
			//also close the ticket
			
		}else{
			setTicketType(formObject, COMPLAINT);
		}
		return serviceType;
	}

	public void setTicketType(Ticket formObject, String enumerationId) {
		Enumeration ticketType = new Enumeration();
		ticketType.setEnumerationId(enumerationId);
		formObject.setTicketType(ticketType);
	}
	
	public void saveAttachments(Ticket formObject, List<MultipartFile> ticketAttachments,boolean refresh) {
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

	public void sendNotifications(MailService mailService,SMSService smsService,String emailTemplate, String smsTemplate,Map<String,Object> bodyParameters) {
		sendEmail(mailService,bodyParameters,emailTemplate);
		sendSMS(smsService,bodyParameters,smsTemplate);
	}

	public void sendSMS(SMSService smsService, Map<String,Object> bodyParameters,String templateName) {
		Ticket formObject = (Ticket)bodyParameters.get("ticket");
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
			smsContext.put("bodyParameters", bodyParameters);
			smsContext.put("template",mailTemplate.getTemplateFileName());
			for(String contactNumber : receiverContacts){
				smsContext.put("contactNumber", contactNumber);
				try {
					smsService.sendSMS(smsContext);
				} catch (UnsupportedEncodingException e) {
					_logger.error("Could not send sms for the context:" + smsContext,e);
				}catch (Exception e) {
					_logger.error("Could not send sms for the context:" + smsContext,e);
				}
			}
		}
	}

	public void sendEmail(MailService mailService,Map<String,Object> bodyParameters,String templateName) {
		//get the user email... this can run in background..
		Ticket formObject = (Ticket)bodyParameters.get("ticket");
		Map<String,Object> mailContext = new HashMap<String,Object>();
		StringBuilder toAddress = new StringBuilder();
		switch (formObject.getStatus().getEnumerationId()) {
		case "OPEN":
			if(formObject.getReporter().getEmailId()!=null){
				toAddress.append(formObject.getReporter().getEmailId());
			}
			if(formObject.getResolver()!=null && formObject.getResolver().getEmailId()!=null){
				toAddress.append(",").append(formObject.getResolver().getEmailId());
			}else{
				_logger.error("No vendor assignment something went wrong: "+formObject.getTicketId());
			}
			break;
		case "CLOSED":
			if(formObject.getResolver()!=null && formObject.getResolver().getEmailId()!=null){
				toAddress.append(formObject.getResolver().getEmailId());
			}else{
				_logger.error("No vendor assignment something went wrong: "+formObject.getTicketId());
			}
			break;
		case "WORK_IN_PROGRESS":
		case "RESOLVED":
			if(formObject.getReporter().getEmailId()!=null){
				toAddress.append(formObject.getReporter().getEmailId());
			}
			break;
		default:
			break;
		}
		if(toAddress.length()<=0){
			_logger.error("No email address found.. so not notifying for ticket: "+ formObject.getTicketId());
			return;
		}
		Query mailTemplateQuery = entityManager.createNamedQuery("getMailTemplateByName");
		mailTemplateQuery.setParameter("templateName", templateName);
		MailTemplate mailTemplate = (MailTemplate)mailTemplateQuery.getSingleResult();
		mailContext.put("to",toAddress.toString());
		mailContext.put("bodyParameters", bodyParameters);
		mailContext.put("contentType",mailTemplate.getContentType());
		mailContext.put("from",mailTemplate.getFrom());
		mailContext.put("subject",mailTemplate.getSubject());
		mailContext.put("template",mailTemplate.getTemplateFileName());
		
		try{
			mailService.sendMail(mailContext);
		}catch(MailSendException e){
			_logger.error("Could not send email for ticket :"+formObject.getTicketId() +" check the stack trace", e);
		}catch (Exception e) {
			_logger.error("Could not send email for ticket :"+formObject.getTicketId() +" check the stack trace", e);
		}
	}

	public static void fetchTicketStats(EntityManager entityManager, org.springframework.security.core.userdetails.User customUser, Map map) {
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

	public Map<String, Object> loadVendorsAndIssueTypes(int serviceTypeId, int siteId) {
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

	public void validateAndSetDefaultsForTicket(String objectName,Ticket formObject,BindingResult result) {
		if(result.getFieldErrorCount("site.siteId")==0 && formObject.getSite().getSiteId()==0){
			FieldError fieldError = new FieldError(objectName, "site.siteId", "Please Select a Site");
			result.addError(fieldError);
		}
		if(formObject.getServiceType()==null || formObject.getServiceType().getServiceTypeId()==0){
			FieldError fieldError = new FieldError(objectName, "serviceType.serviceTypeId", "Please Select a Service Type");
			result.addError(fieldError);
		}else{
			ServiceType serviceType = setTicketType(formObject);
			if(serviceType==null){
				FieldError fieldError = new FieldError(objectName, "serviceType.serviceTypeId", "Service Type does not exist");
				result.addError(fieldError);
			}else if(!result.hasErrors() & !isLog(formObject)){//if it not a log do not allow 
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
				}else{
					FieldError fieldError = new FieldError(objectName, "serviceType.serviceTypeId", "No Vendor Assigned");
					result.addError(fieldError);
				}

			}
			formObject.setServiceType(serviceType);
		}
		
		if(!result.hasErrors() && !isLog(formObject)){
			if(formObject.getIssueType()==null || formObject.getIssueType().getIssueTypeId()==0){
				FieldError fieldError = new FieldError(objectName, "issueType.issueTypeId", "Please Select a Issue Type");
				result.addError(fieldError);
			}
			if(formObject.getSeverity()==null || formObject.getSeverity().getEnumerationId()==null || formObject.getSeverity().getEnumerationId().isEmpty()){
				FieldError fieldError = new FieldError(objectName, "severity.enumerationId", "Please Select Severity");
				result.addError(fieldError);
			}
		}
	}
}
