package com.securet.ssm.persistence.objects;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="user_login")
@NamedQueries({
	@NamedQuery(name = "getUserLoginById", query = "SELECT o from UserLogin o where o.userId=:id"),
})
public class UserLogin extends SecureTObject {

	@Id  
    @Column(name="userId")  
    private String userId;
 
   	@OneToOne
	@PrimaryKeyJoinColumn(name="userId",referencedColumnName="userId")
   	@JsonIgnore
	private User user;
	
	@NotNull
	@JsonIgnore
	@Size(min=1,message="Password cannot be empty")
	private String password;

	@Transient
	@Size(min=1,message="Verify Password cannot be empty")
	private transient String verifyPassword;
	

	private boolean enabled;
	
	private Timestamp lastLoginTimestamp;
	private Timestamp disabledTimestamp;

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public Timestamp getLastLoginTimestamp() {
		return lastLoginTimestamp;
	}
	public void setLastLoginTimestamp(Timestamp lastLoginTimestamp) {
		this.lastLoginTimestamp = lastLoginTimestamp;
	}
	public Timestamp getDisabledTimestamp() {
		return disabledTimestamp;
	}
	public void setDisabledTimestamp(Timestamp disabledTimestamp) {
		this.disabledTimestamp = disabledTimestamp;
	}
	public String getVerifyPassword() {
		return verifyPassword;
	}
	public void setVerifyPassword(String verifyPassword) {
		this.verifyPassword = verifyPassword;
	}
	public void setUserId(String userId) {
		this.userId=userId;
	}
	public String getUserId(){
		return userId;
	}
	
}
