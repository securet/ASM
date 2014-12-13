package com.securet.ssm.persistence.objects;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public class SecureTObject implements Serializable{

    public interface SimpleObject {};

	//default implementations for all objects..
	
	public SecureTObject() {
		this.createdTimestamp=new Timestamp(new Date().getTime());
		this.lastUpdatedTimestamp=new Timestamp(new Date().getTime());
	}
	
	@JsonIgnore
	private Timestamp createdTimestamp;
	@JsonIgnore
	private Timestamp lastUpdatedTimestamp;
	
	public Timestamp getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Timestamp createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	@PrePersist
	public void setCreateTimestamp(){
		setCreatedTimestamp(new Timestamp(new Date().getTime()));
		setLastUpdatedTimestamp(new Timestamp(new Date().getTime()));
	}
	

	public Timestamp getLastUpdatedTimestamp() {
		return lastUpdatedTimestamp;
	}
	
	@PreUpdate
	public void setLastUpdatedTimestamp(){
		setLastUpdatedTimestamp(new Timestamp(new Date().getTime()));
	}

	public void setLastUpdatedTimestamp(Timestamp lastUpdatedTimestamp) {
		this.lastUpdatedTimestamp = lastUpdatedTimestamp;
	}
	
}
