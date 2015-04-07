package com.securet.ssm.services.vo;

import java.util.List;

public class TicketFilter extends DataTableCriteria {
	
	private List<String> statusFilter;
	
	public List<String> getStatusFilter() {
		return statusFilter;
	}

	public void setStatusFilter(List<String> statusFilter) {
		this.statusFilter = statusFilter;
	}

}
