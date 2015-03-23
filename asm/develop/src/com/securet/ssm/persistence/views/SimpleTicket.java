package com.securet.ssm.persistence.views;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.securet.ssm.persistence.objects.IssueType;
import com.securet.ssm.persistence.objects.ServiceType;

public class SimpleTicket {

	private String ticketId;
	private String shortDesc;
	@JsonInclude(Include.NON_NULL)
	private String statusId;
	@JsonInclude(Include.NON_NULL)
	private String ticketType;
	@JsonInclude(Include.NON_NULL)
	private SimpleSite site;
	@JsonInclude(Include.NON_NULL)
	private ServiceType serviceType;
	@JsonInclude(Include.NON_NULL)
	private IssueType issueType;
	@JsonInclude(Include.NON_NULL)
	private SimpleUser clientUser;
	@JsonInclude(Include.NON_NULL)
	private SimpleUser vendorUser;

	
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss a",timezone="IST")
	private Date createdTimestamp;
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss a",timezone="IST")
	private Date lastUpdatedTimestamp;
	
	private int actualTat;
	private int tat;
	private int stopClock;
	
	public SimpleTicket() {
	}
	
	
	public SimpleTicket(String ticketId, String shortDesc, String statusId, int siteId, String siteName, int serviceTypeId, String serviceTypeName, Date createdTimestamp) {
		this.ticketId = ticketId;
		this.shortDesc = shortDesc;
		this.statusId = statusId;
		this.site=new SimpleSite(siteId, siteName);
		this.serviceType=new ServiceType();
		this.serviceType.setServiceTypeId(serviceTypeId);
		this.serviceType.setName(serviceTypeName);
		this.createdTimestamp = createdTimestamp;
	}


	public SimpleTicket(String ticketId, String statusId, int siteId, String siteName, int serviceTypeId, String serviceTypeName, Date createdTimestamp) {
		this.ticketId = ticketId;
		this.statusId = statusId;
		this.site=new SimpleSite(siteId, siteName);
		this.serviceType=new ServiceType();
		this.serviceType.setServiceTypeId(serviceTypeId);
		this.serviceType.setName(serviceTypeName);
		this.createdTimestamp = createdTimestamp;
	}

	public String getTicketId() {
		return ticketId;
	}

	public String getShortDesc() {
		return shortDesc;
	}


	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}


	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public SimpleSite getSite() {
		return site;
	}


	public void setSite(SimpleSite site) {
		this.site = site;
	}


	public ServiceType getServiceType() {
		return serviceType;
	}


	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}


	public SimpleUser getClientUser() {
		return clientUser;
	}


	public void setClientUser(SimpleUser clientUser) {
		this.clientUser = clientUser;
	}


	public SimpleUser getVendorUser() {
		return vendorUser;
	}


	public void setVendorUser(SimpleUser vendorUser) {
		this.vendorUser = vendorUser;
	}


	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}


	public int getActualTat() {
		return actualTat;
	}


	public void setActualTat(int actualTat) {
		this.actualTat = actualTat;
	}


	public int getTat() {
		return tat;
	}


	public void setTat(int tat) {
		this.tat = tat;
	}


	public int getStopClock() {
		return stopClock;
	}


	public void setStopClock(int stopClock) {
		this.stopClock = stopClock;
	}


	public String getTicketType() {
		return ticketType;
	}


	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}


	public IssueType getIssueType() {
		return issueType;
	}


	public void setIssueType(IssueType issueType) {
		this.issueType = issueType;
	}


	public Date getLastUpdatedTimestamp() {
		return lastUpdatedTimestamp;
	}


	public void setLastUpdatedTimestamp(Date lastUpdatedTimestamp) {
		this.lastUpdatedTimestamp = lastUpdatedTimestamp;
	}
	
	//t.ticketId,t.statusId,t.siteId,s.name site,t.serviceTypeId, st.name serviceType, t.createdTimestamp
	
	
}
