package com.securet.ssm.services.ticket;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.sql.JPASQLQuery;
import com.mysema.query.sql.DatePart;
import com.mysema.query.sql.SQLExpressions;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import com.mysema.query.types.QMap;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.CaseBuilder;
import com.mysema.query.types.expr.Coalesce;
import com.mysema.query.types.expr.DateTimeExpression;
import com.mysema.query.types.expr.DateTimeOperation;
import com.mysema.query.types.expr.NumberExpression;
import com.securet.ssm.components.authentication.SecureTAuthenticationSuccessHandler;
import com.securet.ssm.components.mail.MailService;
import com.securet.ssm.components.sms.SMSService;
import com.securet.ssm.persistence.SequenceGeneratorHelper;
import com.securet.ssm.persistence.objects.Asset;
import com.securet.ssm.persistence.objects.Enumeration;
import com.securet.ssm.persistence.objects.IssueType;
import com.securet.ssm.persistence.objects.MailTemplate;
import com.securet.ssm.persistence.objects.PartOrderRequest;
import com.securet.ssm.persistence.objects.SecureTObject;
import com.securet.ssm.persistence.objects.ServiceType;
import com.securet.ssm.persistence.objects.Site;
import com.securet.ssm.persistence.objects.Ticket;
import com.securet.ssm.persistence.objects.TicketArchive;
import com.securet.ssm.persistence.objects.TicketAttachment;
import com.securet.ssm.persistence.objects.User;
import com.securet.ssm.persistence.objects.VendorServiceAsset;
import com.securet.ssm.persistence.objects.querydsl.jpa.JPAPartOrderRequest;
import com.securet.ssm.persistence.objects.querydsl.jpa.JPAServiceSparePart;
import com.securet.ssm.persistence.objects.querydsl.jpa.JPATicket;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLClientUserSite;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLEnumeration;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLIssueType;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLModule;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLOrganization;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLServiceType;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLSite;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLTicket;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLTicketArchive;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLUser;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLVendorServiceAsset;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLPartOrderRequest;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLAsset;
import com.securet.ssm.persistence.views.SimplePartOrderRequest;
import com.securet.ssm.persistence.views.SimpleServiceSparePart;
import com.securet.ssm.persistence.views.SimpleSite;
import com.securet.ssm.persistence.views.SimpleTicket;
import com.securet.ssm.persistence.views.SimpleUser;
import com.securet.ssm.services.ActionHelpers;
import com.securet.ssm.services.DefaultService;
import com.securet.ssm.services.SecureTService;
import com.securet.ssm.services.admin.AdminService;
import com.securet.ssm.services.vo.DataTableCriteria;
import com.securet.ssm.services.vo.HPToolInput;
import com.securet.ssm.services.vo.ListObjects;
import com.securet.ssm.services.vo.TicketFilter;
import com.securet.ssm.utils.SecureTUtils;

public class BaseTicketService extends SecureTService{

	
	private static final Logger _logger = LoggerFactory.getLogger(BaseTicketService.class); 
	public static final String WORK_IN_PROGRESS_DESC = "Work in progress";
	public static final int MAX_SHORT_DESC = 80;
	public static final List<String> TICKET_STATUS = new ArrayList<String>();
	private static final Map<String,String> PO_STATUS_MAPPING = new HashMap<String, String>();
	
	public static final Map<String,Expression> fieldExprMapping=new HashMap<String, Expression>();

	public static final String MDDYYYY_HHMMSS_A = "M/d/yyyy hh:mm:ss aaa";

	static{
		TICKET_STATUS.add("OPEN");
		TICKET_STATUS.add("WORK_IN_PROGRESS");
		TICKET_STATUS.add("RESOLVED");
		TICKET_STATUS.add("CLOSED");

		PO_STATUS_MAPPING.put("Initiated", "PO_INITIATED");
		PO_STATUS_MAPPING.put("Authorize", "PO_AUTHORIZED");
		PO_STATUS_MAPPING.put("Reject", "PO_REJECTED");
		PO_STATUS_MAPPING.put("Complete", "PO_COMPLETED");

		fieldExprMapping.put("ticketId",SQLTicket.ticket.ticketId);
		fieldExprMapping.put("shortDesc",SQLTicket.ticket.shortDesc);
		fieldExprMapping.put("statusId",SQLTicket.ticket.statusId);
		fieldExprMapping.put("ticketType",SQLTicket.ticket.ticketType);
		fieldExprMapping.put("site.name",SQLSite.site.name);
		fieldExprMapping.put("site.circle",SQLSite.site.circle);
		fieldExprMapping.put("serviceType.name",SQLServiceType.serviceType.name);
		fieldExprMapping.put("issueType.name",SQLIssueType.issueType.name);
		fieldExprMapping.put("clientUser.userId",SQLClientUserSite.clientUserSite.userId);
		fieldExprMapping.put("vendorUser.userId",SQLVendorServiceAsset.vendorServiceAsset.userId);
		fieldExprMapping.put("vendorUser.organizationName",SQLOrganization.organization.name);
		fieldExprMapping.put("createdTimestamp",SQLTicket.ticket.createdTimestamp);
		fieldExprMapping.put("lastUpdatedTimestamp",SQLTicket.ticket.lastUpdatedTimestamp);
	
	}

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

