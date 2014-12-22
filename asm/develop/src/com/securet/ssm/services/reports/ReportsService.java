package com.securet.ssm.services.reports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.securet.ssm.persistence.objects.Ticket;
import com.securet.ssm.persistence.views.SimpleTicket;
import com.securet.ssm.persistence.views.aggregates.TicketStatusSummary;
import com.securet.ssm.services.DefaultService;

@Controller
@Repository
@Service	
public class ReportsService extends BaseReportsService {

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
		DOWNLOAD_REPORT_FIELDS.add("Latitude");
		DOWNLOAD_REPORT_FIELDS.add("Longitude");

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value={"/reports","/reports/","/reports/dashboard"})
	public String dashboard(Model model){
		TicketStatusSummary ticketStatusSummary = getTicketCountByStatus("getTicketCountByStatus");
		List<Object[]> ticketCountByServiceTypeAndStatus = fetchResults("getTicketCountByServiceTypeAndStatus");
		List<Object[]> ticketCountByVendorUser = fetchResults("getTicketCountByVendorUser");
		List<Object[]> ticketCountByClientUser = fetchResults("getTicketCountByClientUser");
		model.addAttribute("ticketCountByClientUser", ticketCountByClientUser);
		model.addAttribute("ticketCountByVendorUser", ticketCountByVendorUser);
		model.addAttribute("ticketCountByServiceTypeAndStatus", ticketCountByServiceTypeAndStatus);
		model.addAttribute("ticketStatusSummary", ticketStatusSummary);
		return DefaultService.REPORTS+"dashboard";
	}

	@RequestMapping(value="/reports/getTicketsByTimePeriod")
	public String getTicketsByTimePeriod(@RequestParam("startDate") @DateTimeFormat(pattern="dd/MM/yyyy") Date startDate, @RequestParam("endDate") @DateTimeFormat(pattern="dd/MM/yyyy") Date endDate, Model model, HttpServletResponse response){
		if(startDate!=null && endDate!=null){
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
			Query ticketByDateRange = entityManager.createNamedQuery("getTicketByDateRange");
			ticketByDateRange.setParameter(1, startDate);
			ticketByDateRange.setParameter(2, endDate);
			List<Object[]> ticketSummary = ticketByDateRange.getResultList();
			response.setContentType("application/vnd.ms-excel");
			//response.setHeader("Content-Type", "application/vnd.ms-excel");;
			response.setHeader( "Content-Disposition", "filename=TICKETS_" + simpleDateFormat.format(startDate)+"_"+simpleDateFormat.format(endDate)+".xls");
			model.addAttribute("columnNames", DOWNLOAD_REPORT_FIELDS);
			model.addAttribute("ticketSummary", ticketSummary);
			return DefaultService.REPORTS+"downloadReport";
		}
		return DefaultService.REPORTS+"dashboard";
	}
}
