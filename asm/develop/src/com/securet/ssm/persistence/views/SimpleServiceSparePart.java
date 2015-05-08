package com.securet.ssm.persistence.views;

import java.math.BigDecimal;

public class SimpleServiceSparePart {

	private int sparePartId;
	
	private String partName;
	
	private String partDescription;
	
	private BigDecimal cost;

	public int getSparePartId() {
		return sparePartId;
	}

	public void setSparePartId(int sparePartId) {
		this.sparePartId = sparePartId;
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
