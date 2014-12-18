package com.securet.ssm.services.reports;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.securet.ssm.persistence.views.aggregates.TicketStatusSummary;
import com.securet.ssm.services.SecureTService;
import com.securet.ssm.services.ticket.BaseTicketService;

public class BaseReportsService extends SecureTService {

	private static final Logger _logger = LoggerFactory.getLogger(BaseReportsService.class);
	
	public TicketStatusSummary getTicketCountByStatus(String namedQuery){
		Query ticketCountQuery = entityManager.createNamedQuery(namedQuery);
		List<Object[]> ticketSummary = ticketCountQuery.getResultList();
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
	
}
