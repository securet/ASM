package com.securet.ssm.persistence.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="organization")
@NamedQueries({
	@NamedQuery(name = "getOrganization.count", query = "SELECT COUNT(organizationId) from Organization"),
	@NamedQuery(name = "getOrganizationById", query = "SELECT o from Organization o where o.organizationId=:id"),
	@NamedQuery(name = "getOrganizationForView", query = "SELECT o from Organization o"),
	@NamedQuery(name = "getClientOrganizationForView", query = "SELECT o from Organization o WHERE o.organizationType='CLIENT'"),
	@NamedQuery(name = "getVendorOrganizationForView", query = "SELECT o from Organization o WHERE o.organizationType='VENDOR'")
})
public class Organization extends SecureTObject{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Min(value=0,message="Invalid Organization")
	private int organizationId;
	
	@NotNull
	@Size(min=1,message="Name cannot be empty")
	private String name;

	private String organizationType;
	private String logo;
	private String shortDesc;

	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrganizationType() {
		return organizationType;
	}
	public void setOrganizationType(String type) {
		this.organizationType = type;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	
	@Override
	public String toString() {
		return "Organization [organizationId=" + organizationId + ", name=" + name + ", type=" + organizationType + ", logo=" + logo + ", shortDesc=" + shortDesc + "]";
	}	
	
}
