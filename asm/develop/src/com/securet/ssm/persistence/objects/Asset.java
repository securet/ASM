package com.securet.ssm.persistence.objects;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@NamedQueries({
	@NamedQuery(name = "getAssetById", query = "SELECT o from Asset o where o.assetId=:id"),
	@NamedQuery(name = "getAssetByAssetTag", query = "SELECT o from Asset o where o.assetTag=:assetTag"),
	@NamedQuery(name = "getAssetByAssetIdAndTag", query = "SELECT o from Asset o where o.assetId=:assetId and o.assetTag=:assetTag"),
	@NamedQuery(name = "getAssetByAssetTagAndNotId", query = "SELECT o from Asset o where o.assetId<>:assetId and o.assetTag=:assetTag"),
	@NamedQuery(name = "getAssetForView", query = "SELECT o from Asset o")
})
@Table(
	name="Asset",
	uniqueConstraints=
		@UniqueConstraint(columnNames={"assetTag"})
)
public class Asset extends SecureTObject{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int assetId;
	
	@NotNull
	@Size(min=1,message="Name cannot be empty")
	private String name;

	@ManyToOne
	@JoinColumn(name="siteId",referencedColumnName="siteId")
	private Site site;
	
	@ManyToOne
	@JoinColumn(name="assetTypeId",referencedColumnName="assetTypeId")
	private AssetType assetType;
	
	@NotNull
	@Size(min=1,message="Name cannot be empty")
	private String assetTag; 

	@NotNull
	@JsonFormat(pattern="dd-MM-yyyy")
	@DateTimeFormat(pattern="dd-MM-yyyy")
	private Date installedDate;

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

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}

	public String getAssetTag() {
		return assetTag;
	}

	public void setAssetTag(String assetTag) {
		this.assetTag = assetTag;
	}

	public Date getInstalledDate() {
		return installedDate;
	}

	public void setInstalledDate(Date installedDate) {
		this.installedDate = installedDate;
	}

	
}

