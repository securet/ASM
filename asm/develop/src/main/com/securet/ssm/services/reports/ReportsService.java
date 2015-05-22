package com.securet.ssm.services.reports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysema.query.jpa.sql.JPASQLQuery;
import com.mysema.query.types.ArrayConstructorExpression;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Projections;
import com.mysema.query.types.expr.SimpleExpression;
import com.securet.ssm.persistence.objects.Geo;
import com.securet.ssm.persistence.objects.Module;
import com.securet.ssm.persistence.objects.SecureTObject;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLSite;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLTicket;
import com.securet.ssm.persistence.views.aggregates.TicketStatusSummary;
import com.securet.ssm.services.ActionHelpers;
import com.securet.ssm.services.DefaultService;
import com.securet.ssm.services.ticket.BaseTicketService;
import com.securet.ssm.services.vo.DashboardFilter;
import com.securet.ssm.services.vo.DataTableCriteria;
import com.securet.ssm.services.vo.ListObjects;
import com.securet.ssm.utils.SecureTUtils;

@Controller
@Repository
@Service	
public class ReportsService extends BaseReportsService {

	
	private static final Map<String, Expression> fieldExprMapping = new HashMap<String, Expression>();
	
	
	private static List<String> DOWNLOAD_REPORT_FIELDS = new ArrayList<String>();
	static{
		DOWNLOAD_REPORT_FIELDS.add("TicketId");
		DOWNLOAD_REPORT_FIELDS.add("Site");
		DOWNLOAD_REPORT_FIELDS.add("Client User");
		DOWNLOAD_REPORT_FIELDS.add("Service Type");
		DOWNLOAD_REPORT_FIELDS.add("Issue Type");
		DOWNLOAD_REPORT_FIELDS.add("Vendor User");
		DOWNLOAD_REPORT_FIELDS.add("Status");
		DOWNLOAD_REPORT_FIELDS.add("Description");
		DOWNLOAD_REPORT_FIELDS.add("Created Date");
		DOWNLOAD_REPORT_FIELDS.add("Last Updated Date");
		DOWNLOAD_REPORT_FIELDS.add("Severity");
		DOWNLOAD_REPORT_FIELDS.add("City");
		DOWNLOAD_REPORT_FIELDS.add("Module");
		DOWNLOAD_REPORT_FIELDS.add("Circle");
		DOWNLOAD_REPORT_FIELDS.add("Latitude");
		DOWNLOAD_REPORT_FIELDS.add("Longitude");

		fieldExprMapping.put("reporterUserId", SQLTicket.ticket.resolverUserId);
		fieldExprMapping.put("reporterUserId", SQLTicket.ticket.reporterUserId);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value={"/reports","/reports/","/reports/dashboard"},method = { RequestMethod.GET, RequestMethod.POST })
	public String dashboard(@ModelAttribute("dashboardFilter") DashboardFilter dashboardFilter,BindingResult result,Model model){
		TicketStatusSummary ticketStatusSummary = getTicketCountByStatus(dashboardFilter);
		List<Object[]> ticketCountByServiceTypeAndStatus = fetchTicketCountByServiceTypeAndStatus(dashboardFilter);
		List<SecureTObject> clientUsers =   fetchObjects("getAllClientUsers");
		List<SecureTObject> modules =   fetchObjects("getModuleForView");
/*		List<SecureTObject> cities = fetchObjects("getCityForView");
		List<SecureTObject> circles = fetchObjects("getStateForView");
		circles.addAll(cities);
*/		//List<Object[]> ticketCountByVendorUser = fetchTicketCountByVendorUser(dashboardFilter);
		//List<Object[]> ticketCountByClientUser = fetchTicketCountByClientUser(dashboardFilter);
		//model.addAttribute("ticketCountByClientUser", ticketCountByClientUser);
		//model.addAttribute("ticketCountByVendorUser", ticketCountByVendorUser);
		List<SecureTObject> circles = fetchObjects("getMappedCircles");
		model.addAttribute("circles", circles);
		model.addAttribute("modules", modules);
		model.addAttribute("clientUsers", clientUsers);
		model.addAttribute("ticketCountByServiceTypeAndStatus", ticketCountByServiceTypeAndStatus);
		model.addAttribute("ticketStatusSummary", ticketStatusSummary);
		return DefaultService.REPORTS+"dashboard";
	}

