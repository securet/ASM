package com.securet.ssm.persistence.objects;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.securet.ssm.persistence.views.SimpleTicket;

@Entity
@Table(name="ticket")
@NamedQueries({
	@NamedQuery(name="getTicketStatusForId",query="SELECT t.status.enumerationId FROM Ticket t WHERE t.ticketId=:ticketId"),
	@NamedQuery(name="getTicketCountByStatus",query="SELECT t.status.enumerationId,COUNT(t.ticketId)  FROM Ticket t GROUP BY t.status.enumerationId"),
	@NamedQuery(name="getTicketCountByServiceTypeAndStatus",query="SELECT t.serviceType.serviceTypeId,t.status.enumerationId,t.serviceType.name,COUNT(t.ticketId)  FROM Ticket t GROUP BY t.serviceType.serviceTypeId,t.status.enumerationId")
})
@NamedNativeQueries({
	@NamedNativeQuery(name="getTicketCountByVendorUser",query="SELECT vsa.userId,COUNT(t.ticketId)  FROM ticket t INNER JOIN vendor_service_asset vsa ON vsa.serviceTypeId=t.serviceTypeId AND vsa.assetId=t.assetId AND t.statusId='OPEN' GROUP BY vsa.userId"),
	@NamedNativeQuery(name="getTicketCountByClientUser",query="SELECT t.reporterUserId,COUNT(t.ticketId)  FROM ticket t GROUP BY t.reporterUserId"),
	@NamedNativeQuery(
			  name="getTicketByDateRange",
			  query="SELECT t.ticketId,s.name siteName,t.reporterUserId,st.name serviceType,it.name issueType,t.resolverUserId,status.enumDescription status,t.description,t.createdTimestamp,t.lastUpdatedTimestamp,sv.enumDescription severity,t.latitude,t.longitude FROM ticket t INNER JOIN site s on t.siteId=s.siteId INNER JOIN service_type st on t.serviceTypeId=st.serviceTypeId INNER JOIN issue_type it on t.issueTypeId=it.issueTypeId INNER JOIN enumeration status on t.statusId=status.enumerationId INNER JOIN enumeration sv on t.statusId=sv.enumerationId WHERE t.createdTimestamp BETWEEN (?1) AND (?2) ORDER BY t.lastUpdatedTimestamp DESC"
			),
	@NamedNativeQuery(
			  name="getClientUserTickets",
			  query="SELECT t.ticketId,t.shortDesc,t.statusId,t.siteId,s.name siteName,t.serviceTypeId, st.name serviceTypeName, t.createdTimestamp  from ticket t  INNER JOIN service_type st ON t.serviceTypeId=st.serviceTypeId INNER JOIN site s ON t.siteId=s.siteId  INNER JOIN client_user_site cus ON t.siteId=cus.siteId  LEFT JOIN issue_type it ON t.issueTypeId=it.issueTypeId  where ( t.statusId IN (?2) and  cus.userId=(?1) ) GROUP BY t.ticketId  ORDER BY t.lastUpdatedTimestamp desc",
			  resultSetMapping="simpleTickets"
			),
	@NamedNativeQuery(
			  name="getVendorUserTickets",
			  query="SELECT t.ticketId,t.shortDesc,t.statusId,t.siteId,s.name siteName,t.serviceTypeId, st.name serviceTypeName, t.createdTimestamp  from ticket t  INNER JOIN service_type st ON t.serviceTypeId=st.serviceTypeId INNER JOIN site s ON t.siteId=s.siteId  INNER JOIN vendor_service_asset vsa ON t.assetId=vsa.assetId AND t.serviceTypeId=vsa.serviceTypeId LEFT JOIN issue_type it ON t.issueTypeId=it.issueTypeId  where ( t.ticketType!='LOG' AND vsa.userId=(?1) AND t.statusId IN (?2) ) GROUP BY t.ticketId  ORDER BY t.lastUpdatedTimestamp desc",
			  resultSetMapping="simpleTickets"
			),
	@NamedNativeQuery(
			  name="getFilteredClientUserTickets",
			  query="SELECT t.ticketId,t.shortDesc,t.statusId,t.siteId,s.name siteName,t.serviceTypeId, st.name serviceTypeName, t.createdTimestamp  from ticket t  INNER JOIN service_type st ON t.serviceTypeId=st.serviceTypeId INNER JOIN site s ON t.siteId=s.siteId  INNER JOIN client_user_site cus ON t.siteId=cus.siteId  LEFT JOIN issue_type it ON t.issueTypeId=it.issueTypeId  where ( t.statusId IN (?2) AND  cus.userId=(?1) ) AND (t.statusId like (?3) OR t.Description like (?4) OR  st.name like (?5) OR it.name like (?6) OR t.ticketType like (?7) ) GROUP BY t.ticketId  ORDER BY t.lastUpdatedTimestamp desc",
			  resultSetMapping="simpleTickets"
			),
	@NamedNativeQuery(
			  name="getFilteredVendorUserTickets",
			  query="SELECT t.ticketId,t.shortDesc,t.statusId,t.siteId,s.name siteName,t.serviceTypeId, st.name serviceTypeName, t.createdTimestamp  from ticket t  INNER JOIN service_type st ON t.serviceTypeId=st.serviceTypeId INNER JOIN site s ON t.siteId=s.siteId  INNER JOIN vendor_service_asset vsa ON t.assetId=vsa.assetId AND t.serviceTypeId=vsa.serviceTypeId LEFT JOIN issue_type it ON t.issueTypeId=it.issueTypeId  where ( t.ticketType!='LOG' AND vsa.userId=(?1) AND t.statusId IN (?2) ) AND (t.statusId like (?3) OR t.Description like (?4) OR  st.name like (?5) OR it.name like (?6) OR t.ticketType like (?7) ) GROUP BY t.ticketId  ORDER BY t.lastUpdatedTimestamp desc",
			  resultSetMapping="simpleTickets"
			)
})
@SqlResultSetMapping(name="simpleTickets", 
  classes={ 
    @ConstructorResult(targetClass=SimpleTicket.class, columns={
        @ColumnResult(name="ticketId", type=String.class),
        @ColumnResult(name="shortDesc", type=String.class),
        @ColumnResult(name="statusId", type=String.class),
        @ColumnResult(name="siteId", type=String.class),
        @ColumnResult(name="siteName", type=String.class),
        @ColumnResult(name="serviceTypeId", type=String.class),
        @ColumnResult(name="serviceTypeName", type=String.class),
        @ColumnResult(name="createdTimestamp", type=Date.class),
    })
  }
)
public class Ticket{

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
	
