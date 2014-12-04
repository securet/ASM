package com.securet.ssm.persistence.objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name = "getSiteTypeForView", query = "SELECT o.enumerationId as siteTypeId,o.enumDescription as name FROM Enumeration o WHERE o.enumTypeId='SITE_TYPE'")
})
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
