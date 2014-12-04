package com.securet.ssm.services.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.securet.ssm.persistence.objects.SecureTObject;
import com.securet.ssm.persistence.objects.Site;
import com.securet.ssm.persistence.objects.User;
import com.securet.ssm.services.DefaultService;
import com.securet.ssm.services.SecureTService;
import com.securet.ssm.services.vo.ClientUserSite;

@Controller
@Repository
@Service
public class ClientUserSiteMappingService extends SecureTService{

	private static final Logger _logger = LoggerFactory.getLogger(ClientUserSiteMappingService.class);
	private static List<String> excludeInDisplay = null;  
	private static Map<String,String> customFieldTypes = null;  
	private static List<String> dataViewNames = null;  

	@Autowired
	private AdminService adminService;

	
	public static List<String> getFieldsToExcludeInDisplay(){
		if(excludeInDisplay==null){
			excludeInDisplay=new ArrayList<String>();
		}
		if(excludeInDisplay.isEmpty()){
			excludeInDisplay = new ArrayList<String>();
			excludeInDisplay.add("createdTimestamp");
			excludeInDisplay.add("lastUpdatedTimestamp");
		}
		return excludeInDisplay;
	}

	public static Map<String,String> getCustomFieldTypes(){
		if(customFieldTypes==null){
			customFieldTypes = new HashMap<String,String>();
		}
		if(customFieldTypes.isEmpty()){
			//add any custom field Type here
		}
		return customFieldTypes;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public static List<String> getDataViewNames() {
		if(dataViewNames==null){
			dataViewNames=new ArrayList<String>();
			dataViewNames.add("getClientOrganizationForView");
			dataViewNames.add("getCityWithSitesForView");
		}
		return dataViewNames;
	}

	@RequestMapping("/admin/viewClientUserSites")
	public String viewClientUserSite(Model model){
		//load Organizations
		//Load Regions
		model.addAttribute("formObject",new ClientUserSite());
		makeUIData(entityManager,model,"ClientUserSite");
		return DefaultService.ADMIN+"viewClientUserSites";
	}

	@RequestMapping(value="/admin/getUsersForOrganization",produces="application/json")
	public @ResponseBody List<SecureTObject> getUserForOrganization(@RequestParam String organizationId){
		return fetchQueriedObjects( "getUsersForOrganization", "organizationId",Integer.valueOf(organizationId));
	}
	
	@RequestMapping(value="/admin/getUserAssignedSites",produces="application/json")
	public @ResponseBody List<SecureTObject> getUserAssignedSites(@RequestParam String userId){
		String namedQuery = "getUserAssignedSites";
		String namedParameter = "userId";
		return fetchQueriedObjects(namedQuery, namedParameter, userId);
	}
	

	@Transactional
	@RequestMapping("/admin/saveClientUserSiteMapping")
	public String saveClientUserSiteMapping(@Valid @ModelAttribute("formObject") ClientUserSite formObject,BindingResult result,Model model){
		if(!result.hasErrors()){
			User clientUser = new User();
			clientUser.setUserId(formObject.getUserId());
			//do not persist existing assignments as they exist.
			Query existingAssignments = entityManager.createQuery("SELECT cus.site.siteId FROM ClientUserSite cus WHERE cus.clientUser.userId=:userId and cus.site.siteId IN (:sites)");
			existingAssignments.setParameter("userId", formObject.getUserId());
			existingAssignments.setParameter("sites", formObject.getSites());
			List<Integer> existingSites = existingAssignments.getResultList();
			_logger.debug("existing assignments: "+existingSites);
			List<Integer> newSites = new ArrayList<Integer>(formObject.getSites());
			newSites.removeAll(existingSites);
			//update any new mappings
			for(Integer siteId:newSites){
				com.securet.ssm.persistence.objects.ClientUserSite clientUserSiteObject = new com.securet.ssm.persistence.objects.ClientUserSite();
				clientUserSiteObject.setClientUser(clientUser);
				Site site = new Site();
				site.setSiteId(Integer.valueOf(siteId));
				clientUserSiteObject.setSite(site);
				entityManager.persist(clientUserSiteObject);
			}
			//delete old assignments
			Query deleteOldQuery = entityManager.createQuery("DELETE FROM ClientUserSite cus WHERE cus.clientUser.userId=:userId and cus.site.siteId NOT IN (:sites)");
			deleteOldQuery.setParameter("userId", formObject.getUserId());
			deleteOldQuery.setParameter("sites", formObject.getSites());
			int deletes = deleteOldQuery.executeUpdate();
			_logger.info("Deleted "+ deletes+ "ClientUserMapping on save");
			model.addAttribute("saved", "Saved site mapping successfully for user: "+formObject.getUserId());
		}
		if(formObject.getOrganizationId()!=null && formObject.getOrganizationId()>0){
			List clientUsers = fetchQueriedObjects( "getUsersForOrganization", "organizationId",formObject.getOrganizationId());
			model.addAttribute("getUsersForOrganization", clientUsers);
		}
		
		makeUIData(entityManager,model,"ClientUserSite");
		return DefaultService.ADMIN+"viewClientUserSites";
	}
}