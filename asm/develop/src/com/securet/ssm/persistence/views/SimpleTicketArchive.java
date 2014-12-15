package com.securet.ssm.persistence.views;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SimpleTicketArchive{
	private String ticketId;
	private String description;
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss a",timezone="IST")
	private Date lastUpdatedTimestamp;
	private String modifiedByUser;
	private String vendorOrganization;
	public SimpleTicketArchive() {
		// TODO Auto-generated constructor stub
	}
	public SimpleTicketArchive(String ticketId, String description, Date lastUpdatedTimestamp, String modifiedByUser) {
		this.ticketId = ticketId;
		this.description = description;
		this.lastUpdatedTimestamp = lastUpdatedTimestamp;
		this.modifiedByUser = modifiedByUser;
//		this.vendorOrganization = vendorOrganization;
	}
	public SimpleTicketArchive(String ticketId, String description, Date lastUpdatedTimestamp, String modifiedByUser, String vendorOrganization) {
		this.ticketId = ticketId;
		this.description = description;
		this.lastUpdatedTimestamp = lastUpdatedTimestamp;
		this.modifiedByUser = modifiedByUser;
		this.vendorOrganization = vendorOrganization;
	}
	public String getTicketId() {
		return ticketId;
	}
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getLastUpdatedTimestamp() {
		return lastUpdatedTimestamp;
	}
	public void setLastUpdatedTimestamp(Date lastUpdatedTimestamp) {
		this.lastUpdatedTimestamp = lastUpdatedTimestamp;
	}
	public String getModifiedByUser() {
		return modifiedByUser;
	}
	public void setModifiedByUser(String modifiedByUser) {
		this.modifiedByUser = modifiedByUser;
	}
	public String getVendorOrganization() {
		return vendorOrganization;
	}
	public void setVendorOrganization(String vendorOrganization) {
		this.vendorOrganization = vendorOrganization;
	}
	
}

