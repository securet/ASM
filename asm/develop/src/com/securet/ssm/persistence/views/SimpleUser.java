package com.securet.ssm.persistence.views;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SimpleUser {

	private String userId;
	@JsonInclude(Include.NON_NULL)
	private String fullName;
	@JsonInclude(Include.NON_NULL)
	private String emailId;
	@JsonInclude(Include.NON_NULL)
	private String mobile;
	@JsonInclude(Include.NON_NULL)
	private String organizationName;
	
	public SimpleUser() {
		//Nothing here
	}
	
	public SimpleUser(String userId, String fullName, String emailId, String mobile, String organizationName) {
		this.userId = userId;
		this.fullName = fullName;
		this.emailId = emailId;
		this.mobile = mobile;
		this.organizationName = organizationName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	
	
}
