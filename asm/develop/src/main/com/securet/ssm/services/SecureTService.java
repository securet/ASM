package com.securet.ssm.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.mysema.query.jpa.sql.JPASQLQuery;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import com.mysema.query.types.expr.BooleanExpression;
import com.securet.ssm.persistence.objects.SecureTObject;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLClientUserSite;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLModule;
import com.securet.ssm.persistence.objects.querydsl.sql.SQLSite;
import com.securet.ssm.persistence.views.SimpleSite;
import com.securet.ssm.services.admin.AssetService;
import com.securet.ssm.services.admin.AssetTypeService;
import com.securet.ssm.services.admin.ClientUserSiteMappingService;
import com.securet.ssm.services.admin.IssueTypeService;
import com.securet.ssm.services.admin.ModuleService;
import com.securet.ssm.services.admin.OrganizationService;
import com.securet.ssm.services.admin.ServiceSparePartService;
import com.securet.ssm.services.admin.ServiceTypeService;
import com.securet.ssm.services.admin.SiteService;
import com.securet.ssm.services.admin.UserService;
import com.securet.ssm.services.admin.VendorAssetMappingService;
import com.securet.ssm.utils.SecureTUtils;

import freemarker.ext.beans.BeansWrapper;

public abstract class SecureTService {

	public static final String ASSETS_SSMUPLOADS_LOGOS = "assets/ssmuploads/logos/";
	public static final String ASSETS_SSMUPLOADS_TICKETATTACHMENTS = "uploads/ticketattachments/";
	public static String EXCLUDE_IN_DISPLAY_SUFFIX = "ExcludeInDisplay";
	public static String CUSTOM_FIELD_TYPE_SUFFIX = "CustomFieldTypes";
	private static final String DATA_VIEWS = "DataViews";
	
	public static final Map<String,Object> uiFieldConfig = new HashMap<String, Object>();
	static{
		//Add UI config here - will have a xml based impl later - this may create cyclic dependency.. TODO- make this change quickly  
		uiFieldConfig.put("organizationExcludeInDisplay",OrganizationService.getFieldsToExcludeInDisplay());
		uiFieldConfig.put("organizationCustomFieldTypes",OrganizationService.getCustomFieldTypes());

		uiFieldConfig.put("siteExcludeInDisplay",SiteService.getFieldsToExcludeInDisplay());
		uiFieldConfig.put("siteCustomFieldTypes",SiteService.getCustomFieldTypes());
		uiFieldConfig.put("siteDataViews",SiteService.getDataViewNames());
		
		uiFieldConfig.put("serviceTypeExcludeInDisplay",ServiceTypeService.getFieldsToExcludeInDisplay());
		uiFieldConfig.put("serviceTypeCustomFieldTypes",ServiceTypeService.getCustomFieldTypes());
		
		uiFieldConfig.put("issueTypeExcludeInDisplay",IssueTypeService.getFieldsToExcludeInDisplay());
		uiFieldConfig.put("issueTypeCustomFieldTypes",IssueTypeService.getCustomFieldTypes());
		uiFieldConfig.put("issueTypeDataViews",IssueTypeService.getDataViewNames());

		uiFieldConfig.put("assetTypeExcludeInDisplay",AssetTypeService.getFieldsToExcludeInDisplay());
		uiFieldConfig.put("assetTypeCustomFieldTypes",AssetTypeService.getCustomFieldTypes());

		uiFieldConfig.put("assetExcludeInDisplay",AssetService.getFieldsToExcludeInDisplay());
		uiFieldConfig.put("assetCustomFieldTypes",AssetService.getCustomFieldTypes());
		uiFieldConfig.put("assetDataViews",AssetService.getDataViewNames());

		uiFieldConfig.put("userExcludeInDisplay",UserService.getFieldsToExcludeInDisplay());
		uiFieldConfig.put("userCustomFieldTypes",UserService.getCustomFieldTypes());
		uiFieldConfig.put("userDataViews",UserService.getDataViewNames());

		uiFieldConfig.put("clientUserSiteExcludeInDisplay",ClientUserSiteMappingService.getFieldsToExcludeInDisplay());
		uiFieldConfig.put("clientUserSiteCustomFieldTypes",ClientUserSiteMappingService.getCustomFieldTypes());
		uiFieldConfig.put("clientUserSiteDataViews",ClientUserSiteMappingService.getDataViewNames());

		uiFieldConfig.put("vendorServiceAssetExcludeInDisplay",VendorAssetMappingService.getFieldsToExcludeInDisplay());
		uiFieldConfig.put("vendorServiceAssetCustomFieldTypes",VendorAssetMappingService.getCustomFieldTypes());
		uiFieldConfig.put("vendorServiceAssetDataViews",VendorAssetMappingService.getDataViewNames());

		uiFieldConfig.put("moduleExcludeInDisplay",ModuleService.getFieldsToExcludeInDisplay());
		uiFieldConfig.put("moduleCustomFieldTypes",ModuleService.getCustomFieldTypes());
		uiFieldConfig.put("moduleDataViews",ModuleService.getDataViewNames());

		uiFieldConfig.put("serviceSparePartExcludeInDisplay",ServiceSparePartService.getFieldsToExcludeInDisplay());
		uiFieldConfig.put("serviceSparePartCustomFieldTypes",ServiceSparePartService.getCustomFieldTypes());
		uiFieldConfig.put("serviceSparePartDataViews",ServiceSparePartService.getDataViewNames());
}

	
	@Autowired
	private FreeMarkerConfigurer freemarkerConfig;
	
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;

