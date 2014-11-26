package com.securet.ssm.persistence.objects;

import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

@Entity
@NamedQueries({
	@NamedQuery(name = "getUserById", query = "SELECT o from User o where o.userId=:id"),
})
public class User extends SecureTObject {

	@Id
	@Size(min=1,message="UserId cannot be empty")
	private String userId;

	@NotNull
	@Size(min=1,message="Name cannot be empty")
	private String fullName;
	
	@NotNull
	@Size(min=1,message="Email cannot be empty")
	@Email(message="Invalid Email Id")
	private String emailId;
	
	@NotNull
	@Size(min=1,message="Mobile no cannot be empty")
	private String mobile;
	private boolean enableNotifications;
	
	@ManyToOne
	@JoinColumn(name="organizationId",referencedColumnName="organizationId")
	private Organization organization;

	@OneToMany(fetch=FetchType.EAGER)
	@JoinTable(name="user_role")
	private List<RoleType> roles;
	
	@OneToMany
	@JoinTable(name="user_permission")
	private List<Permission> permissions;
	
	@OneToOne(cascade=CascadeType.ALL,mappedBy="user")
	@JoinColumn(name="userId",referencedColumnName="userId")
	private UserLogin userLogin;
	
	@Transient
	private transient List<String> rolesList;

	@Transient
	private transient String activeRoles;

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
	public boolean isEnableNotifications() {
		return enableNotifications;
	}
	public void setEnableNotifications(boolean enableNotifications) {
		this.enableNotifications = enableNotifications;
	}
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	public List<RoleType> getRoles() {
		return roles;
	}
	public void setRoles(List<RoleType> roles) {
		this.roles = roles;
	}
	public List<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
	public UserLogin getUserLogin() {
		return userLogin;
	}
	public void setUserLogin(UserLogin userLogin) {
		this.userLogin = userLogin;
	}
	public List<String> getRolesList() {
		return rolesList;
	}
	public void setRolesList(List<String> rolesList) {
		this.rolesList = rolesList;
	}
	
	public String getActiveRoles(){
		if(roles!=null && !roles.isEmpty()){
			StringBuilder rolesStr = new StringBuilder();
			Iterator<RoleType> rolesIterator = roles.iterator();
			while(rolesIterator.hasNext()){
				rolesStr.append(rolesIterator.next().getRoleName());
				if(rolesIterator.hasNext())rolesStr.append(",");
			}
			return rolesStr.toString();

		}
		return "";
	}
}
