package com.securet.ssm.persistence.objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="client_user_site")
@NamedQueries({
	@NamedQuery(name="getUserAssignedSites",query="SELECT site FROM ClientUserSite cus WHERE cus.clientUser.userId=:userId")
})
public class ClientUserSite extends SecureTObject{

	@Id
	@OneToOne
	@JoinColumn(name="userId",referencedColumnName="userId")
	private User clientUser;
	
	@Id
	@OneToOne
	@JoinColumn(name="siteId",referencedColumnName="siteId")
	private Site site;

	public User getClientUser() {
		return clientUser;
	}

	public void setClientUser(User clientUser) {
		this.clientUser = clientUser;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}
	
}
