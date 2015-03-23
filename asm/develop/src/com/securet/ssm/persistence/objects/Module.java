package com.securet.ssm.persistence.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="module")
@NamedQueries({
	@NamedQuery(name = "getModuleById", query = "SELECT o from Module o where moduleId=:id"),
	@NamedQuery(name = "getModuleForView", query = "SELECT o from Module o")
})
public class Module extends SecureTObject{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Min(value=0,message="Invalid Module")
	private int moduleId;
	
	@NotNull
	@Size(min=1,message="Name cannot be empty")
	private String name;
	
	private String description;

	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
