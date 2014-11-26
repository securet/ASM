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

@Entity
@Table(name="role_type")
@NamedQueries({
	@NamedQuery(name = "getRoleTypeById", query = "SELECT o from RoleType o where o.roleTypeId=:id"),
	@NamedQuery(name = "getRoleTypeForView", query = "SELECT o from RoleType o")
})

public class RoleType extends SecureTObject{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int roleTypeId;
	
	@NotNull
	@Size(min=1,message="Role cannot be empty")
	private String roleType;

	@NotNull
	@Size(min=1,message="RoleName cannot be empty")
	private String roleName;
	
	
	public int getRoleTypeId() {
		return roleTypeId;
	}

	public void setRoleTypeId(int roleTypeId) {
		this.roleTypeId = roleTypeId;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
}
