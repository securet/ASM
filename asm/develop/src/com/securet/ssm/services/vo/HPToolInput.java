package com.securet.ssm.services.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class HPToolInput {
	
	private String terminalID;
	private String circle;
	private String region;
	private String fault;//IssueType
	private String location;
	
	//@DateTimeFormat(pattern="M/d/yyyy HH:mm:ss a")
	private String startedAt;
	
	private String endedAt;
	
	private long duration;// (minutes)

	
	public String getTerminalID() {
		return terminalID;
	}
	public void setTerminalID(String terminalID) {
		this.terminalID = terminalID;
	}
	public String getCircle() {
		return circle;
	}
	public void setCircle(String circle) {
		this.circle = circle;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getFault() {
		return fault;
	}
	public void setFault(String fault) {
		this.fault = fault;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getStartedAt() {
		return startedAt;
	}
	public void setStartedAt(String startedAt) {
		this.startedAt = startedAt;
	}
	public String getEndedAt() {
		return endedAt;
	}
	public void setEndedAt(String endedAt) {
		this.endedAt = endedAt;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
}