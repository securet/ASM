package com.securet.ssm.services.admin;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.securet.ssm.persistence.objects.SecureTObject;
import com.securet.ssm.persistence.objects.Ticket;
import com.securet.ssm.services.DefaultService;
import com.securet.ssm.services.SecureTService;

@Controller
@Repository
@Service
public class TicketService extends SecureTService {

	private static final Logger _logger = LoggerFactory.getLogger(TicketService.class);
	private static List<String> dataViewNames = null;  

	@Autowired
	private AdminService adminService;

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public static List<String> getDataViewNames() {
		if(dataViewNames==null){
			dataViewNames=new ArrayList<String>();
			dataViewNames.add("getServiceTypeForView");
		}
		return dataViewNames;
	}
	
	@RequestMapping("/tickets/newTicket")
	public String newTicket(Model model,@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser){
		String userId = customUser.getUsername();
		model.addAttribute("formObject",new Ticket());
		String namedQuery = "getUserAssignedSites";
		String namedParameter = "userId";
		List userAssignedSites = fetchQueriedObjects(namedQuery, namedParameter, userId);
		model.addAttribute("userAssignedSites", userAssignedSites);
		
		makeUIData(entityManager,model,"Ticket");
		return DefaultService.TICKET+"newTicket";
	}

	@RequestMapping(value="/tickets/vendorAndIssueTypeBySiteService",produces="application/json")
	public @ResponseBody List<SecureTObject> getAssetsForSite(@RequestParam String serviceTypeId, @RequestParam String siteId){
		Query vendorServiceAssets =  entityManager.createNamedQuery("getVendorServiceAssetByServiceType");
		vendorServiceAssets.setParameter("serviceTypeId", serviceTypeId);
		fetchQueriedObjects("getIssueTypeForService", "serviceTypeId",Integer.valueOf(serviceTypeId));
		return null;
	}

}
