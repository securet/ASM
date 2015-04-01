package com.securet.ssm.services.rest;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.securet.ssm.persistence.objects.Enumeration;
import com.securet.ssm.persistence.objects.SecureTObject;
import com.securet.ssm.persistence.objects.User;
import com.securet.ssm.persistence.views.SimpleSite;
import com.securet.ssm.services.SecureTService;

@RestController
@Repository
@Service
public class DefaultRestfulService extends SecureTService  {
	private Logger _logger = LoggerFactory.getLogger(DefaultRestfulService.class);


	@Transactional
	@RequestMapping("/rest/validateUser")
	public Object validateUser(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user){
		String status  = "error";
		Object message = null;
		User data = null;
		if(user!=null){
			status  = "success";
			data = (User)fetchSingleObject("getUserById", "id", user.getUsername());
			data.getUserLogin().setLastLoginTimestamp(new Timestamp(new Date().getTime()));
			entityManager.persist(data);
		}else{
			message = new FieldError("ticket", "userId", "Invalid credentials!");
		}
		return new SecureTJSONResponse(status, message, data);
	}

	@RequestMapping("/rest/getAllTicketStatus")
	public Object getAllTicketStatus(){
		List<SecureTObject> ticketStatus = fetchObjects("getTicketStatusForView");
		return new SecureTJSONResponse("success", null, ticketStatus);
	}

	@RequestMapping("/rest/serviceTypes")
	public Object getServiceTypes(){
		List<SecureTObject> serviceTypes = fetchObjects("getServiceTypeForView");
		return new SecureTJSONResponse("success", null, serviceTypes);
	}

	@RequestMapping("/rest/severityTypes")
	public Object severityTypes(){
		List<SecureTObject> severityTypes = fetchQueriedObjects("getEnumByType", "enumTypeId", "SEVERITY",Enumeration.class);
		return new SecureTJSONResponse("success", null, severityTypes);
	}

	@RequestMapping("/rest/getIssueTypesForService")
	public Object getIssueTypes(@RequestParam String serviceTypeId){
		List<SecureTObject> issueTypes = fetchQueriedObjects("getIssueTypeForService", "serviceTypeId", Integer.valueOf(serviceTypeId));
		return new SecureTJSONResponse("success", null, issueTypes);
	}
	
	
	@RequestMapping("/rest/getSitesForUser")
	public Object getSitesForUser(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user){
		String status  = "error";
		Object message = null;
		Object data = null;
		if(user!=null){
			status  = "success";
			data = fetchQueriedObjects("getUserAssignedSites", "userId", user.getUsername());
		}else{
			
		}
		return new SecureTJSONResponse(status, null, data);
	}

	@RequestMapping("/rest/searchUserSites")
	public Object searchUserSites(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user,@RequestParam String searchString,@RequestParam(value="resultSize") String resultSizeStr){
		int resultsSize = 10;
		try{
			resultsSize = Integer.parseInt(resultSizeStr);
		}catch(NumberFormatException e){
			_logger.debug("Invalid result size:"+resultSizeStr,e);
		}
		List<SimpleSite> sites = searchSites(searchString, resultsSize,user);
		return new SecureTJSONResponse("success", null, sites);
	}
	
	
/*	@RequestMapping("/error/defaulterror")
	public @ResponseBody Object error(){
		return new SecureTJSONResponse("error", new FieldError("login", "login", "Invalid Credentials"), null);
	}
*/	
}
