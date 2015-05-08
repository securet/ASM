package com.securet.ssm.services.reports;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.jpa.sql.JPASQLQuery;
import com.mysema.query.sql.DatePart;
import com.mysema.query.sql.SQLExpressions;
import com.mysema.query.sql.SQLSubQuery;
import com.mysema.query.types.ArrayConstructorExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.Projections;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.CaseBuilder;
import com.mysema.query.types.expr.Coalesce;
import com.mysema.query.types.expr.DateTimeExpression;
import com.mysema.query.types.expr.DateTimeOperation;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.path.SimplePath;
import com.securet.ssm.persistence.objects.querydsl.jpa.JPAClientUserSite;
import com.securet.ssm.persistence.objects.querydsl.jpa.JPAEnumeration;
import com.securet.ssm.persistence.objects.querydsl.jpa.JPAIssueType;
import com.securet.ssm.persistence.objects.querydsl.jpa.JPAOrganization;
import com.securet.ssm.persistence.objects.querydsl.jpa.JPAServiceType;
import com.securet.ssm.persistence.objects.querydsl.jpa.JPASite;
import com.securet.ssm.persistence.objects.querydsl.jpa.JPATicket;
import com.securet.ssm.persistence.objects.querydsl.jpa.JPATicketArchive;
import com.securet.ssm.persistence.objects.querydsl.jpa.JPAUser;
import com.securet.ssm.persistence.objects.querydsl.jpa.JPAVendorServiceAsset;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLAsset;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLClientUserSite;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLEnumeration;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLIssueType;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLModule;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLOrganization;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLPartOrderRequest;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLServiceType;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLSite;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLTicket;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLTicketArchive;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLUser;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLVendorServiceAsset;
import com.securet.ssm.persistence.views.aggregates.TicketStatusSummary;
import com.securet.ssm.services.SecureTService;
import com.securet.ssm.services.ticket.BaseTicketService;
import com.securet.ssm.services.vo.DashboardFilter;
import com.securet.ssm.utils.SecureTUtils;

public class BaseReportsService extends SecureTService {

	public static final String LOG = "LOG";
	public static final String COMPLAINT = "COMPLAINT";

	public static final String OPEN = "OPEN";
	public static final String WORK_IN_PROGRESS = "WORK_IN_PROGRESS";
	public static final String RESOLVED = "RESOLVED";
	public static final String CLOSED = "CLOSED";
	
	public static final String ALL_OK = "ALL OK";

	Path<Long> countOfTickets = new SimplePath<Long>(Long.class, "countOfTickets");

	SQLTicket sqlTicket = SQLTicket.ticket;
	SQLServiceType sqlServiceType = SQLServiceType.serviceType;
	SQLSite sqlSite = SQLSite.site;
	SQLIssueType sqlIssueType = SQLIssueType.issueType;
	SQLClientUserSite sqlClientUserSite = SQLClientUserSite.clientUserSite;
	SQLVendorServiceAsset sqlVendorServiceAsset = SQLVendorServiceAsset.vendorServiceAsset;
	SQLUser sqlVendorUser = SQLUser.user;
	SQLOrganization sqlVendorOrganization = SQLOrganization.organization;
	SQLTicketArchive sqlTicketArchiveResolved = new SQLTicketArchive("tar");
	SQLTicketArchive sqlTicketArchiveResolvedRelated = new SQLTicketArchive("tarRelated");;
	SQLEnumeration sqlStatus=new SQLEnumeration("status");
	SQLEnumeration sqlSeverity=new SQLEnumeration("severity");
	SQLModule sqlModule = SQLModule.module;
	SQLPartOrderRequest sqlPartOrderRequest = SQLPartOrderRequest.partOrderRequest;
	SQLUser sqlUser = SQLUser.user;
	SQLAsset sqlAsset = SQLAsset.asset;

