package com.securet.ssm.persistence.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@NamedQueries({
	@NamedQuery(name = "getOrganization.count", query = "SELECT COUNT(organizationId) from Organization"),
	@NamedQuery(name = "getOrganizationById", query = "SELECT o from Organization o where o.organizationId=:id"),
	@NamedQuery(name = "getOrganizationForView", query = "SELECT o from Organization o"),
})
public class Organization extends SecureTObject{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Min(value=0,message="Invalid Organization")
	private int organizationId;
	
	@NotNull
	@Size(min=1,message="Name cannot be empty")
	private String name;
	@NotNull
	@Size(min=1,message="Type cannot be empty")
	private String type;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
		return "Organization [organizationId=" + organizationId + ", name=" + name + ", type=" + type + ", logo=" + logo + ", shortDesc=" + shortDesc + "]";
	}	
	
}
