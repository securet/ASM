package com.securet.ssm.services.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Repository;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.securet.ssm.persistence.objects.SecureTObject;
import com.securet.ssm.persistence.objects.User;
import com.securet.ssm.services.SecureTService;

@RestController
@Repository
public class DefaultRestfulService extends SecureTService  {


	@RequestMapping("/rest/validateUser")
	public Object validateUser(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user){
		String status  = "error";
		Object message = null;
		Object data = null;
		if(user!=null){
			status  = "success";
			data = (User)fetchSingleObject("getUserById", "id", user.getUsername());
		}else{
			message = new FieldError("ticket", "userId", "Invalid credentials!");
		}
		return new SecureTJSONResponse(status, null, data);
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
	
/*	@RequestMapping("/error/defaulterror")
	public @ResponseBody Object error(){
		return new SecureTJSONResponse("error", new FieldError("login", "login", "Invalid Credentials"), null);
	}
*/	
}
