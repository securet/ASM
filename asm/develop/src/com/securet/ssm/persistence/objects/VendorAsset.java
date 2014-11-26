package com.securet.ssm.persistence.objects;

import java.util.List;

import javax.persistence.Entity;

@Entity
public class VendorAsset extends SecureTObject{

	private User vendorUser;
	
	private List<ServiceType> serviceTypes;
	
	private List<Asset> assets;

	public User getVendorUser() {
		return vendorUser;
	}

	public void setVendorUser(User vendorUser) {
		this.vendorUser = vendorUser;
	}

	public List<ServiceType> getServiceTypes() {
		return serviceTypes;
	}

	public void setServiceTypes(List<ServiceType> serviceTypes) {
		this.serviceTypes = serviceTypes;
	}

	public List<Asset> getAssets() {
		return assets;
	}

	public void setAssets(List<Asset> assets) {
		this.assets = assets;
	}
	

}
