package com.securet.ssm.persistence.objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.mysema.query.annotations.QueryInit;

@Entity
@Table(name="vendor_service_asset")
@NamedQueries({
	@NamedQuery(name="getVendorServiceAssetByServiceType",query="SELECT o FROM VendorServiceAsset o WHERE o.serviceType.serviceTypeId=:serviceTypeId AND o.asset.site.siteId=:siteId"),
	@NamedQuery(name="getVendorByServiceType",query="SELECT o.vendorUser FROM VendorServiceAsset o WHERE o.serviceType.serviceTypeId=:serviceTypeId AND o.asset.site.siteId=:siteId"),
	@NamedQuery(name="getAssetNameMappedByAssetServiceType",query="SELECT distinct o.asset.name FROM VendorServiceAsset o WHERE o.serviceType.serviceTypeId=:serviceTypeId AND o.asset.assetId IN (:assetId)"),
	@NamedQuery(name="getAssetNameMappedByVendorAssetServiceType",query="SELECT distinct o.asset.assetTag FROM VendorServiceAsset o WHERE o.serviceType.serviceTypeId=:serviceTypeId AND o.asset.assetId IN (:assetId) AND o.vendorUser.userId<>:userId")
})
public class VendorServiceAsset extends SecureTObject{

	@Id
	@ManyToOne
	@JoinColumn(name="userId",referencedColumnName="userId")
	private User vendorUser;
	
	@Id
	@ManyToOne
	@JoinColumn(name="serviceTypeId",referencedColumnName="serviceTypeId")
	private ServiceType serviceType;
	
	@Id
	@ManyToOne
	@JoinColumn(name="assetId",referencedColumnName="assetId")
	@QueryInit("site.*")
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
