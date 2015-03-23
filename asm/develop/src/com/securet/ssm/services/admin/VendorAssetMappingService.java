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
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.jpa.sql.JPASQLQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import com.securet.ssm.persistence.objects.Asset;
import com.securet.ssm.persistence.objects.SecureTObject;
import com.securet.ssm.persistence.objects.ServiceType;
import com.securet.ssm.persistence.objects.User;
import com.securet.ssm.persistence.objects.querydsl.jpa.JPAVendorServiceAsset;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLAsset;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLAssetType;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLClientUserSite;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLSite;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLVendorServiceAsset;
import com.securet.ssm.persistence.views.SimpleAsset;
import com.securet.ssm.persistence.views.SimpleSite;
import com.securet.ssm.services.DefaultService;
import com.securet.ssm.services.SecureTService;
import com.securet.ssm.services.vo.VendorServiceAsset;
import com.securet.ssm.utils.SecureTUtils;

@Controller
@Service
@Repository
public class VendorAssetMappingService extends SecureTService{

	private static final JPAVendorServiceAsset vendorServiceAsset = JPAVendorServiceAsset.vendorServiceAsset;

	private static final SQLVendorServiceAsset sqlVendorServiceAsset = SQLVendorServiceAsset.vendorServiceAsset;
	private static final SQLSite sqlSite = SQLSite.site;
	private static final SQLAsset sqlAsset = SQLAsset.asset;
	private static final SQLClientUserSite sqlClientUserSite = SQLClientUserSite.clientUserSite;
	private static final SQLAssetType sqlAssetType = SQLAssetType.assetType;

