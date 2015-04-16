package com.securet.ssm.services.rest;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mysema.query.jpa.sql.JPASQLQuery;
import com.mysema.query.types.expr.DateTimeOperation;
import com.securet.ssm.persistence.objects.AppNotificaton;
import com.securet.ssm.persistence.objects.querydsl.jpa.JPAAppNotificaton;
import com.securet.ssm.services.SecureTService;

@RestController
@Repository
@Service
public class AppNotificationService  extends SecureTService  {
	
	private static final JPAAppNotificaton jpaAppNotification  = JPAAppNotificaton.appNotificaton;
	private static final Logger _logger = LoggerFactory.getLogger(AppNotificationService.class); 

	@RequestMapping("/rest/appNotifications")
	public Object appNotifications(@ModelAttribute AppNotification appNotification){
		JPASQLQuery  jpaSQLQuery = new JPASQLQuery(entityManager,sqlTemplates);
		jpaSQLQuery.from(jpaAppNotification)
		.where(jpaAppNotification.minAppVersion.loe(appNotification.appCurrentVersion).and(jpaAppNotification.maxAppVersion.goe(appNotification.appCurrentVersion))
				.and(jpaAppNotification.fromDate.before(DateTimeOperation.currentTimestamp(Timestamp.class))
				.and(jpaAppNotification.thruDate.isNull().or(jpaAppNotification.thruDate.after(DateTimeOperation.currentTimestamp(Timestamp.class))))))
		.orderBy(jpaAppNotification.fromDate.asc());		
		//later we may add 
		List<AppNotificaton> appNotifications = jpaSQLQuery.list(jpaAppNotification);
		return new SecureTJSONResponse("success", null, appNotifications);
	}

	
	public static class AppNotification{
		
		public AppNotification() {
			// Nothing Here
		}
		
		private double appCurrentVersion;
		
		private String androidVersionName;
		
		private double androidAPILevel;
		
		private String deviceName;

		public double getAppCurrentVersion() {
			return appCurrentVersion;
		}

		public void setAppCurrentVersion(double appCurrentVersion) {
			this.appCurrentVersion = appCurrentVersion;
		}

		public String getAndroidVersionName() {
			return androidVersionName;
		}

		public void setAndroidVersionName(String androidVersionName) {
			this.androidVersionName = androidVersionName;
		}

		public double getAndroidAPILevel() {
			return androidAPILevel;
		}

		public void setAndroidAPILevel(double androidAPILevel) {
			this.androidAPILevel = androidAPILevel;
		}

		public String getDeviceName() {
			return deviceName;
		}

		public void setDeviceName(String deviceName) {
			this.deviceName = deviceName;
		}
		
	}
	
}
