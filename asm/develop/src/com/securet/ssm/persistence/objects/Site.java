package com.securet.ssm.persistence.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="site")
@NamedQueries({
	@NamedQuery(name = "getSite.count", query = "SELECT COUNT(siteId) from Site"),
	@NamedQuery(name = "getSiteById", query = "SELECT o from Site o where o.siteId=:id"),
	@NamedQuery(name = "getSiteByName", query = "SELECT o from Site o where o.name=:name"),
	@NamedQuery(name = "getSiteForView", query = "SELECT o from Site o"),
	@NamedQuery(name = "getCityWithSitesForView", query = "SELECT distinct o.city from Site o JOIN o.city city"),
	@NamedQuery(name = "getSitesForCity", query = "SELECT NEW com.securet.ssm.persistence.views.SimpleSite(o.siteId, o.name, o.area, o.city, o.state, o.latitude, o.longitude, o.organization.name) from Site o JOIN o.city city WHERE city.geoId=:cityGeoId"),
	@NamedQuery(name = "searchSites", query = "SELECT NEW  com.securet.ssm.persistence.views.SimpleSite(o.siteId, o.name) from Site o where o.name like :siteName"),
	@NamedQuery(name = "getMappedCircles", query = "SELECT DISTINCT o.circle from Site o JOIN o.circle circle")
})
public class Site extends SecureTObject{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int siteId;
	
	@NotNull
	@Size(min=1,message="Name cannot be empty")
	private String name;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="organizationId",referencedColumnName="organizationId")
	private Organization organization;
	
	private String area;
	
	@ManyToOne
	@JoinColumn(name="state",referencedColumnName="geoId")
	private Geo state;

	@ManyToOne
	@JoinColumn(name="city",referencedColumnName="geoId")
	private Geo city;

	@ManyToOne
	@JoinColumn(name="circle",referencedColumnName="geoId")
	private Geo circle;
	
	@ManyToOne
	@JoinColumn(name="moduleId",referencedColumnName="moduleId")
	private Module module;
	

	private double latitude;
	private double longitude;
	private String siteType;
	private String comments;

	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
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
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getName() {
		return name;
	}
	public void setName(String siteName) {
		this.name = siteName;
	}

	public Geo getCircle() {
		return circle;
	}
	public void setCircle(Geo circle) {
		this.circle = circle;
	}
	public Module getModule() {
		return module;
	}
	public void setModule(Module module) {
		this.module = module;
	}

	@Override
	public String toString() {
		return "Site [siteId=" + siteId + ", organization=" + organization + ", area=" + area + ", city=" + city + ", state=" + state + ", latitude=" + latitude + ", longitude=" + longitude + ", siteType=" + siteType + ", comments=" + comments + "]";
	}
	
}
