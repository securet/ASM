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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.securet.ssm.persistence.objects.Asset;
import com.securet.ssm.persistence.objects.SecureTObject;
import com.securet.ssm.persistence.objects.ServiceType;
import com.securet.ssm.persistence.objects.User;
import com.securet.ssm.services.DefaultService;
import com.securet.ssm.services.SecureTService;
import com.securet.ssm.services.vo.VendorServiceAsset;

@Controller
@Service
@Repository
public class VendorAssetMappingService extends SecureTService{

	private Logger _logger = LoggerFactory.getLogger(VendorAssetMappingService.class);
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
			dataViewNames.add("getOrganizationWithUsersForView");
			dataViewNames.add("getServiceTypeForView");
			dataViewNames.add("getCityWithSitesForView");
		}
		return dataViewNames;
	}

	//viewVendorAssetMapping
	@RequestMapping("/admin/viewVendorAssetMapping")
	public String viewVendorAssetMapping(Model model){
		model.addAttribute("formObject",new VendorServiceAsset());
		makeUIData(entityManager,model,"VendorServiceAsset");
		return DefaultService.ADMIN+"viewVendorAssetMapping";
	}
	
	@RequestMapping(value="/admin/getVendorsForOrganization",produces="application/json")
	public @ResponseBody List<SecureTObject> getUserForOrganization(@RequestParam String organizationId){
		return fetchQueriedObjects("getVendorsForOrganization", "organizationId",Integer.valueOf(organizationId));
	}
	
	@RequestMapping(value="/admin/getAssetsForSite",produces="application/json")
	public @ResponseBody List<SecureTObject> getAssetsForSite(@RequestParam String siteId){
		return fetchQueriedObjects("getAssetsForSite", "siteId",Integer.valueOf(siteId));
	}

	@RequestMapping(value="/admin/getUserAssignedAssets",produces="application/json")
	public @ResponseBody List<SecureTObject> getUserAssignedAssets(@RequestParam String userId,@RequestParam String serviceTypeId){
		Query existingVendorAssetMap = entityManager.createQuery("SELECT vsa.asset FROM VendorServiceAsset vsa WHERE vsa.vendorUser.userId=:userId AND vsa.serviceType.serviceTypeId=:serviceTypeId");
		existingVendorAssetMap.setParameter("userId", userId);
		existingVendorAssetMap.setParameter("serviceTypeId", Integer.valueOf(serviceTypeId));
		return existingVendorAssetMap.getResultList();
	}
	
	@Transactional
	@RequestMapping("/admin/saveVendorAssetMapping")
	public String saveVendorAssetMapping(@Valid @ModelAttribute("formObject") VendorServiceAsset formObject,BindingResult result,Model model){
		if(!result.hasErrors()){
			User vendorUser = new User();
			vendorUser.setUserId(formObject.getUserId());
			ServiceType serviceType = new ServiceType();
			serviceType.setServiceTypeId(formObject.getServiceTypeId());
			//find existing assets
			Query existingVendorAssetMap = entityManager.createQuery("SELECT vsa.asset.assetId FROM VendorServiceAsset vsa WHERE vsa.vendorUser.userId=:userId AND vsa.serviceType.serviceTypeId=:serviceTypeId");
			existingVendorAssetMap.setParameter("userId", formObject.getUserId());
			existingVendorAssetMap.setParameter("serviceTypeId", formObject.getServiceTypeId());
			List<Integer> assetsMapped = existingVendorAssetMap.getResultList();
			
			List<Integer> newMappedAssets = new ArrayList<Integer>(formObject.getAssets());
			//remove the to save new list
			newMappedAssets.removeAll(assetsMapped);
			//save new mappings
			for(Integer assetId:newMappedAssets){
				Asset asset = new Asset();
				asset.setAssetId(assetId);
				com.securet.ssm.persistence.objects.VendorServiceAsset vendorServiceAsset = new com.securet.ssm.persistence.objects.VendorServiceAsset();
				vendorServiceAsset.setVendorUser(vendorUser);
				vendorServiceAsset.setServiceType(serviceType);
				vendorServiceAsset.setAssets(asset);
				entityManager.persist(vendorServiceAsset);
			}
			
			//delete any unassigned assets with user and service
			Query deleteVendorAssetMap = entityManager.createQuery("DELETE FROM VendorServiceAsset vsa WHERE vsa.vendorUser.userId=:userId AND vsa.serviceType.serviceTypeId=:serviceTypeId AND vsa.asset.assetId NOT IN (:assets)");
			deleteVendorAssetMap.setParameter("userId", formObject.getUserId());
			deleteVendorAssetMap.setParameter("serviceTypeId", formObject.getServiceTypeId());
			deleteVendorAssetMap.setParameter("assets", formObject.getAssets());
			int deletes = deleteVendorAssetMap.executeUpdate();
			_logger.debug("Deleted unassigned vendor asset mapping :"+deletes);
			model.addAttribute("saved","Saved asset mapping successfully for vendor: "+formObject.getUserId());
		}
		//get users if organization is selected
		if(formObject.getOrganizationId()!=null && formObject.getOrganizationId()>0){
			List vendors = fetchQueriedObjects("getVendorsForOrganization", "organizationId",formObject.getOrganizationId());
			model.addAttribute("getVendorsForOrganization",vendors);
		}
		//get the sites if city was selected
		if(formObject.getCityGeoId()!=null){
			List sites = fetchQueriedObjects("getSitesForCity", "cityGeoId",formObject.getCityGeoId());
			model.addAttribute("getSitesForCity",sites);
		}
		//get the assets if city was selected
		if(formObject.getSiteId()!=null){
			List<SecureTObject> assets = fetchQueriedObjects("getAssetsForSite", "siteId",Integer.valueOf(formObject.getSiteId()));
			Map assetOptions = new HashMap();
			for(SecureTObject secureTObject:assets){
				Asset asset = (Asset) secureTObject; 
				assetOptions.put(asset.getAssetId(), asset.getName());
			}
			model.addAttribute("assetOptions",assetOptions);
			model.addAttribute("getAssetsForSite",assets);
		}
		
		makeUIData(entityManager,model,"VendorServiceAsset");
		return DefaultService.ADMIN+"viewVendorAssetMapping";
	}
}