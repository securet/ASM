package com.securet.ssm.services.reports;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.securet.ssm.persistence.views.aggregates.TicketStatusSummary;
import com.securet.ssm.services.DefaultService;

@Controller
@Repository
@Service	
public class ReportsService extends BaseReportsService {

	@RequestMapping("/reports/dashboard")
	public String dashboard(Model model){
		TicketStatusSummary ticketStatusSummary = getTicketCountByStatus("getTicketCountByStatus");
		List<Object[]> ticketCountByServiceTypeAndStatus = fetchResults("getTicketCountByServiceTypeAndStatus");
		List<Object[]> ticketCountByVendorUser = fetchResults("getTicketCountByVendorUser");
		
		model.addAttribute("ticketCountByVendorUser", ticketCountByVendorUser);
		model.addAttribute("ticketCountByServiceTypeAndStatus", ticketCountByServiceTypeAndStatus);
		model.addAttribute("ticketStatusSummary", ticketStatusSummary);
		return DefaultService.REPORTS+"dashboard";
	}
}
