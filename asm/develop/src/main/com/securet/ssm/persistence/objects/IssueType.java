package com.securet.ssm.persistence.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity()
@Table(name="issue_type")
@NamedQueries({
	@NamedQuery(name = "getIssueType.count", query = "SELECT COUNT(issueTypeId) from IssueType"),
	@NamedQuery(name = "getIssueTypeById", query = "SELECT o from IssueType o where o.issueTypeId=:id"),
	@NamedQuery(name = "getIssueTypeForService", query = "SELECT o from IssueType o where o.serviceType.serviceTypeId=:serviceTypeId"),
	@NamedQuery(name = "getIssueTypeForView", query = "SELECT o from IssueType o")
})
public class IssueType extends SecureTObject{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int issueTypeId;
	
	@NotNull
	@Size(min=1,message="Name cannot be empty")
	private String name;

	@ManyToOne
	@JoinColumn(name="serviceTypeId",referencedColumnName="serviceTypeId")
	private ServiceType serviceType;

	public int getIssueTypeId() {
		return issueTypeId;
	}

	public void setIssueTypeId(int issueTypeId) {
		this.issueTypeId = issueTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}
	
	
	
}
