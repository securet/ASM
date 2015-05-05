package com.securet.ssm.services.vo;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class DashboardFilter extends DataTableCriteria{
	
	public DashboardFilter() {
	}
	
	@DateTimeFormat(pattern="dd-MM-yyyy")
	private Date dashboardStartDate;
	
	@DateTimeFormat(pattern="dd-MM-yyyy")
	private Date dashboardEndDate;

	private int month;

	private int serviceTypeId;
	
	private String serviceType;

	private String statusId;

	private List<Integer> moduleIds;
	
	private List<String> circleIds;
	
	private List<String> clientUserIds;

	private String issueGroup;
	
	public Date getDashboardStartDate() {
		return dashboardStartDate;
	}

	public void setDashboardStartDate(Date dashboardStartDate) {
		this.dashboardStartDate = dashboardStartDate;
	}

	public Date getDashboardEndDate() {
		return dashboardEndDate;
	}

	public void setDashboardEndDate(Date dashboardEndDate) {
		this.dashboardEndDate = dashboardEndDate;
	}

	public int getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(int serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public List<Integer> getModuleIds() {
		return moduleIds;
	}

	public void setModuleIds(List<Integer> moduleIds) {
		this.moduleIds = moduleIds;
	}

	public List<String> getCircleIds() {
		return circleIds;
	}

	public void setCircleIds(List<String> circleIds) {
		this.circleIds = circleIds;
	}

	public List<String> getClientUserIds() {
		return clientUserIds;
	}

	public void setClientUserIds(List<String> clientUserIds) {
		this.clientUserIds = clientUserIds;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getIssueGroup() {
		return issueGroup;
	}

	public void setIssueGroup(String issueGroup) {
		this.issueGroup = issueGroup;
	}

}