	SQLTicket sqlTicket = SQLTicket.ticket;
	SQLServiceType sqlServiceType = SQLServiceType.serviceType;
	SQLSite sqlSite = SQLSite.site;
	SQLIssueType sqlIssueType = SQLIssueType.issueType;
	SQLClientUserSite sqlClientUserSite = SQLClientUserSite.clientUserSite;
	SQLVendorServiceAsset sqlVendorServiceAsset = SQLVendorServiceAsset.vendorServiceAsset;
	SQLUser sqlVendorUser = SQLUser.user;
	SQLModule sqlModule = SQLModule.module;
	SQLOrganization sqlVendorOrganization = SQLOrganization.organization;
	SQLTicketArchive sqlTicketArchiveResolved = new SQLTicketArchive("tar");
	SQLTicketArchive sqlTicketArchiveResolvedRelated = new SQLTicketArchive("tarRelated");;
	SQLEnumeration sqlStatus=new SQLEnumeration("status");

	protected static final JPAPartOrderRequest jpaPartOrderRequest = JPAPartOrderRequest.partOrderRequest;
	protected static final JPATicket jpaTicket = JPATicket.ticket;

	// should make a query planner, to many queries - TODO - data table query planner for custom queries 
	public static final String CLIENT_USER_TICKET_NATIVE_QUERY = "SELECT t.* from ticket t INNER JOIN client_user_site cus ON t.siteId=cus.siteId WHERE cus.userId=(?1)";
	public static final String VENDOR_SITE_TICKET_NATIVE_QUERY = "SELECT t.* from ticket t INNER JOIN vendor_service_asset vsa ON t.assetId=vsa.assetId AND t.serviceTypeId=vsa.serviceTypeId WHERE t.ticketType!='LOG' AND vsa.userId=(?1)";

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

