package com.securet.ssm.services.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.securet.ssm.persistence.objects.ServiceType;
import com.securet.ssm.services.SecureTService;

@Repository
@Service
@Controller
public class ServiceTypeService extends SecureTService {

	private static List<String> excludeInDisplay = null;  
	private static Map<String,String> customFieldTypes = null;  
	private static List<String> dataViewNames = null;  

	@Autowired
	protected AdminService adminService;
	
	
	public static List<String> getFieldsToExcludeInDisplay(){
		if(excludeInDisplay==null){
			excludeInDisplay=new ArrayList<String>();
		}
		if(excludeInDisplay.isEmpty()){
			excludeInDisplay = new ArrayList<String>();
			excludeInDisplay.add("organization");
			excludeInDisplay.add("serviceTypeId");
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
		}
		return customFieldTypes;
	}
	
	public static List<String> getDataViewNames() {
		if(dataViewNames==null){
			dataViewNames=new ArrayList<String>();
		}
		return dataViewNames;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}
	
	@RequestMapping(value="/admin/saveServiceType",method=RequestMethod.POST)
	@Transactional
	public String saveServiceType(@Valid @ModelAttribute("formObject") ServiceType formObject,BindingResult result,Model model){
		boolean createNew = (formObject.getServiceTypeId()==0);//default is 0..
		return adminService.saveObject(formObject, result, model,createNew);
	}

}
