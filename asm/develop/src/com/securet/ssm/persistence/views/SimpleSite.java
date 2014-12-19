package com.securet.ssm.persistence.views;

import com.securet.ssm.persistence.objects.Geo;

public class SimpleSite {

	private int siteId;
	private String name;
	private String area;
	private Geo city;
	private Geo state;
	private double latitude;
	private double longitude;
	private String organizationName;

	public SimpleSite(int siteId, String name, String area, Geo city, Geo state, double latitude, double longitude, String organizationName) {
		this.siteId = siteId;
		this.name = name;
		this.area = area;
		this.city = city;
		this.state = state;
		this.latitude = latitude;
		this.longitude = longitude;
		this.organizationName = organizationName;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Geo getCity() {
		return city;
	}

	public void setCity(Geo city) {
		this.city = city;
	}

	public Geo getState() {
		return state;
	}

	public void setState(Geo state) {
		this.state = state;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
}
