package com.securet.ssm.services.admin;

import java.sql.Timestamp;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.jpa.sql.JPASQLQuery;
import com.mysema.query.sql.SQLSubQuery;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.types.ArrayConstructorExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.Projections;
import com.mysema.query.types.expr.DateTimeOperation;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.template.StringTemplate;
import com.securet.ssm.persistence.objects.SecureTObject;
import com.securet.ssm.persistence.objects.Site;
import com.securet.ssm.persistence.objects.User;
import com.securet.ssm.persistence.objects.querydsl.jpa.JPAClientUserSite;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLClientUserSite;
import com.securet.ssm.services.DefaultService;
import com.securet.ssm.services.SecureTService;
import com.securet.ssm.services.vo.ClientUserSite;
import com.securet.ssm.utils.SecureTUtils;

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
			dataViewNames.add("getAllClientUsers");
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
	
	@RequestMapping(value="/admin/getUserAssignedAndUnAssignedSites",produces="application/json")
	public @ResponseBody Map<String,List> getUserAssignedAndUnAssignedSites(@RequestParam String userId, @RequestParam String cityGeoId){
		Query userSitesAssignedByRegion =  entityManager.createNamedQuery("getUserAssignedSitesByRegion");
		userSitesAssignedByRegion.setParameter("cityGeoId", cityGeoId);
		userSitesAssignedByRegion.setParameter("userId", userId);
		List userAssignedSites = userSitesAssignedByRegion.getResultList();
		Query userUnAssignedByRegion =  entityManager.createNamedQuery("getUserUnAssignedSitesByRegion");
		userUnAssignedByRegion.setParameter(1, cityGeoId);
		userUnAssignedByRegion.setParameter(2, userId);
		//userUnAssignedByRegion.setMaxResults(1000);
		List userUnAssignedRegionSites = userUnAssignedByRegion.getResultList();
		Map<String,List> siteMapping = new HashMap<String, List>(); 
		siteMapping.put("userAssignedSites", userAssignedSites);
		siteMapping.put("allRegionSites", userUnAssignedRegionSites);
		return siteMapping;
	}

	@Transactional
	@RequestMapping("/admin/saveClientUserSiteMapping")
	public String saveClientUserSiteMapping(@Valid @ModelAttribute("formObject") ClientUserSite formObject,BindingResult result,Model model){
		if(!result.hasErrors()){
			User clientUser = new User();
			clientUser.setUserId(formObject.getUserId());
			//do not persist existing assignments as they exist.
			Query existingAssignments = entityManager.createQuery("SELECT cus.site.siteId FROM ClientUserSite cus WHERE cus.clientUser.userId=:userId AND cus.site.city.geoId=:cityGeoId");
			existingAssignments.setParameter("userId", formObject.getUserId());
			existingAssignments.setParameter("cityGeoId", formObject.getCityGeoId());
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

			//remove unmapped sites...for the user selected criteria..   
			List<Integer> allSitesToDelete = new ArrayList<Integer>(existingSites);
			allSitesToDelete.removeAll(formObject.getSites());
			if(allSitesToDelete.size()>0){
				Query deleteOldQuery = entityManager.createNativeQuery("DELETE cus FROM client_user_site cus,site s WHERE cus.siteId=s.siteId  AND cus.userId=(?1) AND s.city=(?2) AND cus.siteId IN (?3)");
				deleteOldQuery.setParameter(1, formObject.getUserId());
				deleteOldQuery.setParameter(2, formObject.getCityGeoId());
				deleteOldQuery.setParameter(3, allSitesToDelete);
				int deletes = deleteOldQuery.executeUpdate();
				_logger.info("Deleted "+ deletes+ "ClientUserMapping on save");
			}
			model.addAttribute("saved", "Saved site mapping successfully for user: "+formObject.getUserId());
		}
/*		if(formObject.getOrganizationId()!=null && formObject.getOrganizationId()>0){
			List clientUsers = fetchQueriedObjects( "getUsersForOrganization", "organizationId",formObject.getOrganizationId());
			model.addAttribute("getUsersForOrganization", clientUsers);
		}
*/		
		makeUIData(entityManager,model,"ClientUserSite");
		return DefaultService.ADMIN+"viewClientUserSites";
	}
	
	@Transactional
	@RequestMapping("/admin/transferClientUserSiteMapping")
	public String transferClientUserSiteMapping(@RequestParam("transferFromUserId") String transferFromUserId,@RequestParam("transferToUserId") String transferToUserId, @RequestParam(value="replicate",required=false)boolean replicate,Model model){
		if(!SecureTUtils.isEmpty(transferFromUserId) && !SecureTUtils.isEmpty(transferToUserId)){
			JPASQLQuery toUserSites = new JPASQLQuery(entityManager, sqlTemplates);
			JPAClientUserSite clientUserSite = JPAClientUserSite.clientUserSite;
			SQLClientUserSite sqlClientUserSite = SQLClientUserSite.clientUserSite;
			String message = null;
			if(replicate){
				SQLSubQuery sourceQuery = new SQLSubQuery();
				SQLSubQuery transferToSubQuery = new SQLSubQuery();
				SQLClientUserSite transferToSites = new SQLClientUserSite("transferToSites");
				
				transferToSubQuery.from(transferToSites).where(sqlClientUserSite.userId.eq(transferToUserId));

				StringBuilder sb = new StringBuilder();
				sb.append("\"").append(transferToUserId).append("\"");
				
				sourceQuery.from(sqlClientUserSite)
				.leftJoin(transferToSites).on(sqlClientUserSite.siteId.eq(transferToSites.siteId).and(sqlClientUserSite.userId.eq(transferFromUserId)).and(transferToSites.userId.eq(transferToUserId)))
				.where(sqlClientUserSite.userId.eq(transferFromUserId).and(transferToSites.siteId.isNull()));
				ArrayConstructorExpression<Object> sourceFieldExpr = Projections.array(Object[].class, (SimpleExpression)DateTimeOperation.currentTimestamp(Timestamp.class),(SimpleExpression)DateTimeOperation.currentTimestamp(Timestamp.class),(SimpleExpression)sqlClientUserSite.siteId,
						(SimpleExpression)StringTemplate.create(sb.toString()));
				ListSubQuery<Object[]> transferClientUserSiteList = sourceQuery.list(sourceFieldExpr);

				//Avoiding use of sql connection, instead fetch the query constructed and run as a native query..
				SQLInsertClause sqlInsertClause = new SQLInsertClause(null, sqlTemplates, sqlClientUserSite);
				sqlInsertClause.select(transferClientUserSiteList);
				String queryToExecute = sqlInsertClause.getSQL().get(0).getSQL();
				
				Query replicateQuery = entityManager.createNativeQuery(queryToExecute);
				replicateQuery.setParameter(1, transferFromUserId);
				replicateQuery.setParameter(2, transferToUserId);
				replicateQuery.setParameter(3, transferFromUserId);
				int sitesReplicated = replicateQuery.executeUpdate();
				message = "Replicated "+sitesReplicated+ " sites from "+ transferFromUserId +" to " + transferToUserId;

			}else{
				List<Integer> fromUserSiteIds =  toUserSites.from(sqlClientUserSite).where(sqlClientUserSite.userId.eq(transferToUserId)).list(sqlClientUserSite.siteId);
				JPAUpdateClause updateClientUserSite =  new JPAUpdateClause(entityManager,JPAClientUserSite.clientUserSite);
				updateClientUserSite.set(clientUserSite.clientUser.userId, transferToUserId);
				updateClientUserSite.where(clientUserSite.clientUser.userId.eq(transferFromUserId).and(clientUserSite.site.siteId.notIn(fromUserSiteIds)));
				long sitesTransferred = updateClientUserSite.execute();
				message = "Transfered "+sitesTransferred+ " sites from "+ transferFromUserId +" to " + transferToUserId;
			}
			entityManager.flush();
			model.addAttribute("saved", message);
			
		}else{
			FieldError error = new FieldError("formObject","transferError", "Please select both from and to user to initiate transfer");
			model.addAttribute("transferError", error);
		}
		viewClientUserSite(model);
		return DefaultService.ADMIN+"viewClientUserSites";
	}
}