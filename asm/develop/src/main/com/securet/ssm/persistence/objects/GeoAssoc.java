package com.securet.ssm.persistence.objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="geo_assoc")
@NamedQueries({
	@NamedQuery(name="getRelatedGeos",query="select o.geoTo FROM GeoAssoc o WHERE o.geoFrom.geoId=:geoIdFrom")
})
public class GeoAssoc extends SecureTObject {

	@Id
	@OneToOne
	@JoinColumn(name="geoId",referencedColumnName="geoId")
	private Geo geoFrom;
	
	@Id
	@OneToOne
	@JoinColumn(name="geoIdTo",referencedColumnName="geoId")
	private Geo geoTo;
	
	private String assocType;

	public Geo getGeoFrom() {
		return geoFrom;
	}

	public void setGeoFrom(Geo geoFrom) {
		this.geoFrom = geoFrom;
	}

	public Geo getGeoTo() {
		return geoTo;
	}

	public void setGeoTo(Geo geoTo) {
		this.geoTo = geoTo;
	}

	public String getAssocType() {
		return assocType;
	}

	public void setAssocType(String assocType) {
		this.assocType = assocType;
	}
}
