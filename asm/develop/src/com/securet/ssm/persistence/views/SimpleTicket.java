package com.securet.ssm.persistence.views;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SimpleTicket {

	private String ticketId;
	private String shortDesc;
	private String statusId;
	private String siteId;
	private String siteName;
	private String serviceTypeId;
	private String serviceTypeName;
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss a",timezone="IST")
	private Date createdTimestamp;

	public SimpleTicket() {
	}
	
	
	public SimpleTicket(String ticketId, String shortDesc, String statusId, String siteId, String siteName, String serviceTypeId, String serviceTypeName, Date createdTimestamp) {
		this.ticketId = ticketId;
		this.shortDesc = shortDesc;
		this.statusId = statusId;
		this.siteId = siteId;
		this.siteName = siteName;
		this.serviceTypeId = serviceTypeId;
		this.serviceTypeName = serviceTypeName;
		this.createdTimestamp = createdTimestamp;
	}


	public SimpleTicket(String ticketId, String statusId, String siteId, String siteName, String serviceTypeId, String serviceTypeName, Date createdTimestamp) {
		this.ticketId = ticketId;
		this.statusId = statusId;
		this.siteId = siteId;
		this.siteName = siteName;
		this.serviceTypeId = serviceTypeId;
		this.serviceTypeName = serviceTypeName;
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

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(String serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	
	//t.ticketId,t.statusId,t.siteId,s.name site,t.serviceTypeId, st.name serviceType, t.createdTimestamp
	
	
}