	private static final String VENDOR_ASSET_BY_SERVICE_REGION_ASSET_TYPE_FROM_CLAUSE = " FROM VendorServiceAsset vsa WHERE vsa.vendorUser.userId=:userId AND vsa.serviceType.serviceTypeId=:serviceTypeId AND vsa.asset.assetType.assetTypeId=:assetTypeId AND vsa.asset.site.city.geoId=:cityGeoId";
	private static final String VENDOR_ASSET_IDS_BY_SERVICE_REGION_ASSET_TYPE_SELECT_FIELDS = "SELECT vsa.asset.assetId ";

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
			dataViewNames.add("getVendorOrganizationForView");
			dataViewNames.add("getServiceTypeForView");
			dataViewNames.add("getCityWithSitesForView");
			dataViewNames.add("getAssetTypeForView");
			dataViewNames.add("getAllVendors");
		}
		return dataViewNames;
	}

	//viewVendorAssetMapping
	@RequestMapping(value={"/admin/viewVendorAssetMapping","/user/viewVendorAssetMapping"})
	public String viewVendorAssetMapping(Model model){
		model.addAttribute("formObject",new VendorServiceAsset());
		makeUIData(entityManager,model,"VendorServiceAsset");
		return DefaultService.ADMIN+"viewVendorAssetMapping";
	}
	
	@RequestMapping(value={"/admin/getVendorsForOrganization","/user/getVendorsForOrganization"},produces="application/json")
	public @ResponseBody List<SecureTObject> getUserForOrganization(@RequestParam String organizationId){
		return fetchQueriedObjects("getVendorsForOrganization", "organizationId",Integer.valueOf(organizationId));
	}
	
	@RequestMapping(value="/admin/getAssetsForSite",produces="application/json")
	public @ResponseBody List<SecureTObject> getAssetsForSite(@RequestParam String siteId){
		return fetchQueriedObjects("getAssetsForSite", "siteId",Integer.valueOf(siteId));
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/admin/getUnassignedAssetsByCityAndAssetType",produces="application/json")
	public @ResponseBody List<SimpleAsset> getUnAssignedAssetsByServiceCityAndAssetType(@RequestParam String serviceTypeId, @RequestParam String cityGeoId,@RequestParam String assetTypeId){
		return getUnAssignedAssetsByServiceCityAndAssetType(null, serviceTypeId, cityGeoId, assetTypeId);
	}

	public List<SimpleAsset> getUnAssignedAssetsByServiceCityAndAssetType(org.springframework.security.core.userdetails.User customUser,@RequestParam String serviceTypeId, @RequestParam String cityGeoId,@RequestParam String assetTypeId){
		QBean<SimpleAsset> simpleAssetExpr = simplAssetResultExpr();
		JPASQLQuery unassignedVendorQuery = new JPASQLQuery(entityManager, sqlTemplates);

		unassignedVendorQuery.from(sqlAsset)
		.innerJoin(sqlAssetType).on(sqlAsset.assetTypeId.eq(sqlAssetType.assetTypeId))
		.innerJoin(sqlSite).on(sqlAsset.siteId.eq(sqlSite.siteId));
		
		if(customUser!=null){
			addClientUserToQuery(customUser, unassignedVendorQuery);
		}

		unassignedVendorQuery.leftJoin(sqlVendorServiceAsset).on(sqlVendorServiceAsset.assetId.eq(sqlAsset.assetId).and(sqlVendorServiceAsset.serviceTypeId.eq(Integer.valueOf(serviceTypeId))));

		unassignedVendorQuery.where(sqlVendorServiceAsset.assetId.isNull()
				.and(sqlSite.city.eq(cityGeoId)).and(sqlAsset.assetTypeId.eq(Integer.valueOf(assetTypeId)))
				);

		return unassignedVendorQuery.list(simpleAssetExpr);
	}

	private QBean<SimpleAsset> simplAssetResultExpr() {
		QBean<SimpleAsset> simpleAssetExpr = Projections.fields(SimpleAsset.class, sqlAsset.assetId,sqlAsset.name,sqlAsset.assetTag,sqlAssetType.name.as("assetType"),
				sqlSite.name.as("siteName"),sqlAsset.installedDate);
		return simpleAssetExpr;
	}


	@SuppressWarnings("unchecked")
	@RequestMapping(value="/admin/getUserAssignedAssets",produces="application/json")
	public @ResponseBody List<SimpleAsset> getUserAssignedAssets(@RequestParam String userId,@RequestParam String serviceTypeId,@RequestParam String assetTypeId,@RequestParam String cityGeoId){
		return getUserAssignedAssets(null, userId, serviceTypeId, assetTypeId, cityGeoId);
	}
	
	public List<SimpleAsset> getUserAssignedAssets(org.springframework.security.core.userdetails.User customUser,@RequestParam String userId,@RequestParam String serviceTypeId,@RequestParam String assetTypeId,@RequestParam String cityGeoId){
		QBean<SimpleAsset> simpleAssetExpr = simplAssetResultExpr();
		JPASQLQuery existingVendorAssetQuery = getUserAssignedAssetsQuery(customUser, userId, serviceTypeId, assetTypeId, cityGeoId);
		return existingVendorAssetQuery.list(simpleAssetExpr);
	}

	private JPASQLQuery getUserAssignedAssetsQuery(org.springframework.security.core.userdetails.User customUser, String userId, String serviceTypeId, String assetTypeId, String cityGeoId) {
		JPASQLQuery existingVendorAssetQuery = new JPASQLQuery(entityManager, sqlTemplates);
		
		existingVendorAssetQuery.from(sqlVendorServiceAsset)
		.innerJoin(sqlAsset).on(sqlVendorServiceAsset.assetId.eq(sqlAsset.assetId))
		.innerJoin(sqlAssetType).on(sqlAsset.assetTypeId.eq(sqlAssetType.assetTypeId))
		.innerJoin(sqlSite).on(sqlAsset.siteId.eq(sqlSite.siteId));
		
		existingVendorAssetQuery.where(sqlVendorServiceAsset.userId.eq(userId).and(sqlVendorServiceAsset.serviceTypeId.eq(Integer.valueOf(serviceTypeId)))
				.and(sqlSite.city.eq(cityGeoId)).and(sqlAsset.assetTypeId.eq(Integer.valueOf(assetTypeId)))
				);

		if(customUser!=null){
			addClientUserToQuery(customUser, existingVendorAssetQuery);
		}
		return existingVendorAssetQuery;
	}

	private JPASQLQuery getUserAssignedAssetsQuery(org.springframework.security.core.userdetails.User customUser, VendorServiceAsset vendorServiceAsset) {
		JPASQLQuery existingVendorAssetQuery = new JPASQLQuery(entityManager, sqlTemplates);
		
		existingVendorAssetQuery.from(sqlVendorServiceAsset)
		.innerJoin(sqlAsset).on(sqlVendorServiceAsset.assetId.eq(sqlAsset.assetId))
		.innerJoin(sqlAssetType).on(sqlAsset.assetTypeId.eq(sqlAssetType.assetTypeId))
		.innerJoin(sqlSite).on(sqlAsset.siteId.eq(sqlSite.siteId));
		
		existingVendorAssetQuery.where(sqlVendorServiceAsset.userId.eq(vendorServiceAsset.getUserId()).and(sqlVendorServiceAsset.serviceTypeId.eq(vendorServiceAsset.getServiceTypeId()))
				.and(sqlSite.city.eq(vendorServiceAsset.getCityGeoId())).and(sqlAsset.assetTypeId.eq(Integer.valueOf(vendorServiceAsset.getAssetTypeId())))
				);

		if(customUser!=null){
			addClientUserToQuery(customUser, existingVendorAssetQuery);
		}
		return existingVendorAssetQuery;
	}
	
	private void addClientUserToQuery(org.springframework.security.core.userdetails.User customUser, JPASQLQuery query) {
		query.innerJoin(sqlClientUserSite).on(sqlSite.siteId.eq(sqlClientUserSite.siteId));
		query.where(sqlClientUserSite.userId.eq(customUser.getUsername()));
	}

	@RequestMapping(value="/admin/getUserAssignedAndUnassignedAssets",produces="application/json")
	public @ResponseBody Map<String,List<SimpleAsset>> getUserAssignedAndUnassignedAssets(@RequestParam String userId,@RequestParam String serviceTypeId,@RequestParam String assetTypeId,@RequestParam String cityGeoId){
		Map<String,List<SimpleAsset>> allAssets = new HashMap<String, List<SimpleAsset>>();
		List<SimpleAsset> unassignedAssets = getUnAssignedAssetsByServiceCityAndAssetType(serviceTypeId,cityGeoId, assetTypeId);
		List<SimpleAsset> assingedAssets =  getUserAssignedAssets(null,userId, serviceTypeId, assetTypeId, cityGeoId);
		allAssets.put("unassignedAssets",unassignedAssets);
		allAssets.put("assignedAssets",assingedAssets);
		return allAssets;
	}

	@RequestMapping(value="/user/getUserAssignedAndUnassignedAssets",produces="application/json")
	public @ResponseBody Map<String,List<SimpleAsset>> getUserAssignedAndUnassignedAssets(@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser,@RequestParam String userId,@RequestParam String serviceTypeId,@RequestParam String assetTypeId,@RequestParam String cityGeoId){
		Map<String,List<SimpleAsset>> allAssets = new HashMap<String, List<SimpleAsset>>();
		List<SimpleAsset> unassignedAssets = getUnAssignedAssetsByServiceCityAndAssetType(customUser,serviceTypeId,cityGeoId, assetTypeId);
		List<SimpleAsset> assingedAssets =  getUserAssignedAssets(customUser,userId, serviceTypeId, assetTypeId, cityGeoId);
		allAssets.put("unassignedAssets",unassignedAssets);
		allAssets.put("assignedAssets",assingedAssets);
		return allAssets;
	}

	@Transactional
	@RequestMapping("/admin/saveVendorAssetMapping")
	public String saveVendorAssetMapping(@Valid @ModelAttribute("formObject") VendorServiceAsset formObject,BindingResult result,Model model){

		//check if the service and asset is assigned to any other vendor...  
		if(!result.hasErrors()){
			validateExistingVendorServiceAssetAssignment(formObject,result);
		}
		//save by vendor, service, city and assetType
		if(!result.hasErrors()){
			User vendorUser = new User();
			vendorUser.setUserId(formObject.getUserId());
			ServiceType serviceType = new ServiceType();
			serviceType.setServiceTypeId(formObject.getServiceTypeId());
			//find existing assets
			Query existingVendorAssetMap = entityManager.createQuery(VENDOR_ASSET_IDS_BY_SERVICE_REGION_ASSET_TYPE_SELECT_FIELDS+VENDOR_ASSET_BY_SERVICE_REGION_ASSET_TYPE_FROM_CLAUSE);
			existingVendorAssetMap.setParameter("userId", formObject.getUserId());
			existingVendorAssetMap.setParameter("serviceTypeId", Integer.valueOf(formObject.getServiceTypeId()));
			existingVendorAssetMap.setParameter("cityGeoId", formObject.getCityGeoId());
			existingVendorAssetMap.setParameter("assetTypeId", Integer.valueOf(formObject.getAssetTypeId()));
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
			
			//delete any unassigned assets with user and service of a the current site...
			//get all assets of the selected site
			List<Integer> allSiteAssetsToDelete = new ArrayList<Integer>(assetsMapped);
			allSiteAssetsToDelete.removeAll(formObject.getAssets());
			//not identify any assets of this site not selected delete from mapping
			if(allSiteAssetsToDelete.size()>0){
				Query deleteVendorAssetMap = entityManager.createNativeQuery("DELETE vsa FROM vendor_service_asset vsa, asset a, site s WHERE vsa.assetId=a.assetId AND a.siteId=s.siteId AND vsa.userId=(?1) AND vsa.serviceTypeId=(?2) AND a.assetTypeId=(?3) AND s.city=(?4) AND vsa.assetId IN (?5)");
				deleteVendorAssetMap.setParameter(1, formObject.getUserId());
				deleteVendorAssetMap.setParameter(2, formObject.getServiceTypeId());
				deleteVendorAssetMap.setParameter(3, Integer.valueOf(formObject.getAssetTypeId()));
				deleteVendorAssetMap.setParameter(4, formObject.getCityGeoId());
				deleteVendorAssetMap.setParameter(5, allSiteAssetsToDelete);
				int deletes = deleteVendorAssetMap.executeUpdate();
				_logger.debug("Deleted unassigned vendor asset mapping :"+deletes);
			}
			model.addAttribute("saved","Saved asset mapping successfully for vendor: "+formObject.getUserId());
		}
		//get users if organization is selected
		if(formObject.getOrganizationId()!=null && formObject.getOrganizationId()>0){
			List vendors = fetchQueriedObjects("getVendorsForOrganization", "organizationId",formObject.getOrganizationId());
			model.addAttribute("getVendorsForOrganization",vendors);
		}
		makeUIData(entityManager,model,"VendorServiceAsset");
		return DefaultService.ADMIN+"viewVendorAssetMapping";
	}

	@Transactional
	@RequestMapping("/user/saveVendorAssetMapping")
	public String saveVendorAssetMapping(@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser,@Valid @ModelAttribute("formObject") VendorServiceAsset formObject,BindingResult result,Model model){

		//check if the service and asset is assigned to any other vendor...  
		if(!result.hasErrors()){
			validateExistingVendorServiceAssetAssignment(formObject,result);
		}

		//save by vendor, service, city and assetType
		if(!result.hasErrors()){
			User vendorUser = new User();
			vendorUser.setUserId(formObject.getUserId());
			ServiceType serviceType = new ServiceType();
			serviceType.setServiceTypeId(formObject.getServiceTypeId());
			//find existing assets
			JPASQLQuery existingVendorAssetQuery = getUserAssignedAssetsQuery(customUser, formObject);
			List<Integer> assetsMapped = existingVendorAssetQuery.list(sqlVendorServiceAsset.assetId);

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
			
			//delete any unassigned assets with user and service of a the current site...
			//get all assets of the selected site
			List<Integer> allSiteAssetsToDelete = new ArrayList<Integer>(assetsMapped);
			allSiteAssetsToDelete.removeAll(formObject.getAssets());
			//not identify any assets of this site not selected delete from mapping
			if(allSiteAssetsToDelete.size()>0){
				StringBuilder bulkDelete = new StringBuilder("DELETE vsa FROM vendor_service_asset vsa, asset a, site s,client_user_site cus WHERE vsa.assetId=a.assetId AND a.siteId=s.siteId AND s.siteId=cus.siteId AND vsa.userId=(?1) AND vsa.serviceTypeId=(?2) AND a.assetTypeId=(?3) AND s.city=(?4) AND vsa.assetId IN (?5)");
				if(customUser!=null){
					bulkDelete.append(" AND cus.userId=(?6)");
				}
				Query deleteVendorAssetMap = entityManager.createNativeQuery(bulkDelete.toString());
				deleteVendorAssetMap.setParameter(1, formObject.getUserId());
				deleteVendorAssetMap.setParameter(2, formObject.getServiceTypeId());
				deleteVendorAssetMap.setParameter(3, Integer.valueOf(formObject.getAssetTypeId()));
				deleteVendorAssetMap.setParameter(4, formObject.getCityGeoId());
				deleteVendorAssetMap.setParameter(5, allSiteAssetsToDelete);
				if(customUser!=null){
					deleteVendorAssetMap.setParameter(6, customUser.getUsername());
				}
				int deletes = deleteVendorAssetMap.executeUpdate();
				_logger.debug("Deleted unassigned vendor asset mapping :"+deletes);
			}
			model.addAttribute("saved","Saved asset mapping successfully for vendor: "+formObject.getUserId());
		}
		//get users if organization is selected
		if(formObject.getOrganizationId()!=null && formObject.getOrganizationId()>0){
			List vendors = fetchQueriedObjects("getVendorsForOrganization", "organizationId",formObject.getOrganizationId());
			model.addAttribute("getVendorsForOrganization",vendors);
		}
		makeUIData(entityManager,model,"VendorServiceAsset");
		return DefaultService.ADMIN+"viewVendorAssetMapping";
	}

	private void validateExistingVendorServiceAssetAssignment(VendorServiceAsset formObject, BindingResult result) {
		//ensure all parameters are selected to continue assigments
		if(SecureTUtils.isEmpty(formObject.getUserId())){
			FieldError fieldError = new FieldError("formObject", "userId", "Please select the vendor");
			result.addError(fieldError);
		}
		if(formObject.getServiceTypeId()==0){
			FieldError fieldError = new FieldError("formObject", "serviceTypeId", "Please select the service type");
			result.addError(fieldError);
		}
		if(SecureTUtils.isEmpty(formObject.getCityGeoId())){
			FieldError fieldError = new FieldError("formObject", "cityGeoId", "Please select the region");
			result.addError(fieldError);
		}
		if(SecureTUtils.isEmpty(formObject.getAssetTypeId())){
			FieldError fieldError = new FieldError("formObject", "assetTypeId", "Please select the asset type");
			result.addError(fieldError);
		}
		
		Query vendorsAssignedQuery =  entityManager.createNamedQuery("getAssetNameMappedByVendorAssetServiceType");
		vendorsAssignedQuery.setParameter("assetId", formObject.getAssets());
		vendorsAssignedQuery.setParameter("serviceTypeId", formObject.getServiceTypeId());
		vendorsAssignedQuery.setParameter("userId", formObject.getUserId());
		List<String> assetsMapped = vendorsAssignedQuery.getResultList();
		if(assetsMapped.size()>0){
			FieldError fieldError = new FieldError("formObject", "serviceTypeId", "Following assets have been mapped to vendor already: "+assetsMapped.toString());
			result.addError(fieldError);
		}
	}

	@Transactional
	@RequestMapping("/admin/transferVendorAssetMapping")
	public String transferVendorAssetMapping(@RequestParam(value="transferFromUserId",required=false) String transferFromUserId,@RequestParam(value="transferToUserId",required=false) String transferToUserId,@RequestParam(value="serviceTypeId",required=false) String serviceTypeIdStr,Model model){
		if(!SecureTUtils.isEmpty(transferFromUserId) && !SecureTUtils.isEmpty(transferToUserId) && !SecureTUtils.isEmpty(serviceTypeIdStr)){
			JPASQLQuery toUserAssets = new JPASQLQuery(entityManager, sqlTemplates);

			int serviceTypeId = Integer.parseInt(serviceTypeIdStr);
			List<Integer> fromUserAssetIds =  toUserAssets.from(sqlVendorServiceAsset)
					.where(sqlVendorServiceAsset.userId.eq(transferToUserId).and(sqlVendorServiceAsset.serviceTypeId.eq(serviceTypeId )))
					.list(sqlVendorServiceAsset.assetId);

			JPAUpdateClause updateVendorAssets =  new JPAUpdateClause(entityManager,vendorServiceAsset);
			updateVendorAssets.set(vendorServiceAsset.vendorUser.userId, transferToUserId)
			.where(vendorServiceAsset.vendorUser.userId.eq(transferFromUserId).and(vendorServiceAsset.asset.assetId.notIn(fromUserAssetIds)).
					and(vendorServiceAsset.serviceType.serviceTypeId.eq(serviceTypeId))
			);

			long assetsTransferred = updateVendorAssets.execute();
			entityManager.flush();
			model.addAttribute("saved", "Transfered "+assetsTransferred+ " assets from "+ transferFromUserId +" to " + transferToUserId);
			
		}else{
			FieldError error = new FieldError("formObject","transferError", "Please select From, To User and Service Type to initiate transfer");
			model.addAttribute("transferError", error);
		}
		viewVendorAssetMapping(model);
		return DefaultService.ADMIN+"viewVendorAssetMapping";
	}

	@Transactional
	@RequestMapping("/user/transferVendorAssetMapping")
	public String transferVendorAssetMapping(@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser,@RequestParam(value="transferFromUserId",required=false) String transferFromUserId,@RequestParam(value="transferToUserId",required=false) String transferToUserId,@RequestParam(value="serviceTypeId",required=false) String serviceTypeIdStr,Model model){
		if(!SecureTUtils.isEmpty(transferFromUserId) && !SecureTUtils.isEmpty(transferToUserId) && !SecureTUtils.isEmpty(serviceTypeIdStr)){
			JPASQLQuery toUserAssets = new JPASQLQuery(entityManager, sqlTemplates);

			int serviceTypeId = Integer.parseInt(serviceTypeIdStr);
/*			List<Integer> fromUserAssetIds =  toUserAssets.from(sqlVendorServiceAsset)
					.where(sqlVendorServiceAsset.userId.eq(transferToUserId).and(sqlVendorServiceAsset.serviceTypeId.eq(serviceTypeId )))
					.list(sqlVendorServiceAsset.assetId);

			JPAUpdateClause updateVendorAssets =  new JPAUpdateClause(entityManager,vendorServiceAsset);
			updateVendorAssets.set(vendorServiceAsset.vendorUser.userId, transferToUserId)
			.where(vendorServiceAsset.vendorUser.userId.eq(transferFromUserId).and(vendorServiceAsset.asset.assetId.notIn(fromUserAssetIds)).
					and(vendorServiceAsset.serviceType.serviceTypeId.eq(serviceTypeId))
			);

			long assetsTransferred = updateVendorAssets.execute();
*/			
			StringBuilder bulkUpdate = new StringBuilder("UPDATE vendor_service_asset vsa, asset a, site s, client_user_site cus SET vsa.userId=(?1) WHERE vsa.assetId=a.assetId AND a.siteId=s.siteId AND s.siteId=cus.siteId AND vsa.userId=(?2) AND vsa.serviceTypeId=(?3)");
			if(customUser!=null){
				bulkUpdate.append(" AND cus.userId=(?4)");
			}
			Query transferVendorAssetMap = entityManager.createNativeQuery(bulkUpdate.toString());
			transferVendorAssetMap.setParameter(1, transferToUserId);
			transferVendorAssetMap.setParameter(2, transferFromUserId);
			transferVendorAssetMap.setParameter(3, serviceTypeId);
			if(customUser!=null){
				transferVendorAssetMap.setParameter(4, customUser.getUsername());
			}
			
			long assetsTransferred = transferVendorAssetMap.executeUpdate();
			entityManager.flush();
			model.addAttribute("saved", "Transfered "+assetsTransferred+ " assets from "+ transferFromUserId +" to " + transferToUserId);
		}else{
			FieldError error = new FieldError("formObject","transferError", "Please select From, To User and Service Type to initiate transfer");
			model.addAttribute("transferError", error);
		}
		viewVendorAssetMapping(model);
		return DefaultService.ADMIN+"viewVendorAssetMapping";
	}
}