	@PersistenceContext(type=PersistenceContextType.TRANSACTION)
	protected EntityManager entityManager;

	@Autowired//querydsl template based on database configuration..
	protected SQLTemplates sqlTemplates;

	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory){
		this.entityManagerFactory=entityManagerFactory;
	}
	
	public void setEntityManager(EntityManager entityManager){
		this.entityManager=entityManager;
	}

	public FreeMarkerConfigurer getFreemarkerConfig() {
		return freemarkerConfig;
	}

	public void setFreemarkerConfig(FreeMarkerConfigurer freemarkerConfig) {
		this.freemarkerConfig = freemarkerConfig;
	}

	public void setSqlTemplates(SQLTemplates sqlTemplates){
		this.sqlTemplates=sqlTemplates;
	}
	
	public void makeUIData(EntityManager entityManager, SecureTObject ssmObject, Model model) {
		String entityName = ssmObject.getClass().getSimpleName();
		makeUIData(entityManager, model, entityName);
	}

	public void makeUIData(EntityManager entityManager, Model model, String entityName) {
		if(!model.containsAttribute("statics")){
			model.addAttribute("statics",((BeansWrapper)freemarkerConfig.getConfiguration().getObjectWrapper()).getStaticModels());
		}
		List<String> dataViews = (List<String>) uiFieldConfig.get(SecureTUtils.decapitalize(entityName)+SecureTService.DATA_VIEWS);
		if(dataViews!=null){
			for(String viewName:dataViews){
				List<SecureTObject> viewObjects = entityManager.createNamedQuery(viewName).getResultList();
				model.addAttribute(viewName,viewObjects);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void makeModel(Model model,SecureTObject ssmObject) {
		String entityName = ssmObject.getClass().getSimpleName();
		List<String> excludeInDisplay = (List<String>) uiFieldConfig.get(SecureTUtils.decapitalize(entityName)+SecureTService.EXCLUDE_IN_DISPLAY_SUFFIX);
		Map<String, String> customFieldTypes= (Map<String, String>) uiFieldConfig.get(SecureTUtils.decapitalize(entityName)+SecureTService.CUSTOM_FIELD_TYPE_SUFFIX);
		List<SecureTUtils.FormField> fieldList = SecureTUtils.getFieldsFromClass(ssmObject.getClass(),excludeInDisplay,customFieldTypes);
		model.addAttribute("statics",((BeansWrapper)freemarkerConfig.getConfiguration().getObjectWrapper()).getStaticModels());
		model.addAttribute("entityName", entityName);
		model.addAttribute("formField", fieldList);
		model.addAttribute("formObject", ssmObject);
	}
	

	public List<SecureTObject> fetchQueriedObjects(String namedQuery, String namedParameter,Object fieldValue) {
		Query query = entityManager.createNamedQuery(namedQuery);
		query.setParameter(namedParameter, fieldValue);
		List<SecureTObject> ssmObjects = query.getResultList();
		return ssmObjects;
	}

	public List<SecureTObject> fetchQueriedObjects(String namedQuery, String namedParameter,Object fieldValue,Class clazz) {
		Query query = entityManager.createNamedQuery(namedQuery,clazz);
		query.setParameter(namedParameter, fieldValue);
		List<SecureTObject> ssmObjects = query.getResultList();
		return ssmObjects;
	}

	public SecureTObject fetchSingleObject(String namedQuery, String namedParameter,Object fieldValue) {
		Query query = entityManager.createNamedQuery(namedQuery);
		query.setParameter(namedParameter, fieldValue);
		SecureTObject ssmObjects = (SecureTObject)query.getSingleResult();
		return ssmObjects;
	}

	public List<SecureTObject> fetchObjects(String namedQuery) {
		Query query = entityManager.createNamedQuery(namedQuery);
		List<SecureTObject> ssmObjects = query.getResultList();
		return ssmObjects;
	}

	public List fetchResults(String namedQuery) {
		Query query = entityManager.createNamedQuery(namedQuery);
		List ssmObjects = query.getResultList();
		return ssmObjects;
	}
	
	public Object simplifyErrorMessages(List<FieldError> fieldErrors) {
		List<FieldError> errors = new ArrayList<FieldError>();
		for(FieldError error:fieldErrors){
			errors.add(new FieldError(error.getObjectName(), error.getField(), error.getDefaultMessage()));
		}
		return errors;
	}

	public @ResponseBody List<SimpleSite> searchSites(@RequestParam String searchString,@RequestParam int resultsSize){
		return searchSites(searchString, resultsSize,null);
	}
	
	protected List<SimpleSite> searchSites(String searchString, int resultsSize, org.springframework.security.core.userdetails.User customUser) {
		JPASQLQuery jpasqlQuery = new JPASQLQuery(entityManager, sqlTemplates);
		SQLModule sqlModule = SQLModule.module;
		SQLSite sqlSite = SQLSite.site;
		SQLClientUserSite sqlClientUserSite = SQLClientUserSite.clientUserSite;
		

		String searchStringExpr = "%"+searchString+"%";
		QBean<SimpleSite> simpleSiteExpr = Projections.fields(SimpleSite.class, sqlSite.siteId,sqlSite.name,sqlSite.area);
		
		BooleanExpression searchCriteriaExpr = sqlSite.name.like(searchStringExpr).or(sqlSite.area.like(searchStringExpr).or(sqlSite.circle.like(searchStringExpr)).or(sqlModule.name.like(searchStringExpr)));

		jpasqlQuery.from(sqlSite).innerJoin(sqlModule).on(sqlSite.moduleId.eq(sqlModule.moduleId));
		if(customUser!=null){
			jpasqlQuery.innerJoin(sqlClientUserSite).on(sqlSite.siteId.eq(sqlClientUserSite.siteId));
		}
		
		if(customUser!=null){
			searchCriteriaExpr = sqlClientUserSite.userId.eq(customUser.getUsername()).and(searchCriteriaExpr);
		}

		jpasqlQuery.where(searchCriteriaExpr);
		
		jpasqlQuery.limit(resultsSize);//show top 50 results.. 
		List<SimpleSite> sites = (List<SimpleSite>) jpasqlQuery.list(simpleSiteExpr);
		return sites;
	}
	
	
}
