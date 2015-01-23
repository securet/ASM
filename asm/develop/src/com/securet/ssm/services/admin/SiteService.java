package com.securet.ssm.services.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.securet.ssm.persistence.objects.Organization;
import com.securet.ssm.persistence.objects.SecureTObject;
import com.securet.ssm.persistence.objects.Site;
import com.securet.ssm.persistence.views.SimpleSite;
import com.securet.ssm.services.SecureTService;

@Repository
@Service
@Controller
public class SiteService extends SecureTService{

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
			excludeInDisplay.add("siteId");
			excludeInDisplay.add("organization");
			excludeInDisplay.add("createdTimestamp");
			excludeInDisplay.add("lastUpdatedTimestamp");
		}
		return excludeInDisplay;
	}

	public static Map<String,String> getCustomFieldTypes(){
		if(customFieldTypes==null){
			customFieldTypes = new HashMap<String,String>();
			//add any custom field Type here
			customFieldTypes.put("siteType", "list");
		}
		return customFieldTypes;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	@RequestMapping(value="/admin/saveSite",method=RequestMethod.POST)
	@Transactional
	public String saveSite(@Valid @ModelAttribute("formObject") Site formObject,BindingResult result,Model model){
		boolean createNew = (formObject.getSiteId()==0);//default is 0..
		validateSite(formObject,result);
		if(!result.hasErrors()){
			//set the client organization - we will have only one as of now.. 
			Query clientOrganizationQuery = entityManager.createNamedQuery("getClientOrganizationForView");
			List<Organization> organizations = clientOrganizationQuery.getResultList();
			if(organizations!=null){
				formObject.setOrganization(organizations.get(0));
			}else{
				FieldError fieldError = new FieldError("formObject", "organization.organizationId", "Could not find any client organization, please create one");
				result.addError(fieldError);
			}
			
		}
/*		if(formObject.getState()!=null && formObject.getState().getGeoId()!=null){
			String namedQuery = "getRelatedGeos";
			String namedParameter = "geoIdFrom";
			List<SecureTObject> geoCities = fetchQueriedObjects(namedQuery, namedParameter,formObject.getState().getGeoId());
			model.addAttribute("getCityForView", geoCities);
		}
*/		
		return adminService.saveObject(formObject, result, model,createNew);
	}

	private void validateSite(Site formObject, BindingResult result) {
/*		if(formObject.getOrganization()==null || formObject.getOrganization().getOrganizationId()==0){
			FieldError fieldError = new FieldError("formObject", "organization.organizationId", "Please select an organization");
			result.addError(fieldError);
		}
*/		if(formObject.getCity()==null || formObject.getCity().getGeoId()==null || formObject.getCity().getGeoId().equals("")){
			FieldError fieldError = new FieldError("formObject", "city.geoId", "Please select a city");
			result.addError(fieldError);
		}
		if(formObject.getState()==null || formObject.getState().getGeoId()==null || formObject.getState().getGeoId().equals("")){
			FieldError fieldError = new FieldError("formObject", "state.geoId", "Please select a state");
			result.addError(fieldError);
		}
		
	}

	public static List<String> getDataViewNames() {
		if(dataViewNames==null){
			dataViewNames=new ArrayList<String>();
			//dataViewNames.add("getOrganizationForView");
			dataViewNames.add("getStateForView");
			//dataViewNames.add("getCityForView");
			dataViewNames.add("getSiteTypeForView");
		}
		return dataViewNames;
	}
	
	@RequestMapping(value="/admin/searchSites",produces="application/json")
	public @ResponseBody List<SimpleSite> searchSites(@RequestParam String searchString){
		Query siteSearchQuery = entityManager.createNamedQuery("searchSites");
		siteSearchQuery.setParameter("siteName", "%"+searchString+"%");
		siteSearchQuery.setMaxResults(100);
		List<SimpleSite> sites = (List<SimpleSite>) siteSearchQuery.getResultList();
		return sites;
	}

}
