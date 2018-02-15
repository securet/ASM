package com.securet.ssm.persistence.objects;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.securet.ssm.persistence.views.SimpleSite;

@Entity
@Table(name="client_user_site")
@NamedQueries({
	@NamedQuery(name="getUserAssignedSites",query="SELECT site FROM ClientUserSite cus WHERE cus.clientUser.userId=:userId"),
	@NamedQuery(name="getUserAssignedSitesByRegion",query="SELECT NEW com.securet.ssm.persistence.views.SimpleSite(cus.site.siteId, cus.site.name, cus.site.area, cus.site.city, cus.site.state, cus.site.latitude, cus.site.longitude, cus.site.organization.name) FROM ClientUserSite cus WHERE cus.site.city.geoId=:cityGeoId AND cus.clientUser.userId=:userId")
})
@NamedNativeQueries({
	@NamedNativeQuery(
			  name="getUserUnAssignedSitesByRegion",
			  query="SELECT  s.siteId,s.name,s.area,s.city cityGeoId,gc.geoName city,s.state stateGeoId,gs.geoName state,s.latitude,s.longitude,os.name organizationName FROM site s INNER JOIN geo gc ON s.city=gc.geoId INNER JOIN geo gs ON s.state=gs.geoId INNER JOIN organization os ON s.organizationId=os.organizationId LEFT JOIN client_user_site cus ON s.siteId=cus.siteId WHERE s.city=(?1) AND (cus.userId!=(?2) OR cus.siteId IS NULL) GROUP BY s.name",
			  resultSetMapping="simpleSite"
			)
})
@SqlResultSetMapping(name="simpleSite", 
classes={ 
  @ConstructorResult(targetClass=SimpleSite.class, columns={
      @ColumnResult(name="siteId", type=Integer.class),
      @ColumnResult(name="name", type=String.class),
      @ColumnResult(name="area", type=String.class),
      @ColumnResult(name="cityGeoId", type=String.class),
      @ColumnResult(name="city", type=String.class),
      @ColumnResult(name="stateGeoId", type=String.class),
      @ColumnResult(name="state", type=String.class),
      @ColumnResult(name="latitude", type=Double.class),
      @ColumnResult(name="longitude", type=Double.class),
      @ColumnResult(name="organizationName", type=String.class)
  })
})

public class ClientUserSite extends SecureTObject{

	@Id
	@ManyToOne
	@JoinColumn(name="userId",referencedColumnName="userId")
	private User clientUser;
	
	@Id
	@ManyToOne
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
