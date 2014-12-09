package com.securet.ssm.persistence.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="geo")
@NamedQueries({
		@NamedQuery(name = "getStateForView", query = "SELECT o from Geo o where o.geoType=1"),
		@NamedQuery(name = "getCityForView", query = "SELECT o from Geo o where o.geoType=2"),
})
public class Geo extends SecureTObject {
	public enum GeoType {
		COUNTRY,STATE,CITY,REGION
	}

	@Id
	private String geoId;
	
	@Column(name="geoName")
	private String name;
	
	private GeoType geoType;

	public String getGeoId() {
		return geoId;
	}

	public void setGeoId(String geoId) {
		this.geoId = geoId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GeoType getGeoType() {
		return geoType;
	}

	public void setGeoType(GeoType geoType) {
		this.geoType = geoType;
	}
	
	public static void main(String[] args) {
		System.out.println(GeoType.STATE.ordinal());
	}
}
