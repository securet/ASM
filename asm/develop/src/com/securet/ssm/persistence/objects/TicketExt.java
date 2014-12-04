package com.securet.ssm.persistence.objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ticket_ext")
public class TicketExt extends SecureTObject {

	@Id
	private String ticketId;

	@Id
	private String ticketArchiveId;
	
	@Id
	private String fieldName;
	
	private String fieldValue;

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getTicketArchiveId() {
		return ticketArchiveId;
	}

	public void setTicketArchiveId(String ticketArchiveId) {
		this.ticketArchiveId = ticketArchiveId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
}