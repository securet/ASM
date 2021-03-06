package com.securet.ssm.persistence.objects;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.securet.ssm.persistence.objects.SecureTObject.SimpleObject;

@Entity
@Table(name="ticket_archive")
@NamedQueries({
	@NamedQuery(name="getLatestTicketArchivesForTicketId",query="SELECT o from TicketArchive o WHERE o.ticketId=:ticketId order by o.lastUpdatedTimestamp desc"),
	@NamedQuery(name="getLatestSimpleTicketArchivesForTicketId",query="SELECT NEW com.securet.ssm.persistence.views.SimpleTicketArchive(o.ticketId,o.description,o.status.enumerationId,o.lastUpdatedTimestamp,o.modifiedBy.userId as modifiedByUser,o.modifiedBy.organization.name) from TicketArchive o JOIN o.modifiedBy mu JOIN mu.organization org WHERE o.ticketId=:ticketId order by o.lastUpdatedTimestamp desc")
})
public class TicketArchive{

    
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss a",timezone="IST")
    @JsonView(SimpleObject.class)
	private Timestamp createdTimestamp;
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss a",timezone="IST")
    @JsonView(SimpleObject.class)
	private Timestamp lastUpdatedTimestamp;
	
    
    public TicketArchive() {
		//No argument constructor for JPA
	}

	public TicketArchive(Ticket ticket){
		this.ticketId=ticket.getTicketId();
		this.ticketMasterId=ticket.getTicketId();
		this.modifiedBy=ticket.getModifiedBy();
		this.description=ticket.getDescription();
		this.modifiedBy=ticket.getModifiedBy();
		this.reporter=ticket.getReporter();
		this.resolver=ticket.getResolver();
		this.status=ticket.getStatus();
		this.setCreatedTimestamp(new Timestamp(ticket.getCreatedTimestamp().getTime()));
		this.setLastUpdatedTimestamp(new Timestamp(ticket.getLastUpdatedTimestamp().getTime()));
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ticketArchiveId;
	
    @JsonView(SimpleObject.class)
	private String ticketId;
	
	private String ticketMasterId;
	
	@OneToOne
	@JoinColumn(name="reporterUserId",referencedColumnName="userId")
	private User reporter;

	@OneToOne
	@JoinColumn(name="statusId",referencedColumnName="enumerationId")
	private Enumeration status;

	@OneToOne
	@JoinColumn(name="resolverUserId",referencedColumnName="userId")
	private User resolver;
	
	@OneToOne
	@JoinColumn(name="modifiedBy",referencedColumnName="userId")
	private User modifiedBy;

    @JsonView(SimpleObject.class)
	@Column(columnDefinition = "TEXT")
	private String description;

    private Integer relatedArchiveId;
    
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
	
	public Timestamp getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Timestamp createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

/*	@PrePersist
	public void setCreateTimestamp(){
		setCreatedTimestamp(new Timestamp(new Date().getTime()));
		setLastUpdatedTimestamp(new Timestamp(new Date().getTime()));
	}
*/	

	public Timestamp getLastUpdatedTimestamp() {
		return lastUpdatedTimestamp;
	}
	
/*	@PreUpdate
	public void setLastUpdatedTimestamp(){
		setLastUpdatedTimestamp(new Timestamp(new Date().getTime()));
	}
*/
	public void setLastUpdatedTimestamp(Timestamp lastUpdatedTimestamp) {
		this.lastUpdatedTimestamp = lastUpdatedTimestamp;
	}

	public Enumeration getStatus() {
		return status;
	}

	public void setStatus(Enumeration status) {
		this.status = status;
	}

	public int getRelatedArchiveId() {
		return relatedArchiveId;
	}

	public void setRelatedArchiveId(int relatedArchiveId) {
		this.relatedArchiveId = relatedArchiveId;
	}
}