	public Ticket getUserTicket(String ticketId, org.springframework.security.core.userdetails.User customUser, MailService mailService, SMSService smsService) {
		Ticket currentTicket = null;
		//consider admin as the client as of now

		boolean isResolver = customUser.getAuthorities().contains(SecureTAuthenticationSuccessHandler.resolverAuthority);
		Ticket userTicket =  getUserTickets(customUser,ticketId);
		//if tickets found, the user should be allowed to view, else.. show him his tickets list - error message??
		if(userTicket!=null){
			currentTicket = userTicket;
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

	public Ticket getUserTickets(org.springframework.security.core.userdetails.User customUser, String ticketId){
		boolean isResolver = customUser.getAuthorities().contains(SecureTAuthenticationSuccessHandler.resolverAuthority);
		JPASQLQuery ticketQuery = new JPASQLQuery(entityManager, sqlTemplates);
		//"SELECT t.* from ticket t INNER JOIN client_user_site cus ON t.siteId=cus.siteId WHERE cus.userId=(?1)";
		//"SELECT t.* from ticket t INNER JOIN vendor_service_asset vsa ON t.assetId=vsa.assetId AND t.serviceTypeId=vsa.serviceTypeId WHERE t.ticketType!='LOG' AND vsa.userId=(?1)";
		ticketQuery.from(sqlTicket);
		BooleanExpression whereExpression = sqlTicket.ticketId.eq(ticketId);
		innerJoinClientUserSiteForTicket(ticketQuery);
		if(isResolver){
			//find the logged in user, assigned service types and sites 
			innerJoinVendorUserAssetForTicket(ticketQuery);
			ticketQuery.where(vendorTicketFilterExpr(customUser));
		}else{
			//consider admin as the client as of now
			ticketQuery.where(clientUserTicketFilter(customUser));
		}
		leftJoinTicketArchiveForTAT(ticketQuery, sqlTicket, sqlTicketArchiveResolved, sqlTicketArchiveResolvedRelated);

		NumberExpression<Integer> tatExpr = ticketTATExpr(sqlTicket);
		NumberExpression<Integer> stopClockExpr = ticketStopClockExpr(sqlTicketArchiveResolved, sqlTicketArchiveResolvedRelated, sqlTicket, sqlClientUserSite);
		
		NumberExpression<Integer> actualTATExpr = tatExpr.subtract(stopClockExpr).as("actualTat");
		stopClockExpr = stopClockExpr.as("stopClock");

		QMap jpaTicketBeanExpr = Projections.map(jpaTicket, tatExpr,actualTATExpr,stopClockExpr);
		ticketQuery.where(whereExpression);
		Map<Expression<?>, ?> result = ticketQuery.uniqueResult(jpaTicketBeanExpr);
		Ticket ticket = (Ticket) result.get(jpaTicket);
		if(ticket!=null){
			if(result.get(tatExpr)!=null){
				ticket.setTat((Integer) result.get(tatExpr));
				ticket.setActualTat((Integer) result.get(actualTATExpr));
				ticket.setStopClock((Integer) result.get(stopClockExpr));
			}
		}
		return ticket;
	}

	protected String editTicketDetails(String ticketId, org.springframework.security.core.userdetails.User customUser, Model model) {
		Ticket currentTicket = getUserTicket(ticketId, customUser,getMailService(),getSmsService());
		if(currentTicket==null){
			return listTicketsForUser(customUser,null,model);
		}
		return loadEditTicketModel(model, currentTicket);
	}


	protected String loadEditTicketModel(Model model, Ticket currentTicket) {
		//also load the archive...
		List<SecureTObject> ticketArchives = fetchQueriedObjects("getLatestTicketArchivesForTicketId", "ticketId", currentTicket.getTicketId());
		model.addAttribute("ticketArchives", ticketArchives);// pagination???

		//also load any partOrderRequest
		List<SimplePartOrderRequest> simplePartOrderRequests = fetchPORRequestsForTicket(currentTicket);

		model.addAttribute("partOrderRequests",simplePartOrderRequests);
		
		model.addAttribute("formObject",currentTicket);
		return DefaultService.TICKET+"modifyTicket";
	}

	protected List<SimplePartOrderRequest> fetchPORRequestsForTicket(Ticket currentTicket) {
		JPAQuery partOrderRequestQuery = new JPAQuery(entityManager);
		partOrderRequestQuery.from(jpaPartOrderRequest).where(jpaPartOrderRequest.ticket.eq(currentTicket)).orderBy(jpaPartOrderRequest.lastUpdatedTimestamp.desc());
		QBean<SimplePartOrderRequest> simplePartOrderRequestFields = poRequestFields();
		List<SimplePartOrderRequest> simplePartOrderRequests =  partOrderRequestQuery.list(simplePartOrderRequestFields);
		return simplePartOrderRequests;
	}

	protected QBean<SimplePartOrderRequest> poRequestFields() {
		JPAServiceSparePart jpaPOServiceSparePart = jpaPartOrderRequest.serviceSparePart;
		QBean<SimpleServiceSparePart> simpleServiceSparePartBean = Projections.bean(SimpleServiceSparePart.class, jpaPOServiceSparePart.sparePartId
				,jpaPOServiceSparePart.partName,jpaPOServiceSparePart.partDescription);

		QBean<SimpleUser> initiatedByBean = Projections.bean(SimpleUser.class, jpaPartOrderRequest.initiatedBy.userId.as("userId"));
		QBean<SimpleUser> respondedByBean = Projections.bean(SimpleUser.class, jpaPartOrderRequest.respondedBy.userId.as("userId"));
		
		return Projections.bean(SimplePartOrderRequest.class, simpleServiceSparePartBean.as("serviceSparePart"),initiatedByBean.as("initiatedBy"),
				respondedByBean.as("respondedBy"),jpaPartOrderRequest.partOrderRequestId,jpaPartOrderRequest.status.enumDescription.as("statusId"), 
				jpaPartOrderRequest.cost,jpaPartOrderRequest.ticket.ticketId.as("ticketId"), jpaPartOrderRequest.createdTimestamp,jpaPartOrderRequest.lastUpdatedTimestamp);
	}

	protected String listTicketsForUser(org.springframework.security.core.userdetails.User customUser, String filterStatus, Model model) {
		boolean isReporter = !customUser.getAuthorities().contains(SecureTAuthenticationSuccessHandler.resolverAuthority);
		model.addAttribute("isReporter", isReporter);
		model.addAttribute("userName", customUser.getUsername());
		if(filterStatus!=null && !filterStatus.isEmpty()){
			model.addAttribute("filterStatus", filterStatus);
		}
		return DefaultService.TICKET+"listTickets";
	}

	public ListObjects listUserTicketsDSL(TicketFilter ticketFilter, org.springframework.security.core.userdetails.User customUser) {

		JPASQLQuery listTicketsQuery = simpleTicketQueryByFilter(customUser, ticketFilter);
		JPASQLQuery countTicketsQuery  = simpleTicketQueryByFilter(customUser, ticketFilter,false);
		
		
		QBean<SimpleTicket> resultListExpression = simpleTicketBeanExpression();
		Map<String,JPASQLQuery> jpaQueriesToRun = new HashMap<String, JPASQLQuery>();
		listTicketsQuery.groupBy(sqlTicket.ticketId);

		addTicketFilterOrderByExpression(ticketFilter, listTicketsQuery);

		int startBeforeFilter = ticketFilter.getStart();
		//when we need to sort on actualTat - we do not need to run the optimal query
		if(!DataTableCriteria.hasOrderByField(ticketFilter, "actualTat")){
			filterBySpecificTicketsWithFilter(listTicketsQuery,ticketFilter,customUser,null);
		}
		
		jpaQueriesToRun.put(DataTableCriteria.DATA_QUERY, listTicketsQuery);
		
		jpaQueriesToRun.put(DataTableCriteria.COUNT_QUERY, countTicketsQuery);

		if(_logger.isDebugEnabled())_logger.debug("quries to run "+jpaQueriesToRun);
		ListObjects tickets =  ActionHelpers.listSimpleObjectFromQueryDSL(ticketFilter, jpaQueriesToRun,resultListExpression,SQLTicket.ticket.ticketId.countDistinct());
		//set the start back after the response.. so the UI can send correct index
		ticketFilter.setStart(startBeforeFilter);
		return tickets;
	}

	private void addTicketFilterOrderByExpression(DataTableCriteria ticketFilter, JPASQLQuery listTicketsQuery) {
		if(ticketFilter.getOrder()!=null && ticketFilter.getOrder().size()>0){
			ticketFilter.makeOrderByExpression(ticketFilter, listTicketsQuery,fieldExprMapping);
    	}else{
    		listTicketsQuery.orderBy(sqlTicket.lastUpdatedTimestamp.desc());
    	}
	}

	private void filterBySpecificTickets(JPASQLQuery listTicketsQuery,DataTableCriteria ticketFilter, org.springframework.security.core.userdetails.User customUser, String filterStatus) {
		//seeing that the TAT can be calculated only on the tickets to sent in response... 
		//so first fetch the ticketId to sent in response and then make the final expression with TAT
		JPASQLQuery ticketIdsToFilter =  simpleTicketQuery(customUser, ticketFilter,filterStatus,false);
		ActionHelpers.setQueryLimitOptions(ticketFilter, ticketIdsToFilter);
		addTicketFilterOrderByExpression(ticketFilter, ticketIdsToFilter);
		List<String> ticketIds = ticketIdsToFilter.groupBy(SQLTicket.ticket.ticketId).list(SQLTicket.ticket.ticketId);
		if(SecureTUtils.isNotEmpty(ticketIds)){
			//add the filter to list query.. to filter
			listTicketsQuery.where(SQLTicket.ticket.ticketId.in(ticketIds));
			//now reset the column start as we will fetch only relevant tickets 
			ticketFilter.setStart(0);
		}
	}
	
	private void filterBySpecificTicketsWithFilter(JPASQLQuery listTicketsQuery,TicketFilter ticketFilter, org.springframework.security.core.userdetails.User customUser, String filterStatus) {
		//seeing that the TAT can be calculated only on the tickets to sent in response... 
		//so first fetch the ticketId to sent in response and then make the final expression with TAT
		JPASQLQuery ticketIdsToFilter =  simpleTicketQueryByFilter(customUser, ticketFilter,false);
		ActionHelpers.setQueryLimitOptions(ticketFilter, ticketIdsToFilter);
		addTicketFilterOrderByExpression(ticketFilter, ticketIdsToFilter);
		List<String> ticketIds = ticketIdsToFilter.list(SQLTicket.ticket.ticketId);
		if(SecureTUtils.isNotEmpty(ticketIds)){
			//add the filter to list query.. to filter
			listTicketsQuery.where(SQLTicket.ticket.ticketId.in(ticketIds));
			//now reset the column start as we will fetch only relevant tickets 
			ticketFilter.setStart(0);
		}
	}
	public ListObjects listUserTicketsDSL(DataTableCriteria columns, String filterStatus, org.springframework.security.core.userdetails.User customUser) {

		JPASQLQuery listTicketsQuery = simpleTicketQuery(customUser, columns,filterStatus);
		JPASQLQuery countTicketsQuery  = simpleTicketQuery(customUser, columns,filterStatus,false);
		
		QBean<SimpleTicket> resultListExpression = simpleTicketBeanExpression();
		Map<String,JPASQLQuery> jpaQueriesToRun = new HashMap<String, JPASQLQuery>();
		listTicketsQuery.groupBy(sqlTicket.ticketId);

		addTicketFilterOrderByExpression(columns, listTicketsQuery);
		int startBeforeFilter = columns.getStart();
		//when we need to sort on actualTat - we do not need to run the optimal query
		if(!DataTableCriteria.hasOrderByField(columns, "actualTat")){
			filterBySpecificTickets(listTicketsQuery,columns,customUser,filterStatus);
		}

		jpaQueriesToRun.put(DataTableCriteria.DATA_QUERY, listTicketsQuery);
		
		jpaQueriesToRun.put(DataTableCriteria.COUNT_QUERY, countTicketsQuery);

		if(_logger.isDebugEnabled())_logger.debug("quries to run "+jpaQueriesToRun);
		ListObjects tickets =  ActionHelpers.listSimpleObjectFromQueryDSL(columns, jpaQueriesToRun,resultListExpression,SQLTicket.ticket.ticketId.countDistinct());
		//set the start back after the response.. so the UI can send correct index
		columns.setStart(startBeforeFilter);
		return tickets;
	}


	private JPASQLQuery simpleTicketQuery(org.springframework.security.core.userdetails.User customUser, DataTableCriteria columns, String filterStatus) {
		return simpleTicketQuery(customUser, columns, filterStatus, true);
	}

	private JPASQLQuery simpleTicketQuery(org.springframework.security.core.userdetails.User customUser, DataTableCriteria columns, String filterStatus,boolean includeTAT) {
		String textToSearch = DataTableCriteria.getDefaultTextToSearch(columns);

		
		JPASQLQuery  jpaSQLQuery = new JPASQLQuery(entityManager,sqlTemplates);
		jpaSQLQuery.from(sqlTicket)
		//service type and site join
		.innerJoin(sqlServiceType).on(sqlTicket.serviceTypeId.eq(sqlServiceType.serviceTypeId))
		.innerJoin(sqlSite).on(sqlTicket.siteId.eq(sqlSite.siteId));
		//client and vendor join..
		innerJoinClientUserSiteForTicket(jpaSQLQuery)
		.innerJoin(sqlStatus).on(sqlTicket.statusId.eq(sqlStatus.enumerationId));
		
		leftJoinVendorUserAssetForTicket(jpaSQLQuery)
		.leftJoin(sqlIssueType).on(sqlTicket.issueTypeId.eq(sqlIssueType.issueTypeId));
		if(includeTAT){
			//also add the archive tables to find tat... 
			leftJoinTicketArchiveForTAT(jpaSQLQuery, sqlTicket, sqlTicketArchiveResolved, sqlTicketArchiveResolvedRelated);
		}
		BooleanExpression whereExpression = null;
		if(filterStatus!=null && !filterStatus.isEmpty()){
			whereExpression = sqlTicket.statusId.eq(filterStatus);
		}else{
			whereExpression = sqlTicket.statusId.in(TICKET_STATUS);
		}

		whereExpression = ticketUserFilterExpr(customUser, whereExpression);

		boolean textSearchEnabled = textToSearch!=null && !textToSearch.isEmpty();
		if(textSearchEnabled){
			String searchString = DataTableCriteria.PERCENTILE+textToSearch+DataTableCriteria.PERCENTILE;
			whereExpression = whereExpression.and(sqlTicket.ticketId.like(searchString).or(sqlTicket.statusId.like(searchString)).or(sqlTicket.ticketType.like(searchString))
					.or(sqlTicket.description.like(searchString)).or(sqlServiceType.name.like(searchString)).or(sqlIssueType.name.like(searchString))
					.or(sqlClientUserSite.userId.like(searchString)).or(sqlVendorUser.userId.like(searchString)).or(sqlVendorOrganization.name.like(searchString))		
			);
		}
		//whereExpression = whereExpression.and(ticketArchiveResolved.ticketArchiveId.isNull().or(ticketArchiveResolved.statusId.eq(RESOLVED)));
		jpaSQLQuery.where(whereExpression);
		

		return jpaSQLQuery;
	}

	private JPASQLQuery simpleTicketQueryByFilter(org.springframework.security.core.userdetails.User customUser, TicketFilter ticketFilter) {
		return simpleTicketQueryByFilter(customUser, ticketFilter,true);
	}

	private JPASQLQuery simpleTicketQueryByFilter(org.springframework.security.core.userdetails.User customUser, TicketFilter ticketFilter,boolean includeTAT) {
		
		JPASQLQuery  jpaSQLQuery = new JPASQLQuery(entityManager,sqlTemplates);
		jpaSQLQuery.from(sqlTicket)
		//service type and site join
		.innerJoin(sqlServiceType).on(sqlTicket.serviceTypeId.eq(sqlServiceType.serviceTypeId))
		.innerJoin(sqlSite).on(sqlTicket.siteId.eq(sqlSite.siteId));
		//client and vendor join..
		innerJoinClientUserSiteForTicket(jpaSQLQuery)
		.innerJoin(sqlStatus).on(sqlTicket.statusId.eq(sqlStatus.enumerationId));
		
		leftJoinVendorUserAssetForTicket(jpaSQLQuery)
		.leftJoin(sqlIssueType).on(sqlTicket.issueTypeId.eq(sqlIssueType.issueTypeId));
		if(includeTAT){
			//also add the archive tables to find tat... 
			leftJoinTicketArchiveForTAT(jpaSQLQuery, sqlTicket, sqlTicketArchiveResolved, sqlTicketArchiveResolvedRelated);
		}
		BooleanExpression whereExpression = null;
		if(ticketFilter.getStatusFilter()!=null && !ticketFilter.getStatusFilter().isEmpty()){
			whereExpression = sqlTicket.statusId.in(ticketFilter.getStatusFilter());
		}else{
			whereExpression = sqlTicket.statusId.in(TICKET_STATUS);
		}

		whereExpression = ticketUserFilterExpr(customUser, whereExpression);

		//whereExpression = whereExpression.and(ticketArchiveResolved.ticketArchiveId.isNull().or(ticketArchiveResolved.statusId.eq(RESOLVED)));
		jpaSQLQuery.where(whereExpression);
		

		return jpaSQLQuery;
	}

	private BooleanExpression ticketUserFilterExpr(org.springframework.security.core.userdetails.User customUser, BooleanExpression whereExpression) {
		boolean isReporter;
		if(customUser!=null){
			isReporter = !customUser.getAuthorities().contains(SecureTAuthenticationSuccessHandler.resolverAuthority);
			if(isReporter){
				//also add the where filter..
				whereExpression= whereExpression.and(clientUserTicketFilter(customUser));
			}else{
				whereExpression=whereExpression.and(vendorTicketFilterExpr(customUser));
			}
		}
		return whereExpression;
	}

	private JPASQLQuery leftJoinVendorUserAssetForTicket(JPASQLQuery jpaSQLQuery) {
		return jpaSQLQuery.leftJoin(sqlVendorServiceAsset).on(sqlTicket.assetId.eq(sqlVendorServiceAsset.assetId).and(sqlTicket.serviceTypeId.eq(sqlVendorServiceAsset.serviceTypeId)))
		.leftJoin(sqlVendorUser).on(sqlVendorServiceAsset.userId.eq(sqlVendorUser.userId))
		.leftJoin(sqlVendorOrganization).on(sqlVendorUser.organizationId.eq(sqlVendorOrganization.organizationId));
	}

	private JPASQLQuery innerJoinVendorUserAssetForTicket(JPASQLQuery jpaSQLQuery) {
		return jpaSQLQuery.innerJoin(sqlVendorServiceAsset).on(sqlTicket.assetId.eq(sqlVendorServiceAsset.assetId).and(sqlTicket.serviceTypeId.eq(sqlVendorServiceAsset.serviceTypeId)))
		.innerJoin(sqlVendorUser).on(sqlVendorServiceAsset.userId.eq(sqlVendorUser.userId))
		.innerJoin(sqlVendorOrganization).on(sqlVendorUser.organizationId.eq(sqlVendorOrganization.organizationId));
	}

	private BooleanExpression clientUserTicketFilter(org.springframework.security.core.userdetails.User customUser) {
		return sqlClientUserSite.userId.eq(customUser.getUsername());
	}

	private BooleanExpression vendorTicketFilterExpr(org.springframework.security.core.userdetails.User customUser) {
		return sqlTicket.ticketType.ne(LOG).and(sqlVendorServiceAsset.userId.endsWith(customUser.getUsername()));
	}

	public static void leftJoinTicketArchiveForTAT(JPASQLQuery jpaSQLQuery,SQLTicket sqlTicket,SQLTicketArchive sqlTicketArchiveResolved,SQLTicketArchive sqlTicketArchiveResolvedRelated) {
		jpaSQLQuery.leftJoin(sqlTicketArchiveResolved).on(sqlTicket.ticketId.eq(sqlTicketArchiveResolved.ticketId).and(sqlTicketArchiveResolved.statusId.eq(RESOLVED)))
		.leftJoin(sqlTicketArchiveResolvedRelated).on(sqlTicketArchiveResolved.ticketId.eq(sqlTicketArchiveResolvedRelated.ticketId).and(sqlTicketArchiveResolved.ticketArchiveId.eq(sqlTicketArchiveResolvedRelated.relatedArchiveId)));
	}

	private JPASQLQuery innerJoinClientUserSiteForTicket(JPASQLQuery jpaSQLQuery) {
		return jpaSQLQuery.innerJoin(sqlClientUserSite).on(sqlTicket.siteId.eq(sqlClientUserSite.siteId));
	}

	private QBean<Ticket> jpaTicketBeanExpr() {
		NumberExpression<Integer> tatExpr = ticketTATExpr(sqlTicket);
		NumberExpression<Integer> stopClockExpr = ticketStopClockExpr(sqlTicketArchiveResolved, sqlTicketArchiveResolvedRelated, sqlTicket, sqlClientUserSite);
		
		NumberExpression<Integer> actualTATExpr = tatExpr.subtract(stopClockExpr).as("actualTat").as("actualTat");
		stopClockExpr = stopClockExpr.as("stopClock").as("stopClock");

		QBean<Ticket> jpaTicketBeanExpr = Projections.bean(Ticket.class, jpaTicket,tatExpr.as("tat"),stopClockExpr,actualTATExpr);
		return jpaTicketBeanExpr;
	}

	private QBean<SimpleTicket> simpleTicketBeanExpression() {

		NumberExpression<Integer> tatExpr = ticketTATExpr(sqlTicket);
		NumberExpression<Integer> stopClockExpr = ticketStopClockExpr(sqlTicketArchiveResolved, sqlTicketArchiveResolvedRelated, sqlTicket, sqlClientUserSite);
		
		NumberExpression<Integer> actualTATExpr = tatExpr.subtract(stopClockExpr).as("actualTat").as("actualTat");
		stopClockExpr = stopClockExpr.as("stopClock").as("stopClock");
		
		QBean<SimpleSite> simpleSiteExpr = Projections.bean(SimpleSite.class, sqlTicket.siteId,sqlSite.name,sqlSite.circle);  
		QBean<ServiceType> serviceTypeExpr = Projections.bean(ServiceType.class, sqlTicket.serviceTypeId,sqlServiceType.name);
		QBean<IssueType> issueTypeExpr = Projections.bean(IssueType.class, sqlTicket.issueTypeId,sqlIssueType.name);
		QBean<SimpleUser> clientUserExpr = Projections.bean(SimpleUser.class, sqlTicket.reporterUserId.as("userId"));
		QBean<SimpleUser> vendorUserExpr = Projections.bean(SimpleUser.class, sqlVendorUser.userId,sqlVendorOrganization.name.as("organizationName"));
		QBean<SimpleTicket> resultListExpression = Projections.bean(SimpleTicket.class, sqlTicket.ticketId,sqlTicket.shortDesc,sqlTicket.source,sqlStatus.enumDescription.as("statusId"),sqlTicket.ticketType,simpleSiteExpr.as("site"),
				serviceTypeExpr.as("serviceType"),clientUserExpr.as("clientUser"),vendorUserExpr.as("vendorUser"),issueTypeExpr.as("issueType"),
				sqlTicket.createdTimestamp,sqlTicket.lastUpdatedTimestamp,tatExpr.as("tat"),
				actualTATExpr,stopClockExpr);
		return resultListExpression;
	}

	public static NumberExpression<Integer> ticketStopClockExpr(SQLTicketArchive sqlTicketArchiveResolved,SQLTicketArchive sqlTicketArchiveResolvedRelated,SQLTicket sqlTicket,SQLClientUserSite sqlClientUserSite) {
		Coalesce<Timestamp> clockEndTsExpr = (Coalesce<Timestamp>)sqlTicketArchiveResolvedRelated.lastUpdatedTimestamp.coalesce(sqlTicket.lastUpdatedTimestamp);

		NumberExpression<Integer> stopClockExpr = SQLExpressions.datediff(DatePart.second, sqlTicketArchiveResolved.lastUpdatedTimestamp, clockEndTsExpr.asDateTime());
		stopClockExpr = new CaseBuilder().when(sqlTicketArchiveResolvedRelated.lastUpdatedTimestamp.isNotNull()).then(stopClockExpr.coalesce(0).asNumber().sum().divide(sqlClientUserSite.userId.countDistinct()).intValue()).otherwise(Expressions.constant(0));
		return stopClockExpr;
	}

	public static NumberExpression<Integer> ticketTATExpr(SQLTicket sqlTicket) {
		DateTimeExpression<Timestamp> closeTimeExpr = new CaseBuilder().when(sqlTicket.statusId.eq("CLOSED").or(sqlTicket.statusId.eq("RESOLVED"))).then(sqlTicket.lastUpdatedTimestamp).otherwise(DateTimeOperation.currentTimestamp(Timestamp.class));
		NumberExpression<Integer> tatExpr = SQLExpressions.datediff(DatePart.second,sqlTicket.createdTimestamp,closeTimeExpr);
		return tatExpr;
	}

	public ListObjects listUserTickets(DataTableCriteria columns, String filterStatus, org.springframework.security.core.userdetails.User customUser,boolean allFields) {
		return listUserTicketsDSL(columns, filterStatus, customUser);
	}

	public ListObjects listUserTicketsByTicketFilter(TicketFilter ticketFilter, org.springframework.security.core.userdetails.User customUser,boolean allFields) {
		return listUserTicketsDSL(ticketFilter, customUser);
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
		//load the ticket first 
		Ticket storedTicket = entityManager.find(Ticket.class, ticket.getTicketId());
		TicketArchive ticketArchive = new TicketArchive(storedTicket);
		ticket.setDescription(description);
		//also attach the relate the previous ticket id 
		Query archiveQuery = entityManager.createNamedQuery("getLatestTicketArchivesForTicketId");
		archiveQuery.setParameter("ticketId", ticket.getTicketId());
		archiveQuery.setMaxResults(1);
		List<TicketArchive> ticketArchives = archiveQuery.getResultList();
		if(ticketArchives.size()>0){
			ticketArchive.setRelatedArchiveId(ticketArchives.get(0).getTicketArchiveId());
		}
		
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
		//run notifications in a  thread to avoid response delay with SMTP and SMS connections
		Runnable notifications = new Runnable() {
			
			@Override
			public void run() {
				sendEmail(mailService,bodyParameters,emailTemplate);
				sendSMS(smsService,bodyParameters,smsTemplate);
			}
		};
		
		Ticket formObject = (Ticket)bodyParameters.get("ticket");
		Thread notificationThread = new Thread(notifications,"Notification#"+formObject.getTicketId());
		notificationThread.start();
	}

	public void sendSMS(SMSService smsService, Map<String,Object> bodyParameters,String templateName) {
		Ticket formObject = (Ticket)bodyParameters.get("ticket");
		List<String> receiverContacts = new ArrayList<String>();
		switch (formObject.getStatus().getEnumerationId()) {
		case "OPEN":
		case "CLOSED":
			//check for comma separated numbers .. and add them.
			List<String> resolverContacts = SecureTUtils.fetchCSVAsList(formObject.getResolver().getMobile());
			if(resolverContacts!=null){
				receiverContacts.addAll(resolverContacts);
			}
			break;
		case "WORK_IN_PROGRESS":
		case "RESOLVED":
			//check for comma separated numbers .. and add them.
			List<String> reporterContacts = SecureTUtils.fetchCSVAsList(formObject.getReporter().getMobile());
			if(reporterContacts!=null){
				receiverContacts.addAll(reporterContacts);
			}
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

	public void assignAssetAndVendor(String objectName, Ticket formObject, BindingResult result) {
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
	
	protected Object parseHPToolMessage(org.springframework.security.core.userdetails.User customUser, HPToolInput hpToolInput, BindingResult result) {
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
			
			SimpleDateFormat sdf = new SimpleDateFormat(MDDYYYY_HHMMSS_A);
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

	protected void closeHPToolTicket(HPToolInput hpToolInput, Ticket ticketFound) {
		SimpleDateFormat sdf = new SimpleDateFormat(MDDYYYY_HHMMSS_A);
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

	protected PartOrderRequest fetchPartOrderRequestFromId(PartOrderRequest partOrderRequest) {
		JPAQuery partOrderRequestQuery = new JPAQuery(entityManager);
		partOrderRequestQuery.from(jpaPartOrderRequest).where(jpaPartOrderRequest.eq(partOrderRequest));
		partOrderRequest = partOrderRequestQuery.singleResult(jpaPartOrderRequest);
		return partOrderRequest;
	}

	protected SimplePartOrderRequest fetchSimplePartOrderRequestFromId(PartOrderRequest partOrderRequest) {
		JPAQuery partOrderRequestQuery = new JPAQuery(entityManager);
		partOrderRequestQuery.from(jpaPartOrderRequest).where(jpaPartOrderRequest.eq(partOrderRequest));
		SimplePartOrderRequest simplePartOrderRequest = partOrderRequestQuery.singleResult(poRequestFields());
		return simplePartOrderRequest;
	}

	protected PartOrderRequest updatePORequestStatus(org.springframework.security.core.userdetails.User customUser, PartOrderRequest partOrderRequest) {
		String statusDesc =  partOrderRequest.getStatus().getEnumDescription();
		partOrderRequest = fetchPartOrderRequestFromId(partOrderRequest);
		if(statusDesc.equals("Authorize") || statusDesc.equals("Reject")){
			User user = new User();
			user.setUserId(customUser.getUsername());
			partOrderRequest.setRespondedBy(user);
		}
		Enumeration poRequestStatus = new Enumeration();
		poRequestStatus.setEnumerationId(PO_STATUS_MAPPING.get(statusDesc));
		partOrderRequest.setStatus(poRequestStatus);
		entityManager.persist(partOrderRequest);
		return partOrderRequest;
	}

}
