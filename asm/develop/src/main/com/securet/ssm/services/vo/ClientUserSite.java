package com.securet.ssm.services.vo;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class ClientUserSite {

	
	private Integer organizationId; 
	
	@NotNull(message="Please select a user")
	@Size(min=1,message="Please select a user")
	private String userId;
	
	private String cityGeoId;
	
	private boolean replicate;
	
	@NotNull(message="Please select atleast one site")
	//@Size(min=1,message="Please select atleast one site")
	private List<Integer> sites;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<Integer> getSites() {
		return sites;
	}

	public void setSites(List<Integer> sites) {
		this.sites = sites;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getCityGeoId() {
		return cityGeoId;
	}

	public void setCityGeoId(String cityGeoId) {
		this.cityGeoId = cityGeoId;
	}

	public boolean isReplicate() {
		return replicate;
	}

	public void setReplicate(boolean replicate) {
		this.replicate = replicate;
	}
	
}