	JPATicket jpaTicket = JPATicket.ticket;
	JPAServiceType jpaServiceType = JPAServiceType.serviceType;
	JPASite jpaSite = JPASite.site;
	JPAIssueType jpaIssueType = JPAIssueType.issueType;
	JPAClientUserSite jpaClientUserSite = JPAClientUserSite.clientUserSite;
	JPAVendorServiceAsset jpaVendorServiceAsset = JPAVendorServiceAsset.vendorServiceAsset;
	JPAUser jpaVendorUser = JPAUser.user;
	JPAOrganization jpaVendorOrganization = JPAOrganization.organization;
	JPATicketArchive jpaTicketArchiveResolved = new JPATicketArchive("tar");
	JPATicketArchive jpaTicketArchiveResolvedRelated = new JPATicketArchive("tarRelated");;
	JPAEnumeration jpaStatus=new JPAEnumeration("status");

	private static final Logger _logger = LoggerFactory.getLogger(BaseReportsService.class);

	
	public TicketStatusSummary getTicketCountByStatus(DashboardFilter dashboardFilter){
		//expression for subquery resultset
		@SuppressWarnings("unchecked")
		ArrayConstructorExpression<Object> resultSetExpr = Projections.array(Object[].class, (SimpleExpression)sqlTicket.statusId,(SimpleExpression)sqlTicket.ticketId.countDistinct().as(countOfTickets));

		JPASQLQuery query = makeTicketFilterQuery(dashboardFilter);
		query.groupBy(sqlTicket.statusId);
		@SuppressWarnings("unchecked")
		List<Object[]> ticketSummary = query.list(resultSetExpr);
		
		TicketStatusSummary ticketStatusSummary = new TicketStatusSummary();
		for(Object[] object:ticketSummary){
			if(object[0].equals(BaseTicketService.OPEN)){
				ticketStatusSummary.setOpenCount((Long)object[1]);
			}else if(object[0].equals(BaseTicketService.WORK_IN_PROGRESS)){
				ticketStatusSummary.setWorkInProgressCount((Long)object[1]);
			}else if(object[0].equals(BaseTicketService.RESOLVED)){
				ticketStatusSummary.setResolvedCount((Long)object[1]);
			}else if(object[0].equals(BaseTicketService.CLOSED)){
				ticketStatusSummary.setClosedCount((Long)object[1]);
			}
		}
		return ticketStatusSummary;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Object[]> fetchTicketCountByServiceTypeAndStatus(DashboardFilter dashboardFilter) {
		JPASQLQuery query = new JPASQLQuery(entityManager,sqlTemplates);
		
		//prepare the subquery - which filters if necessary based on user criteria
		SQLSubQuery ticketFilterSubquery = makeTicketFilterSubQuery(dashboardFilter);
		ticketFilterSubquery.groupBy(sqlTicket.serviceTypeId,sqlTicket.statusId);

		//expression for subquery resultset
		ArrayConstructorExpression resultSetExpr = Projections.array(Object[].class, (SimpleExpression)sqlTicket.serviceTypeId,(SimpleExpression)sqlTicket.statusId,(SimpleExpression)sqlTicket.ticketId.countDistinct().as(countOfTickets));

		//Final query join will be 
		SQLServiceType allServiceTypes = new SQLServiceType("allServiceTypes");
		SQLEnumeration allStatus = new SQLEnumeration("allStatus");
		Path<String> ticketDetailsAlias = new SimplePath<String>(String.class, "ticketDetails");
		SQLServiceType ticketDetailsServiceType = new SQLServiceType("ticketDetails");
		SQLTicket ticketDetails = new SQLTicket("ticketDetails");
		//expression final result
		ArrayConstructorExpression resultSetExprFinal = Projections.array(Object[].class, (SimpleExpression)allServiceTypes.serviceTypeId,(SimpleExpression)allServiceTypes.name,(SimpleExpression)allStatus.enumerationId,(SimpleExpression)countOfTickets);

		//Join Cartesian between service types and ticket status.. 
		query.from(allServiceTypes).join(allStatus)//.on(allServiceTypes.name.ne("ALL OK"))
		//join with ticket filter subquery in service type and ticket status
		.leftJoin(ticketFilterSubquery.list(resultSetExpr), ticketDetailsAlias).on(allServiceTypes.serviceTypeId.eq(ticketDetailsServiceType.serviceTypeId).and(allStatus.enumerationId.eq(ticketDetails.statusId)));
		query.where(allStatus.enumerationId.ne("REOPEN").and(allStatus.enumTypeId.eq("TICKET_STATUS")));

		//result...
		List<Object[]> resultsList = query.list(resultSetExprFinal);
		_logger.debug("Tickets by service results : "+resultsList.size());
		return resultsList;
	}

	protected SQLSubQuery makeTicketFilterSubQuery(DashboardFilter dashboardFilter) {
		SQLSubQuery ticketFilterSubquery = new SQLSubQuery();
		ticketFilterSubquery.from(sqlTicket).innerJoin(sqlSite).on(sqlTicket.siteId.eq(sqlSite.siteId))
		.innerJoin(sqlClientUserSite).on(sqlTicket.siteId.eq(sqlClientUserSite.siteId).and(sqlSite.siteId.eq(sqlClientUserSite.siteId)))
		.where(dashboardFilterPredicate(dashboardFilter));
		return ticketFilterSubquery;
	}

	protected JPASQLQuery makeTicketFilterQuery(DashboardFilter dashboardFilter) {
		JPASQLQuery ticketFilterQuery = new JPASQLQuery(entityManager,sqlTemplates);
		ticketFilterQuery.from(sqlTicket).innerJoin(sqlSite).on(sqlTicket.siteId.eq(sqlSite.siteId))
		.innerJoin(sqlClientUserSite).on(sqlTicket.siteId.eq(sqlClientUserSite.siteId).and(sqlSite.siteId.eq(sqlClientUserSite.siteId)))
		.where(dashboardFilterPredicate(dashboardFilter));
		return ticketFilterQuery;
	}

	private void addDashboardFilter(DashboardFilter dashboardFilter, JPASQLQuery query) {
		Predicate dashboardPredicate = dashboardFilterPredicate(dashboardFilter);
		query.where(dashboardPredicate);
	}

	private Predicate dashboardFilterPredicate(DashboardFilter dashboardFilter) {
		if(dashboardFilter.getDashboardStartDate()==null || dashboardFilter.getDashboardEndDate()==null){
			//fetch last one month data if no data time period given.
			long currentTime = System.currentTimeMillis();
			long startTimeMillis = currentTime-((1000*60*60*24*30l));
			dashboardFilter.setDashboardStartDate(new Date(startTimeMillis));
			dashboardFilter.setDashboardEndDate(new Date(currentTime));
		}

		BooleanExpression dashboardExpression = sqlTicket.createdTimestamp.between(new Timestamp(dashboardFilter.getDashboardStartDate().getTime()), new Timestamp(dashboardFilter.getDashboardEndDate().getTime()));
		
		if(dashboardFilter.getServiceType()!=null && !dashboardFilter.getServiceType().isEmpty()){
			dashboardExpression=dashboardExpression.and(sqlServiceType.name.eq(dashboardFilter.getServiceType()));
		}
		
		if(SecureTUtils.isNotEmpty(dashboardFilter.getStatusId())){
			dashboardExpression=dashboardExpression.and(sqlTicket.statusId.eq(dashboardFilter.getStatusId()));
		}

		if(SecureTUtils.isNotEmpty(dashboardFilter.getCircleIds())){
			dashboardExpression=dashboardExpression.and(sqlSite.circle.in(dashboardFilter.getCircleIds()));
		}

		if(SecureTUtils.isNotEmpty(dashboardFilter.getModuleIds())){
			dashboardExpression=dashboardExpression.and(sqlSite.moduleId.in(dashboardFilter.getModuleIds()));
		}

		if(SecureTUtils.isNotEmpty(dashboardFilter.getClientUserIds())){
			dashboardExpression=dashboardExpression.and(sqlClientUserSite.userId.in(dashboardFilter.getClientUserIds()));
		}

		Predicate dashboardPredicate = (Predicate) dashboardExpression;
		return dashboardPredicate;
	}

	protected JPASQLQuery ticketDashboardQuery(DashboardFilter dashboardFilter) {
		JPASQLQuery query = new JPASQLQuery(entityManager,sqlTemplates);
		query.from(sqlTicket);
		addDashboardFilter(dashboardFilter, query);
		return query;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List<Object[]> ticketReportSummary(DashboardFilter dashboardFilter) {
		JPASQLQuery query = ticketDashboardQuery(dashboardFilter);
		
		query.innerJoin(sqlSite).on(sqlTicket.siteId.eq(sqlSite.siteId))
		.innerJoin(sqlServiceType).on(sqlTicket.serviceTypeId.eq(sqlServiceType.serviceTypeId))
		.innerJoin(sqlIssueType).on(sqlTicket.issueTypeId.eq(sqlIssueType.issueTypeId))
		.innerJoin(sqlStatus).on(sqlTicket.statusId.eq(sqlStatus.enumerationId))
		.innerJoin(sqlSeverity).on(sqlTicket.severity.eq(sqlSeverity.enumerationId))
		.innerJoin(sqlClientUserSite).on(sqlTicket.siteId.eq(sqlClientUserSite.siteId))
		.innerJoin(sqlModule).on(sqlSite.moduleId.eq(sqlModule.moduleId));
		
		ArrayConstructorExpression resultSetExpr = ticketReportSummaryFields();

		query.groupBy(sqlTicket.ticketId);
		List<Object[]> ticketSummary = query.list(resultSetExpr);
		return ticketSummary;
	}

	@SuppressWarnings(value={"rawtypes","unchecked"})
	protected ArrayConstructorExpression ticketReportSummaryFields() {
		ArrayConstructorExpression resultSetExpr = Projections.array(Object[].class, (SimpleExpression)sqlTicket.ticketId,(SimpleExpression)sqlSite.name.as("siteName"),
				(SimpleExpression)sqlTicket.reporterUserId,(SimpleExpression)sqlServiceType.name.as("serviceType"),(SimpleExpression)sqlIssueType.name.as("issueType"),
				(SimpleExpression)sqlTicket.resolverUserId,(SimpleExpression)sqlStatus.enumDescription.as("status"),(SimpleExpression)sqlTicket.description,
				(SimpleExpression)sqlTicket.createdTimestamp,(SimpleExpression)sqlTicket.lastUpdatedTimestamp,(SimpleExpression)sqlSeverity.enumDescription.as("severity"),
				(SimpleExpression)sqlSite.city,(SimpleExpression)sqlModule.name.as("module"),(SimpleExpression)sqlSite.circle,
				(SimpleExpression)sqlTicket.latitude,(SimpleExpression)sqlTicket.longitude);
		return resultSetExpr;
	}

	protected void leftJoinTicketArchiveForTAT(SQLSubQuery jpaSQLQuery) {
		jpaSQLQuery.leftJoin(sqlTicketArchiveResolved).on(sqlTicket.ticketId.eq(sqlTicketArchiveResolved.ticketId).and(sqlTicketArchiveResolved.statusId.eq(RESOLVED)))
		.leftJoin(sqlTicketArchiveResolvedRelated).on(sqlTicketArchiveResolved.ticketId.eq(sqlTicketArchiveResolvedRelated.ticketId).and(sqlTicketArchiveResolved.ticketArchiveId.eq(sqlTicketArchiveResolvedRelated.relatedArchiveId)));
	}

	protected NumberExpression<Integer> ticketStopClockExpr() {
		Coalesce<Timestamp> clockEndTsExpr = (Coalesce<Timestamp>)sqlTicketArchiveResolvedRelated.lastUpdatedTimestamp.coalesce(sqlTicket.lastUpdatedTimestamp);

		NumberExpression<Integer> stopClockExpr = SQLExpressions.datediff(DatePart.second, sqlTicketArchiveResolved.lastUpdatedTimestamp, clockEndTsExpr.asDateTime());
		stopClockExpr = stopClockExpr.coalesce(0).asNumber().sum().intValue();
		return stopClockExpr;
	}

	protected NumberExpression<Integer> ticketTATExpr() {
		DateTimeExpression<Timestamp> closeTimeExpr = new CaseBuilder().when(sqlTicket.statusId.eq("CLOSED").or(sqlTicket.statusId.eq("RESOLVED"))).then(sqlTicket.lastUpdatedTimestamp).otherwise(DateTimeOperation.currentTimestamp(Timestamp.class));
		NumberExpression<Integer> tatExpr = SQLExpressions.datediff(DatePart.second,sqlTicket.createdTimestamp,closeTimeExpr);
		return tatExpr;
	}

}
