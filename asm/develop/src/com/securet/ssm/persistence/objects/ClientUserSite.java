package com.securet.ssm.persistence.objects;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class ClientUserSite extends SecureTObject{

	@Id
	@OneToOne
	@JoinColumn(name="siteId",referencedColumnName="siteId")
	private User clientUser;
	
	@Id
	@ManyToOne
	@JoinColumn(name="siteId",referencedColumnName="siteId")
	private List<Site> sites;

	public User getClientUser() {
		return clientUser;
	}

	public void setClientUser(User clientUser) {
		this.clientUser = clientUser;
	}

	public List<Site> getSites() {
		return sites;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}
	
}
