package com.securet.ssm.persistence.objects;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Ticket extends SecureTObject {

	@Id
	private String ticketId;

	@Column(unique=true)
	private String ticketMasterId;
	
	@OneToOne
	@JoinColumn(name="ticketType",referencedColumnName="enumerationId")
	private Enumeration ticketType;
	
	@OneToOne
	@JoinColumn(name="reporterUserId",referencedColumnName="userId")
	private User reporter;

	@OneToOne
	@JoinColumn(name="resolverUserId",referencedColumnName="userId")
	private User resolver;

	@NotNull(message="Please Select a Service Type")
	@OneToOne
	@JoinColumn(name="serviceTypeId",referencedColumnName="serviceTypeId")
	private ServiceType serviceType;
	
	@OneToOne
	@JoinColumn(name="issueTypeId",referencedColumnName="issueTypeId")
	private IssueType issueType; 

	@OneToOne
	@JoinColumn(name="statusId",referencedColumnName="enumerationId")
	private Enumeration status;
	
	private String shortDesc;
	
	@Size(min=1,message="Description cannot be empty")
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@OneToOne
	@JoinColumn(name="severity",referencedColumnName="enumerationId")
	private Enumeration severity;

	@OneToOne
	@JoinColumn(name="priority",referencedColumnName="enumerationId")
	private Enumeration priority;

	@OneToOne
	@JoinColumn(name="createdBy",referencedColumnName="userId")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="modifiedBy",referencedColumnName="userId")
	private User modifiedBy;

	@NotNull(message="Please Select a Site")
	@OneToOne
	@JoinColumn(name="siteId",referencedColumnName="siteId")
	private Site site;
	
	@OneToOne
	@JoinColumn(name="assetId",referencedColumnName="assetId")
	private Asset asset;
	
	@OneToMany
	@JoinColumn(name="ticketId",referencedColumnName="ticketId")
	private List<TicketExt> ticketExtensions;

	@JsonIgnore
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="ticketId",referencedColumnName="ticketId")
	private List<TicketAttachment> attachments;
	
	private double latitude;
	
	private double longitude;
	
	private String source;

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

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public IssueType getIssueType() {
		return issueType;
	}

	public void setIssueType(IssueType issueType) {
		this.issueType = issueType;
	}

	public Enumeration getStatus() {
		return status;
	}

	public void setStatus(Enumeration status) {
		this.status = status;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Enumeration getSeverity() {
		return severity;
	}

	public void setSeverity(Enumeration severity) {
		this.severity = severity;
	}

	public Enumeration getPriority() {
		return priority;
	}

	public void setPriority(Enumeration priority) {
		this.priority = priority;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}


	public List<TicketExt> getTicketExtensions() {
		return ticketExtensions;
	}

	public void setTicketExtensions(List<TicketExt> ticketExtensions) {
		this.ticketExtensions = ticketExtensions;
	}

	public List<TicketAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<TicketAttachment> attachments) {
		this.attachments = attachments;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Enumeration getTicketType() {
		return ticketType;
	}

	public void setTicketType(Enumeration ticketType) {
		this.ticketType = ticketType;
	}
}
