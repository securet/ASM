package com.securet.ssm.services.admin;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.securet.ssm.persistence.objects.SecureTObject;
import com.securet.ssm.services.ActionHelpers;
import com.securet.ssm.services.DefaultService;
import com.securet.ssm.services.SecureTService;
import com.securet.ssm.services.vo.DataTableCriteria;
import com.securet.ssm.services.vo.ListObjects;

@Service
@Repository
@Controller
public class AdminService extends SecureTService{
	
	private static final String OBJECTS_PACKAGE = "com.securet.ssm.persistence.objects.";

	
	private static final Logger _logger = LoggerFactory.getLogger(AdminService.class); 
	

	private List<DataTableCriteria> columns;


	public List<DataTableCriteria> getColumns() {
		return columns;
	}

	public void setColumns(List<DataTableCriteria> columns) {
		this.columns = columns;
	}

	@RequestMapping("/admin/createEditObject")
	public String createEditObject(@RequestParam String entityName,@RequestParam(required=false) String id,Model model){
		SecureTObject ssmObject = null;
		if(id!=null && !id.isEmpty()){
			Query query = null;
			try{
				query = entityManager.createNamedQuery("get"+entityName+"ById");
			}catch(IllegalArgumentException e){
				_logger.error("No Such entity, check the entity name:"+entityName,e);
				return "error/404";
			}
			query.setParameter("id", Integer.valueOf(id));
			ssmObject= (SecureTObject)query.getSingleResult();
		}else{
			ssmObject = makeDefaultInstance(entityName);
		}
		makeUIData(entityManager,ssmObject,model);
		makeModel(model,ssmObject);
		return DefaultService.ADMIN+"createEditObject";
	}


	private SecureTObject makeDefaultInstance(String entityName) {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			@SuppressWarnings("unchecked")
			Class<? extends SecureTObject> ssmObjectCLass = (Class<SecureTObject>)classLoader.loadClass(OBJECTS_PACKAGE+entityName);
			return ssmObjectCLass.newInstance();
		} catch (ClassNotFoundException e) {
			_logger.error("class not found :"+OBJECTS_PACKAGE+entityName,e);
		} catch (InstantiationException e) {
			_logger.error("could not instantiate for: "+OBJECTS_PACKAGE+entityName,e);
		} catch (IllegalAccessException e) {
			_logger.error("could not access class : "+OBJECTS_PACKAGE+entityName,e);
		}
		return null;
	}

	
	public String saveObject(SecureTObject formObject, BindingResult result, Model model,boolean createNew) {
		makeModel(model, formObject);
		if(result.hasErrors()){
			makeUIData(entityManager,formObject,model);
			return DefaultService.ADMIN+"createEditObject";
		}
		if(createNew){
			entityManager.persist(formObject);
		}else{
			entityManager.merge(formObject);
		}
		_logger.debug("Saving  "+formObject.getClass().getSimpleName()+": " +formObject);
		model.addAttribute("createNew", createNew);
		return DefaultService.ADMIN+"viewObjects";
	}
	
	@Transactional
	@RequestMapping(value="/admin/listSimpleObjects",produces="application/json")
	public @ResponseBody ListObjects loadSimpleObjects(@ModelAttribute DataTableCriteria columns, @RequestParam("entityName") String entityName,@RequestParam("operator") String operator,HttpServletRequest request){
		if(_logger.isDebugEnabled())_logger.debug("load objects for "+entityName);
		return ActionHelpers.loadSimpleObjects(entityManager, columns, entityName, operator, request);
	}


	@RequestMapping("/admin/viewObjects")
	public String viewObjects(@RequestParam String entityName,Model model){
		model.addAttribute("entityName", entityName);
		makeModel(model,makeDefaultInstance(entityName));
		return DefaultService.ADMIN+"viewObjects";
	}

	@RequestMapping(value="/admin/getSitesForCity",produces="application/json")
	public @ResponseBody List<SecureTObject> getSitesForCity(@RequestParam String cityGeoId){
		String namedQuery = "getSitesForCity";
		String namedParameter = "cityGeoId";
		return fetchQueriedObjects(namedQuery, namedParameter,cityGeoId);
	}


}
