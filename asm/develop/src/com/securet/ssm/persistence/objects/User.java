package com.securet.ssm.persistence.objects;

import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="user")
@NamedQueries({
	@NamedQuery(name = "getUserById", query = "SELECT o FROM User o WHERE lower(o.userId)=:id"),
	@NamedQuery(name = "getOrganizationWithUsersForView", query = "SELECT DISTINCT o.organization FROM User o JOIN o.organization org WHERE org.organizationId IS NOT NULL"),
	@NamedQuery(name = "getUsersForOrganization", query = "SELECT NEW com.securet.ssm.persistence.views.SimpleUser(o.userId, o.fullName, o.emailId, o.mobile, o.organization.name) FROM User o WHERE o.organization.organizationId=:organizationId"),
	@NamedQuery(name = "getAllClientUsers", query = "SELECT NEW com.securet.ssm.persistence.views.SimpleUser(o.userId, o.fullName, o.emailId, o.mobile, o.organization.name) FROM User o JOIN o.roles role WHERE role.roleType IN ('CLIENT_CONTROLLER','CLIENT_USER')"),
	@NamedQuery(name = "getAllVendors", query = "SELECT NEW com.securet.ssm.persistence.views.SimpleUser(o.userId, o.fullName, o.emailId, o.mobile, o.organization.name) FROM User o JOIN o.roles role WHERE role.roleType IN ('RESOLVER') GROUP BY o.userId"),
	@NamedQuery(name = "getVendorsForOrganization", query = "SELECT NEW com.securet.ssm.persistence.views.SimpleUser(o.userId, o.fullName, o.emailId, o.mobile, o.organization.name) FROM User o JOIN o.roles role WHERE o.organization.organizationId=:organizationId and role.roleType IN ('RESOLVER')")
})
public class User extends SecureTObject {

    @JsonView(SimpleObject.class)
	@Id
	@Size(min=1,message="UserId cannot be empty")
	private String userId;

    @JsonView(SimpleObject.class)
	@NotNull
	@Size(min=1,message="Name cannot be empty")
	private String fullName;
	
    @JsonView(SimpleObject.class)
	@NotNull
	@Size(min=1,message="Email cannot be empty")
//	@Email(message="Invalid Email Id")
	private String emailId;
	
    @JsonView(SimpleObject.class)
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
