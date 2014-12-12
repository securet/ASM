package com.securet.ssm.services.rest;

import java.util.List;

import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.securet.ssm.persistence.objects.SecureTObject;
import com.securet.ssm.persistence.objects.User;
import com.securet.ssm.services.SecureTService;

@RestController
@Repository
public class DefaultRestfulService extends SecureTService  {


	@RequestMapping("/rest/validateUser")
	public User validateUser(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user){
		User fetchedUser = (User)fetchSingleObject("getUserById", "id", user.getUsername());
		return fetchedUser;
	}

	@RequestMapping("/rest/getAllTicketStatus")
	public List<SecureTObject> getAllTicketStatus(){
		List<SecureTObject> ticketStatus = fetchObjects("getTicketStatusForView");
		return ticketStatus;	
	}

	@RequestMapping("/rest/serviceTypes")
	public List<SecureTObject> getServiceTypes(){
		List<SecureTObject> serviceTypes = fetchObjects("getServiceTypeForView");
		return serviceTypes;	
	}

	@RequestMapping("/rest/getIssueTypesForService")
	public List<SecureTObject> getIssueTypes(@RequestParam String serviceTypeId){
		List<SecureTObject> issueTypes = fetchQueriedObjects("getIssueTypeForService", "serviceTypeId", Integer.valueOf(serviceTypeId));
		return issueTypes;
	}
	
	
	@RequestMapping("/rest/getSitesForUser")
	public List<SecureTObject> getSitesForUser(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user){
		List<SecureTObject> sites = fetchQueriedObjects("getUserAssignedSites", "userId", user.getUsername());
		return sites;
	}

}
