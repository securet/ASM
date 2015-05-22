package com.securet.ssm.persistence.objects;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
		name="app_notification"
)
public class AppNotificaton extends SecureTObject {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int notificationId;
	
	private String message;
	
	private boolean isAppUpdate;
	
	private double minAppVersion;
	
	private double maxAppVersion;
	
	private Timestamp fromDate;
	
	private Timestamp thruDate;

	public int getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isAppUpdate() {
		return isAppUpdate;
	}

	public void setAppUpdate(boolean isAppUpdate) {
		this.isAppUpdate = isAppUpdate;
	}

	public Timestamp getFromDate() {
		return fromDate;
	}

	public void setFromDate(Timestamp fromDate) {
		this.fromDate = fromDate;
	}

	public Timestamp getThruDate() {
		return thruDate;
	}

	public void setThruDate(Timestamp thruDate) {
		this.thruDate = thruDate;
	}

	public double getMaxAppVersion() {
		return maxAppVersion;
	}

	public void setMaxAppVersion(double maxAppVersion) {
		this.maxAppVersion = maxAppVersion;
	}

	public double getMinAppVersion() {
		return minAppVersion;
	}

	public void setMinAppVersion(double minAppVersion) {
		this.minAppVersion = minAppVersion;
	}

	
	
}
