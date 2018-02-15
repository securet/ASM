package com.securet.ssm.persistence.objects;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(	name="service_spare_part",
	uniqueConstraints=@UniqueConstraint(columnNames={"vendorOrganizationId", "partName"})
)
public class ServiceSparePart extends SecureTObject{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int sparePartId;
	
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="vendorOrganizationId",referencedColumnName="organizationId")
	private Organization vendorOrganization;
	
	@NotNull
	@Size(min=1,message="Part Name cannot be empty")
	private String partName;
	
	@NotNull
	@Size(min=1,message="Part Description cannot be empty")
	private String partDescription;
	
	@NotNull(message="Cost has to be given")
	private BigDecimal cost;

	public int getSparePartId() {
		return sparePartId;
	}

	public void setSparePartId(int sparePartId) {
		this.sparePartId = sparePartId;
	}

	public Organization getVendorOrganization() {
		return vendorOrganization;
	}

	public void setVendorOrganization(Organization vendorOrganization) {
		this.vendorOrganization = vendorOrganization;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getPartDescription() {
		return partDescription;
	}

	public void setPartDescription(String partDescription) {
		this.partDescription = partDescription;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

}
