package com.securet.ssm.persistence.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="ticket_archive")
public class TicketArchive extends SecureTObject{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ticketArchiveId;
	
	private String ticketId;
	
	private String ticketMasterId;
	
	@OneToOne
	@JoinColumn(name="reporterUserId",referencedColumnName="userId")
	private User reporter;

	@OneToOne
	@JoinColumn(name="resolverUserId",referencedColumnName="userId")
	private User resolver;
	
	@OneToOne
	@JoinColumn(name="modifiedBy",referencedColumnName="userId")
	private User modifiedBy;

	private String description;

	public int getTicketArchiveId() {
		return ticketArchiveId;
	}

	public void setTicketArchiveId(int ticketArchiveId) {
		this.ticketArchiveId = ticketArchiveId;
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getTicketMasterId() {
		return ticketMasterId;
	}

	public void setTicketMasterId(String ticketMasterId) {
		this.ticketMasterId = ticketMasterId;
	}

	public User getReporter() {
		return reporter;
	}

	public void setReporter(User reporter) {
		this.reporter = reporter;
	}

	public User getResolver() {
		return resolver;
	}

	public void setResolver(User resolver) {
		this.resolver = resolver;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
}
