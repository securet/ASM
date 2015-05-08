package com.securet.ssm.services.reports;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysema.query.jpa.sql.JPASQLQuery;
import com.mysema.query.sql.SQLSubQuery;
import com.mysema.query.types.ArrayConstructorExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import com.securet.ssm.persistence.objects.SecureTObject;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLAsset;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLUserRole;
import com.securet.ssm.services.DefaultService;
import com.securet.ssm.services.vo.DashboardFilter;
import com.securet.ssm.services.vo.SLAPenaltyStats;
import com.securet.ssm.utils.SecureTUtils;

@Controller
@Repository
@Service
public class SLAPenaltyReportService extends BaseReportsService{

	
	private static final int HOUR_IN_SECS = 60*60;
	private static final SQLUserRole sqlUserRole = SQLUserRole.userRole;
	private static final int MIN_PENALTY_CLOCK = 12;
	private static final int CROSSED_PENALTY_CLOCK = 4;

	
	@RequestMapping(value="/reports/sla/status")
	public String slaStatus(@ModelAttribute("dashboardFilter") DashboardFilter dashboardFilter, Model model){
		List<SecureTObject> circles = fetchObjects("getMappedCircles");
		model.addAttribute("circles", circles);
		//cashout, availability, CER, TAT, House Keeping
		if(dashboardFilter.getMonth()!=0){
			findCashOutSLAPenaltyStatus(dashboardFilter);
		}
		
		return DefaultService.REPORTS+"slaStatus";
	}

	@RequestMapping(value="/reports/sla/fetchStatus")
	public String fetchSLAStatus(@ModelAttribute("dashboardFilter") DashboardFilter dashboardFilter, Model model){
		List<SLAPenaltyStats> slaPenaltyStats = findCashOutSLAPenaltyStatus(dashboardFilter);
		List<SecureTObject> circles = fetchObjects("getMappedCircles");
		model.addAttribute("circles", circles);
		model.addAttribute("slaPenaltyStats",slaPenaltyStats);
		return DefaultService.REPORTS+"slaStatus";
	}
		
