package com.securet.ssm.persistence.views;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SimplePartOrderRequest {

	private int partOrderRequestId;

	private SimpleServiceSparePart serviceSparePart; 	

	private SimpleUser initiatedBy;
	
	private SimpleUser respondedBy;
	
	private BigDecimal cost;
	
	private String ticketId;
	
	private String statusId;
	
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss a",timezone="IST")
	private Timestamp createdTimestamp;
	
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss a",timezone="IST")
	private Timestamp lastUpdatedTimestamp;

	public int getPartOrderRequestId() {
		return partOrderRequestId;
	}

	public void setPartOrderRequestId(int partOrderRequestId) {
		this.partOrderRequestId = partOrderRequestId;
	}

	public SimpleServiceSparePart getServiceSparePart() {
		return serviceSparePart;
	}

	public void setServiceSparePart(SimpleServiceSparePart serviceSparePart) {
		this.serviceSparePart = serviceSparePart;
	}

	public SimpleUser getInitiatedBy() {
		return initiatedBy;
	}

	public void setInitiatedBy(SimpleUser initiatedBy) {
		this.initiatedBy = initiatedBy;
	}

	public SimpleUser getRespondedBy() {
		return respondedBy;
	}

	public void setRespondedBy(SimpleUser respondedBy) {
		this.respondedBy = respondedBy;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public String getTicketId() {
		return ticketId;
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

	public Timestamp getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Timestamp createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public Timestamp getLastUpdatedTimestamp() {
		return lastUpdatedTimestamp;
	}

	public void setLastUpdatedTimestamp(Timestamp lastUpdatedTimestamp) {
		this.lastUpdatedTimestamp = lastUpdatedTimestamp;
	}
}
