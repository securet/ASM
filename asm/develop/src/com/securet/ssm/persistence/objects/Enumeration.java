package com.securet.ssm.persistence.objects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Enumeration extends SecureTObject {

	@Id
	private String enumerationId;
	private String enumTypeId;
	private String enumDescription;
	
	public String getEnumerationId() {
		return enumerationId;
	}
	public void setEnumerationId(String enumerationId) {
		this.enumerationId = enumerationId;
	}
	public String getEnumTypeId() {
		return enumTypeId;
	}
	public void setEnumTypeId(String enumTypeId) {
		this.enumTypeId = enumTypeId;
	}
	public String getEnumDescription() {
		return enumDescription;
	}
	public void setEnumDescription(String enumDescription) {
		this.enumDescription = enumDescription;
	}
	
	
	
}
