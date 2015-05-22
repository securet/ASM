package com.securet.ssm.persistence.objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="enumeration")
@NamedQueries({
	@NamedQuery(name = "getTicketType", query = "SELECT o.enumerationId,o.enumDescription FROM Enumeration o WHERE o.enumTypeId='TICKET_TYPE'"),
	@NamedQuery(name = "getSiteTypeForView", query = "SELECT o.enumerationId as siteTypeId,o.enumDescription as name FROM Enumeration o WHERE o.enumTypeId='SITE_TYPE'"),
	@NamedQuery(name = "getSeverityForView", query = "SELECT o.enumerationId ,o.enumDescription  FROM Enumeration o WHERE o.enumTypeId='SEVERITY'"),
	@NamedQuery(name = "getTicketStatusForView", query = "SELECT o.enumerationId ,o.enumDescription  FROM Enumeration o WHERE o.enumTypeId='TICKET_STATUS'"),
	@NamedQuery(name = "getEnumByType", query = "SELECT o  FROM Enumeration o WHERE o.enumTypeId=:enumTypeId")
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