	private List<SLAPenaltyStats> findCashOutSLAPenaltyStatus(DashboardFilter dashboardFilter) {
		SQLSubQuery vendorSLAMeasureDetailsQuery = vendorSLAMeasureDetailsQuery(dashboardFilter);		

		NumberExpression<Integer> tatExpr = ticketTATExpr();
		NumberExpression<Integer> stopClockExpr = ticketStopClockExpr();
		
		NumberExpression<Integer> actualTATExpr = tatExpr.subtract(stopClockExpr).as("actualTat");
		NumberExpression<Double> penaltyBlockExpr = tatExpr.subtract(stopClockExpr).doubleValue().subtract(HOUR_IN_SECS*MIN_PENALTY_CLOCK).divide(HOUR_IN_SECS*CROSSED_PENALTY_CLOCK).as("penaltyBlock");
		
		
		ArrayConstructorExpression resultSetExpr = Projections.array(Object[].class, (SimpleExpression)sqlVendorOrganization.name.as("vendorOrg"),
				(SimpleExpression)sqlTicket.ticketId,(SimpleExpression)sqlTicket.resolverUserId,(SimpleExpression)sqlTicket.reporterUserId,
				(SimpleExpression)sqlTicket.lastUpdatedTimestamp.month().as("monthOfResolution"),
				(SimpleExpression)sqlTicket.createdTimestamp,(SimpleExpression)sqlTicket.lastUpdatedTimestamp,(SimpleExpression)actualTATExpr, 
				(SimpleExpression)penaltyBlockExpr);
		
		SQLSubQuery vendorSLAMeasureGroupVendorQuery = new SQLSubQuery();
		Path<?> vendorDetails= new StringPath("vendorDetails");
		vendorSLAMeasureGroupVendorQuery.from(vendorSLAMeasureDetailsQuery.list(resultSetExpr),vendorDetails).groupBy(sqlVendorOrganization.name.as("vendorOrg"));
		
		NumberPath penaltyblocks = new NumberPath(Double.class, "penaltyBlock");		
		Path vendorOrg  = new StringPath("vendorOrg");
		StringPath ticketId  = new StringPath("ticketId");

		JPASQLQuery allVendorsSLAPenaltyDetailsQuery = new JPASQLQuery(entityManager, sqlTemplates);
		Path<?> vendorDetailPath = new StringPath("vendorDetailPath");
		Path<?> noOfIssues = new NumberPath(Integer.class,"noOfIssues");
		Path<?> totalPenalty = new NumberPath(Double.class,"totalPenalty");

		ArrayConstructorExpression vendorSLAMeasureGroupExpr = Projections.array(Object[].class, (SimpleExpression)vendorOrg
				,(SimpleExpression)ticketId.countDistinct().as("noOfIssues"),penaltyblocks.sum().as("totalPenalty"));

		
		allVendorsSLAPenaltyDetailsQuery.from(sqlVendorServiceAsset)
		.innerJoin(sqlVendorUser).on(sqlVendorServiceAsset.userId.eq(sqlVendorUser.userId))
		.innerJoin(sqlVendorOrganization).on(sqlVendorUser.organizationId.eq(sqlVendorOrganization.organizationId))
		.leftJoin(vendorSLAMeasureGroupVendorQuery.list(vendorSLAMeasureGroupExpr),vendorDetailPath ).on(sqlVendorOrganization.name.eq(vendorOrg));

		if(SecureTUtils.isNotEmpty(dashboardFilter.getCircleIds())){
			allVendorsSLAPenaltyDetailsQuery.leftJoin(SQLAsset.asset).on(SQLAsset.asset.assetId.eq(sqlVendorServiceAsset.assetId))
			.leftJoin(sqlSite).on(sqlSite.siteId.eq(SQLAsset.asset.siteId));
			allVendorsSLAPenaltyDetailsQuery.where(sqlSite.circle.in(dashboardFilter.getCircleIds()));			
		}
		
		if(dashboardFilter.getIssueGroup().equals("cashout")){
			allVendorsSLAPenaltyDetailsQuery.where(sqlVendorServiceAsset.serviceTypeId.eq(8));
		}else if(dashboardFilter.getIssueGroup().equals("caretaker")){
			allVendorsSLAPenaltyDetailsQuery.where(sqlVendorServiceAsset.serviceTypeId.eq(10));
		}
			
		allVendorsSLAPenaltyDetailsQuery.groupBy(sqlVendorOrganization.name);
		
		QBean<SLAPenaltyStats> finalFields = Projections.bean(SLAPenaltyStats.class, (SimpleExpression)sqlVendorOrganization.name.as("vendorOrganization"),
				(SimpleExpression)sqlVendorServiceAsset.assetId.countDistinct().as("noOfSites"),
				(SimpleExpression)noOfIssues,totalPenalty);
		return allVendorsSLAPenaltyDetailsQuery.list(finalFields);
	}

	private SQLSubQuery vendorSLAMeasureDetailsQuery(DashboardFilter dashboardFilter) {
		SQLSubQuery vendorSLAMeasureDetailsQuery = new SQLSubQuery();
		vendorSLAMeasureDetailsQuery.from(sqlUserRole).innerJoin(sqlVendorUser).on(sqlUserRole.userUserId.eq(sqlVendorUser.userId))
		.leftJoin(sqlVendorOrganization).on(sqlVendorUser.organizationId.eq(sqlVendorOrganization.organizationId))
		.leftJoin(sqlTicket).on(sqlVendorUser.userId.eq(sqlTicket.resolverUserId).and(sqlTicket.lastUpdatedTimestamp.month().eq(dashboardFilter.getMonth()))
				.and(sqlTicket.statusId.eq("RESOLVED").or(sqlTicket.statusId.eq("CLOSED"))));
		BooleanExpression issueTypeExpr = sqlTicket.issueTypeId.eq(sqlIssueType.issueTypeId);
		if(dashboardFilter.getIssueGroup().equals("cashout")){
			issueTypeExpr.and(sqlIssueType.issueGroup.eq(dashboardFilter.getIssueGroup()));
		}
		vendorSLAMeasureDetailsQuery.leftJoin(sqlIssueType).on(issueTypeExpr);
		leftJoinTicketArchiveForTAT(vendorSLAMeasureDetailsQuery);				
		vendorSLAMeasureDetailsQuery.groupBy(sqlTicket.ticketId).orderBy(sqlTicket.lastUpdatedTimestamp.desc());
		return vendorSLAMeasureDetailsQuery;
	}
	

}
