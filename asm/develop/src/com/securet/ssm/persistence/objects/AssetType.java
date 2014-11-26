package com.securet.ssm.persistence.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity()
@Table(name="asset_type")
@NamedQueries({
	@NamedQuery(name = "getAssetType.count", query = "SELECT COUNT(assetTypeId) from AssetType"),
	@NamedQuery(name = "getAssetTypeById", query = "SELECT o from AssetType o where o.assetTypeId=:id"),
	@NamedQuery(name = "getAssetTypeForView", query = "SELECT o from AssetType o")
})

public class AssetType extends SecureTObject {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int assetTypeId;
	
	@NotNull
	@Size(min=1,message="Name cannot be empty")
	private String name;

	public int getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(int assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
}
