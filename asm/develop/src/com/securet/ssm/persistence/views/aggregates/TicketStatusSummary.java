package com.securet.ssm.persistence.views.aggregates;

public class TicketStatusSummary {
	private long openCount=0;
	private long workInProgressCount=0;
	private long resolvedCount=0;
	private long closedCount=0;
	private long totalCount=0;
	
	
	public long getOpenCount() {
		return openCount;
	}
	public void setOpenCount(long object) {
		this.openCount = object;
	}
	public long getWorkInProgressCount() {
		return workInProgressCount;
	}
	public void setWorkInProgressCount(long workInProgressCount) {
		this.workInProgressCount = workInProgressCount;
	}
	public long getResolvedCount() {
		return resolvedCount;
	}
	public void setResolvedCount(long resolvedCount) {
		this.resolvedCount = resolvedCount;
	}
	public long getClosedCount() {
		return closedCount;
	}
	public void setClosedCount(long closedCount) {
		this.closedCount = closedCount;
	}

	public long getTotalCount() {
		totalCount=openCount+workInProgressCount+resolvedCount+closedCount;
		return totalCount;
	}
}