	@RequestMapping(value={"/reports/vendorUserTicketCount"})
	public @ResponseBody ListObjects vendorUserTicketCount(@ModelAttribute DashboardFilter dashboardFilter, BindingResult result,Model model){
		Map<String, JPASQLQuery> jpaQueriesToRun = prepareTicketsCountByField(dashboardFilter,sqlTicket.resolverUserId);
		
		//also add the ticket type and status filter.. 
		JPASQLQuery dataQuery = jpaQueriesToRun.get(DataTableCriteria.DATA_QUERY);
		
		addVendorUserTicketCountFilters(dataQuery);

		JPASQLQuery countQuery = jpaQueriesToRun.get(DataTableCriteria.COUNT_QUERY);
		addVendorUserTicketCountFilters(countQuery);

		ArrayConstructorExpression resultSetExpr = vendorTicketCountExpr();
		return ActionHelpers.listSimpleObjectFromQueryDSL(dashboardFilter, jpaQueriesToRun, resultSetExpr, sqlTicket.resolverUserId.countDistinct());
	}

	private void addVendorUserTicketCountFilters(JPASQLQuery dataQuery) {
		List<String> validStatus = new ArrayList<String>();
		validStatus.add(BaseTicketService.OPEN);
		validStatus.add(BaseTicketService.WORK_IN_PROGRESS);
		dataQuery.where(sqlTicket.ticketType.ne(BaseTicketService.LOG).and(sqlTicket.statusId.in(validStatus)));
	}

	private Map<String, JPASQLQuery> prepareTicketsCountByField(DashboardFilter dashboardFilter, Expression<?> groupField) {
		Map<String,JPASQLQuery> jpaQueriesToRun = new HashMap<String, JPASQLQuery>(); 
		JPASQLQuery dataQuery = makeTicketFilterQuery(dashboardFilter); //ticketDashboardQuery(dashboardFilter);
		jpaQueriesToRun.put(DataTableCriteria.DATA_QUERY, dataQuery);
		jpaQueriesToRun.put(DataTableCriteria.COUNT_QUERY, dataQuery.clone());
		if(dashboardFilter!=null && dashboardFilter.getOrder()!=null && dashboardFilter.getOrder().size()>0){
			dashboardFilter.makeOrderByExpression(dashboardFilter, dataQuery,fieldExprMapping);
		}
		dataQuery.groupBy(groupField);
		return jpaQueriesToRun;
	}

	private ArrayConstructorExpression vendorTicketCountExpr() {
		ArrayConstructorExpression resultSetExpr = Projections.array(Object[].class, (SimpleExpression)sqlTicket.resolverUserId,(SimpleExpression)sqlTicket.ticketId.countDistinct().as("noOfTickets"));
		return resultSetExpr;
	}

	@RequestMapping(value={"/reports/clientUserTicketCount"})
	public @ResponseBody ListObjects clientUserTicketCount(@ModelAttribute DashboardFilter dashboardFilter, BindingResult result,Model model){
		Map<String, JPASQLQuery> jpaQueriesToRun = prepareTicketsCountByField(dashboardFilter,sqlTicket.reporterUserId);
		ArrayConstructorExpression resultSetExpr = clientTicketCountExpr();
		return ActionHelpers.listSimpleObjectFromQueryDSL(dashboardFilter, jpaQueriesToRun, resultSetExpr, sqlTicket.reporterUserId.countDistinct());
	}

	private DashboardFilter setDashBoardFilterDate(Date dashboardStartDate, Date dashboardEndDate) {
		DashboardFilter dashboardFilter = new DashboardFilter();
		dashboardFilter.setDashboardStartDate(dashboardStartDate);
		dashboardFilter.setDashboardEndDate(dashboardEndDate);
		return dashboardFilter;
	}

