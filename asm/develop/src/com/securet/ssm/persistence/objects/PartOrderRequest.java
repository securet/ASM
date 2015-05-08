package com.securet.ssm.persistence.objects;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="part_order_request")
public class PartOrderRequest extends SecureTObject{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int partOrderRequestId;
	
	@NotNull
	@OneToOne
	@JoinColumn(name="ticketId",referencedColumnName="ticketId")
	private Ticket ticket;
	
	@NotNull
	@OneToOne
	@JoinColumn(name="sparePartId",referencedColumnName="sparePartId")
	private ServiceSparePart serviceSparePart;
	
	@NotNull
	@OneToOne
	@JoinColumn(name="statusId",referencedColumnName="enumerationId")
	private Enumeration status;
	
	@OneToOne
	@JoinColumn(name="initiatedBy",referencedColumnName="userId")
	private User initiatedBy;
	

	@OneToOne
	@JoinColumn(name="respondedBy",referencedColumnName="userId")
	private User respondedBy;


	@NotNull
	private BigDecimal cost;
	
	public int getPartOrderRequestId() {
		return partOrderRequestId;
	}


	public void setPartOrderRequestId(int partOrderRequestId) {
		this.partOrderRequestId = partOrderRequestId;
	}


	public Ticket getTicket() {
		return ticket;
	}


	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}


	public ServiceSparePart getServiceSparePart() {
		return serviceSparePart;
	}


	public void setServiceSparePart(ServiceSparePart serviceSparePart) {
		this.serviceSparePart = serviceSparePart;
	}


	public Enumeration getStatus() {
		return status;
	}


	public void setStatus(Enumeration status) {
		this.status = status;
	}


	public User getInitiatedBy() {
		return initiatedBy;
	}


	public void setInitiatedBy(User initiatedBy) {
		this.initiatedBy = initiatedBy;
	}


	public User getRespondedBy() {
		return respondedBy;
	}


	public void setRespondedBy(User respondedBy) {
		this.respondedBy = respondedBy;
	}


	public BigDecimal getCost() {
		return cost;
	}


	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	
}
