package com.securet.ssm.services.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.securet.ssm.persistence.objects.Organization;
import com.securet.ssm.services.SecureTService;

@Service
@Repository
@Controller
public class OrganizationService extends SecureTService{

	private static final Logger _logger = LoggerFactory.getLogger(OrganizationService.class);

	private static List<String> excludeInDisplay = null;  
	private static Map<String,String> customFieldTypes = null;
	
	@Autowired
	private AdminService adminService;
	

	private Organization formObject;
	
	
	public static List<String> getFieldsToExcludeInDisplay(){
		if(excludeInDisplay==null){
			excludeInDisplay=new ArrayList<String>();
		}
		if(excludeInDisplay.isEmpty()){
			excludeInDisplay = new ArrayList<String>();
			excludeInDisplay.add("organizationId");
			excludeInDisplay.add("createdStamp");
			excludeInDisplay.add("lastUpdatedStamp");
		}
		return excludeInDisplay;
	}
	
	public static Map<String,String> getCustomFieldTypes(){
		if(customFieldTypes==null){
			customFieldTypes = new HashMap<String,String>();
		}
		if(customFieldTypes.isEmpty()){
			customFieldTypes.put("logo", "file");
		}
		return customFieldTypes;
	}
	
	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public Organization getOrganization() {
		return formObject;
	}

	public void setOrganization(Organization organization) {
		this.formObject = organization;
	}
	
	
	@RequestMapping(value="/admin/saveOrganization",method=RequestMethod.POST)
	@Transactional
	public String saveOrganization(@Valid @ModelAttribute("formObject") Organization formObject,BindingResult result,Model model){
		boolean createNew = (formObject.getOrganizationId()==0);//default is 0..
		return adminService.saveObject(formObject, result, model,createNew);
	}

	public static Object getDataViewNames() {
		// TODO Auto-generated method stub
		return null;
	}
}
