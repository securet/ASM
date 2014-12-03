package com.securet.ssm.persistence.objects;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="vendor_service_asset")
public class VendorServiceAsset extends SecureTObject{

	@Id
	@OneToOne
	@JoinColumn(name="userId",referencedColumnName="userId")
	private User vendorUser;
	
	@Id
	@OneToOne
	@JoinColumn(name="serviceTypeId",referencedColumnName="serviceTypeId")
	private ServiceType serviceType;
	
	@Id
	@OneToOne
	@JoinColumn(name="assetId",referencedColumnName="assetId")
	private Asset asset;

	public User getVendorUser() {
		return vendorUser;
	}

	public void setVendorUser(User vendorUser) {
		this.vendorUser = vendorUser;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAssets(Asset asset) {
		this.asset = asset;
	}

	@Override
	public boolean equals(Object obj) {
		VendorServiceAsset vendorServiceAsset = (VendorServiceAsset)obj;
		if(checkObjectNotNull(vendorServiceAsset) && checkObjectNotNull(this)){
			if(this.getAsset().getAssetId()==vendorServiceAsset.getAsset().getAssetId() && this.getVendorUser().getUserId().equals(vendorServiceAsset.getVendorUser().getUserId()) && this.getServiceType().getServiceTypeId()== vendorServiceAsset.getServiceType().getServiceTypeId()){
				return true;
			}
		}
		
		return false;
	}

	private boolean checkObjectNotNull(VendorServiceAsset vendorServiceAsset) {
		return vendorServiceAsset.getAsset()!=null && vendorServiceAsset.getServiceType()!=null && vendorServiceAsset.getVendorUser()!=null;
	}
	
	
	
}