	private ArrayConstructorExpression clientTicketCountExpr() {
		ArrayConstructorExpression resultSetExpr = Projections.array(Object[].class, (SimpleExpression)sqlTicket.reporterUserId,(SimpleExpression)sqlTicket.ticketId.countDistinct().as("noOfTickets"));
		return resultSetExpr;
	}

	
	@RequestMapping(value="/reports/getTicketsByTimePeriod")
	public String getTicketsByTimePeriod(@ModelAttribute("dashboardFilter") DashboardFilter dashboardFilter, Model model, HttpServletResponse response){
		if(dashboardFilter.getDashboardStartDate()!=null && dashboardFilter.getDashboardEndDate()!=null){

			List<Object[]> ticketSummary = ticketReportSummary(dashboardFilter);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
			response.setContentType("application/vnd.ms-excel");
			//response.setHeader("Content-Type", "application/vnd.ms-excel");
			
			StringBuilder fileName=new StringBuilder();
			fileName.append("TICKETS_" ).append(simpleDateFormat.format(dashboardFilter.getDashboardStartDate()))
			.append("_TO_"+simpleDateFormat.format(dashboardFilter.getDashboardEndDate()));

			if(!SecureTUtils.isEmpty(dashboardFilter.getServiceType())){
				fileName.append("_").append(dashboardFilter.getServiceType().replaceAll(" ", "_"));
			}
			
			if(!SecureTUtils.isEmpty(dashboardFilter.getStatusId())){
				fileName.append("_").append(dashboardFilter.getStatusId());
			}

			fileName.append(".xls");
			response.setHeader( "Content-Disposition", "attachment; filename="+fileName.toString()); 
			model.addAttribute("columnNames", DOWNLOAD_REPORT_FIELDS);
			model.addAttribute("ticketSummary", ticketSummary);
			return DefaultService.REPORTS+"downloadReport";
		}
		return DefaultService.REPORTS+"dashboard";
	}

	@RequestMapping(value="/reports/downloadVendorUserTicketCount")
	public String downloadVendorUserTicketCount(@ModelAttribute("dashboardFilter") DashboardFilter dashboardFilter, Model model, HttpServletResponse response){
		if(dashboardFilter.getDashboardStartDate()!=null && dashboardFilter.getDashboardEndDate()!=null){
			Map<String, JPASQLQuery> jpaQueriesToRun  = prepareTicketsCountByField(dashboardFilter, sqlTicket.resolverUserId);
			JPASQLQuery query =  jpaQueriesToRun.get(DataTableCriteria.DATA_QUERY);
			//also add the ticket type and status filter.. 
			addVendorUserTicketCountFilters(query);

			List<Object[]> ticketSummary = query.list(vendorTicketCountExpr());

			List<String> vendorFieldNames = new ArrayList<String>();
			vendorFieldNames.add("Vendor");
			vendorFieldNames.add("No Of Tickets");			
			prepareForDownload(dashboardFilter, model, response, ticketSummary,vendorFieldNames,"TICKETS_BY_VENDOR_");
			return DefaultService.REPORTS+"downloadReport";
		}
		return DefaultService.REPORTS+"dashboard";
	}

	@RequestMapping(value="/reports/downloadClientUserTicketCount")
	public String downloadClientUserTicketCount(@ModelAttribute("dashboardFilter") DashboardFilter dashboardFilter, Model model, HttpServletResponse response){
		if(dashboardFilter.getDashboardStartDate()!=null && dashboardFilter.getDashboardEndDate()!=null){
			Map<String, JPASQLQuery> jpaQueriesToRun  = prepareTicketsCountByField(dashboardFilter, sqlTicket.reporterUserId);
			JPASQLQuery query =  jpaQueriesToRun.get(DataTableCriteria.DATA_QUERY);
			
			List<Object[]> ticketSummary = query.list(clientTicketCountExpr());

			List<String> clientFieldNames = new ArrayList<String>();
			clientFieldNames.add("Client User");
			clientFieldNames.add("No Of Tickets");			
			prepareForDownload(dashboardFilter, model, response, ticketSummary,clientFieldNames,"TICKETS_BY_CLIENT_");
			return DefaultService.REPORTS+"downloadReport";
		}
		return DefaultService.REPORTS+"dashboard";
	}

	private void prepareForDownload(DashboardFilter dashboardFilter, Model model, HttpServletResponse response, List<Object[]> ticketSummary, List<String> fieldNameList, Object name) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
		response.setContentType("application/vnd.ms-excel");
		//response.setHeader("Content-Type", "application/vnd.ms-excel");
		
		StringBuilder fileName=new StringBuilder();
		fileName.append(name).append(simpleDateFormat.format(dashboardFilter.getDashboardStartDate()))
		.append("_TO_"+simpleDateFormat.format(dashboardFilter.getDashboardEndDate()));

		if(!SecureTUtils.isEmpty(dashboardFilter.getServiceType())){
			fileName.append("_").append(dashboardFilter.getServiceType().replaceAll(" ", "_"));
		}
		
		if(!SecureTUtils.isEmpty(dashboardFilter.getStatusId())){
			fileName.append("_").append(dashboardFilter.getStatusId());
		}

		fileName.append(".xls");
		response.setHeader( "Content-Disposition", "attachment; filename="+fileName.toString());
		model.addAttribute("columnNames", fieldNameList);
		model.addAttribute("ticketSummary", ticketSummary);
	}
	
	
}
