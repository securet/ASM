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

import com.securet.ssm.persistence.objects.Asset;
import com.securet.ssm.services.SecureTService;

@Repository
@Service
@Controller
public class AssetService extends SecureTService {

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
			excludeInDisplay.add("assetId");
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
			//dataViewNames.add("getSiteForView");
			dataViewNames.add("getAssetTypeForView");
		}
		return dataViewNames;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	@RequestMapping(value="/admin/saveAsset",method=RequestMethod.POST)
	@Transactional
	public String saveAsset(@Valid @ModelAttribute("formObject") Asset formObject,BindingResult result,Model model){
		//check for unique asset id
		boolean createNew = (formObject.getAssetId()==0);//default is 0..
		validate(createNew,formObject,result);
		return adminService.saveObject(formObject, result, model,createNew);
	}

	private void validate(boolean createNew,Asset formObject, BindingResult result) {
		if(formObject.getAssetTag()!=null){
			Query query = null;
			if(createNew){
				query = entityManager.createNamedQuery("getAssetByAssetTag");
			}else{
				query = entityManager.createNamedQuery("getAssetByAssetTagAndNotId");
				query.setParameter("assetId", formObject.getAssetId());
			}
			query.setParameter("assetTag", formObject.getAssetTag());
			int resultsCount = query.getResultList().size();
			if(resultsCount>0){
				FieldError fieldError = new FieldError("formObject", "assetTag", "Asset already exists");
				result.addError(fieldError);
			}
		}
		if(formObject.getAssetType()==null || formObject.getAssetType().getAssetTypeId()==0){
			FieldError fieldError = new FieldError("formObject", "assetType.assetTypeId", "Asset Type cannot be empty");
			result.addError(fieldError);
		}
		if(formObject.getSite()==null || formObject.getSite().getSiteId()==0){
			FieldError fieldError = new FieldError("formObject", "site.name", "Site is not valid");
			result.addError(fieldError);
		}
	}

}
