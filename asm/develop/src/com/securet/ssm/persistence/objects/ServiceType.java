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

@Entity()
@Table(name="service_type")
@NamedQueries({
	@NamedQuery(name = "getServiceType.count", query = "SELECT COUNT(serviceTypeId) from ServiceType"),
	@NamedQuery(name = "getServiceTypeById", query = "SELECT o from ServiceType o where o.serviceTypeId=:id"),
	@NamedQuery(name = "getServiceTypeForView", query = "SELECT o from ServiceType o")
})
public class ServiceType extends SecureTObject{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Min(value=0,message="Invalid Service Type")
	private int serviceTypeId;
	
	@NotNull
	@Size(min=1,message="Name cannot be empty")
	private String name;

	public int getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(int serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
