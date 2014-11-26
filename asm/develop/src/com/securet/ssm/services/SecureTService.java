package com.securet.ssm.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.securet.ssm.persistence.objects.SecureTObject;
import com.securet.ssm.services.admin.AssetService;
import com.securet.ssm.services.admin.AssetTypeService;
import com.securet.ssm.services.admin.IssueTypeService;
import com.securet.ssm.services.admin.OrganizationService;
import com.securet.ssm.services.admin.ServiceTypeService;
import com.securet.ssm.services.admin.SiteService;
import com.securet.ssm.services.admin.UserService;
import com.securet.ssm.utils.SecureTUtils;

import freemarker.ext.beans.BeansWrapper;

public abstract class SecureTService {
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
}

	
	@Autowired
	private FreeMarkerConfigurer freemarkerConfig;
	
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;

	@PersistenceContext(type=PersistenceContextType.TRANSACTION)
	protected EntityManager entityManager;

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


	public void makeUIData(EntityManager entityManager, SecureTObject ssmObject, Model model) {
		String entityName = ssmObject.getClass().getSimpleName();
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
}