	@NotNull(message="Description cannot be empty")
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

	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="ticketId",referencedColumnName="ticketId")
	private List<TicketAttachment> attachments;
	
	private double latitude;
	
	private double longitude;
	
	private String source;

	@Transient
	private transient boolean autoUpdateTimeFields=true;
	
	@Transient
	private transient int actualTat;
	@Transient
	private transient int tat;
	@Transient
	private transient int stopClock;

	
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss a",timezone="IST")
	private Timestamp createdTimestamp;
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss a",timezone="IST")
	private Timestamp lastUpdatedTimestamp;

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

	public Timestamp getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Timestamp createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	@PrePersist
	public void setCreateTimestamp(){
		if(autoUpdateTimeFields){
			setCreatedTimestamp(new Timestamp(new Date().getTime()));
			setLastUpdatedTimestamp(new Timestamp(new Date().getTime()));
		}
	}
	

	public Timestamp getLastUpdatedTimestamp() {
		return lastUpdatedTimestamp;
	}
	
	@PreUpdate
	public void setLastUpdatedTimestamp(){
		if(autoUpdateTimeFields){
			setLastUpdatedTimestamp(new Timestamp(new Date().getTime()));
		}
	}

	public void setLastUpdatedTimestamp(Timestamp lastUpdatedTimestamp) {
		this.lastUpdatedTimestamp = lastUpdatedTimestamp;
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

	public boolean isAutoUpdateTimeFields() {
		return autoUpdateTimeFields;
	}

	public void setAutoUpdateTimeFields(boolean autoUpdateTimeFields) {
		this.autoUpdateTimeFields = autoUpdateTimeFields;
	}

}
