package com.securet.ssm.persistence.views;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SimpleAsset {

	private int assetId;
	private String name;
	private String assetTag; 
	private String assetType;
	private String siteName;
	@JsonFormat(pattern="dd-MM-yyyy")
	private Date installedDate;

	public SimpleAsset() {
		//Nothing here
	}
	
	public SimpleAsset(int assetId, String name, String assetTag, String assetType, String siteName, Date installedDate) {
		this.assetId = assetId;
		this.name = name;
		this.assetTag = assetTag;
		this.assetType = assetType;
		this.siteName = siteName;
		this.installedDate = installedDate;
	}

	public int getAssetId() {
		return assetId;
	}

	public void setAssetId(int assetId) {
		this.assetId = assetId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAssetTag() {
		return assetTag;
	}

	public void setAssetTag(String assetTag) {
		this.assetTag = assetTag;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Date getInstalledDate() {
		return installedDate;
	}

	public void setInstalledDate(Date installedDate) {
		this.installedDate = installedDate;
	}

	
}
