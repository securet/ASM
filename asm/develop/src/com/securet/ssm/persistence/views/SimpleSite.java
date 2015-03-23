package com.securet.ssm.persistence.views;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.securet.ssm.persistence.objects.Geo;


public class SimpleSite {

	private int siteId;
	private String name;
	@JsonInclude(Include.NON_NULL)
	private String area;
	@JsonInclude(Include.NON_NULL)
	private Geo city;
	@JsonInclude(Include.NON_NULL)
	private Geo state;
	@JsonInclude(Include.NON_NULL)
	private double latitude;
	@JsonInclude(Include.NON_NULL)
	private double longitude;
	@JsonInclude(Include.NON_NULL)
	private String organizationName;
	@JsonInclude(Include.NON_NULL)
	private String circle;
	@JsonInclude(Include.NON_NULL)
	private String module;

	public SimpleSite() {
		//Nothing here
	}
	
	public SimpleSite (int siteId,String name){
		this.siteId=siteId;
		this.name=name;
	}
	
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

	public SimpleSite(int siteId, String name, String area,String cityGeoId,  String city,String stateGeoId, String state, double latitude, double longitude, String organizationName) {
		this.siteId = siteId;
		this.name = name;
		this.area = area;
		
		Geo cityGeo = new Geo();
		cityGeo.setGeoId(cityGeoId);
		cityGeo.setName(city);
		this.city = cityGeo;

		Geo stateGeo = new Geo();
		stateGeo.setGeoId(stateGeoId);
		stateGeo.setName(state);
		this.state = stateGeo;

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

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
}
