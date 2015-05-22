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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.securet.ssm.persistence.objects.Organization;
import com.securet.ssm.services.SecureTService;
import com.securet.ssm.utils.SecureTUtils;

@Service
@Repository
@Controller
public class OrganizationService extends SecureTService{

	private static final Logger _logger = LoggerFactory.getLogger(OrganizationService.class);

	private static final String VENDOR = "VENDOR";

	private static List<String> excludeInDisplay = null;  
	private static Map<String,String> customFieldTypes = null;
	
	@Autowired
	private AdminService adminService;
	
	public static List<String> getFieldsToExcludeInDisplay(){
		if(excludeInDisplay==null){
			excludeInDisplay=new ArrayList<String>();
		}
		if(excludeInDisplay.isEmpty()){
			excludeInDisplay = new ArrayList<String>();
			excludeInDisplay.add("organizationType");
			excludeInDisplay.add("logoFile");
			excludeInDisplay.add("organizationId");
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
	
	
	@RequestMapping(value="/admin/saveOrganization",method=RequestMethod.POST)
	@Transactional
	public String saveOrganization(@RequestParam(value="logoFile",required=false)MultipartFile logoFile , @Valid @ModelAttribute("formObject") Organization formObject,BindingResult result,Model model){
		boolean createNew = (formObject.getOrganizationId()==0);//default is 0..
		_logger.debug("organization name: "+formObject.getName());
		// we will only create VENDOR as of now.. so if type is null assign VENDOR 
		if(formObject.getOrganizationType()==null || formObject.getOrganizationType().isEmpty()){
			formObject.setOrganizationType(VENDOR);
		}
		String savedStatus = adminService.saveObject(formObject, result, model,createNew);
		if(!result.hasErrors() && logoFile!=null && !logoFile.isEmpty()){
			_logger.debug("file uploaded with logoFile.isEmpty()"+ logoFile.isEmpty());
			_logger.debug("file uploaded with contentType"+ SecureTUtils.getFileExtension(logoFile.getContentType()));
			String fileName = formObject.getOrganizationId()+SecureTUtils.getFileExtension(logoFile.getContentType());
			String logoPath = SecureTService.ASSETS_SSMUPLOADS_LOGOS+fileName;
			String savedPath = SecureTUtils.saveToFile(logoFile, SecureTService.ASSETS_SSMUPLOADS_LOGOS,fileName);
			if(savedPath!=null){
				formObject.setLogo(logoPath);
				entityManager.merge(formObject);
			}
		}
		return savedStatus;
	}

	public static Object getDataViewNames() {
		// TODO Auto-generated method stub
		return null;
	}
}
