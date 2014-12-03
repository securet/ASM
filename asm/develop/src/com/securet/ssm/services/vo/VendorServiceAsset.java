package com.securet.ssm.services.vo;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class VendorServiceAsset {

	private Integer organizationId;
	
	@NotNull(message="Please select a user")
	@Size(min=1,message="Please select a user")
	private String userId;
	
	@NotNull(message="Please select a service type")
	@Min(value=1,message="Please select a service type")
	private Integer serviceTypeId;
	
	@NotNull(message="Please select assets")
	@Size(min=1,message="Please select assets")
	private List<Integer> assets;

	private String cityGeoId;
	
	private String siteId;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Integer serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public List<Integer> getAssets() {
		return assets;
	}

	public void setAssets(List<Integer> assets) {
		this.assets = assets;
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

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	
}
