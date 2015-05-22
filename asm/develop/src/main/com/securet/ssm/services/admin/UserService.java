package com.securet.ssm.services.admin;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.securet.ssm.persistence.objects.RoleType;
import com.securet.ssm.persistence.objects.User;
import com.securet.ssm.persistence.objects.UserLogin;
import com.securet.ssm.services.DefaultService;
import com.securet.ssm.services.SecureTService;
import com.securet.ssm.utils.SecureTUtils;

@Controller
@Repository
@Service
public class UserService extends SecureTService {

	private static final Logger _logger = LoggerFactory.getLogger(UserService.class);
	
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
			//uses custom form
			excludeInDisplay.add("roles");
			excludeInDisplay.add("rolesList");
			excludeInDisplay.add("userLogin");
			excludeInDisplay.add("permissions");
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
	
	public static List<String> getDataViewNames() {
		if(dataViewNames==null){
			dataViewNames=new ArrayList<String>();
			dataViewNames.add("getOrganizationForView");
			dataViewNames.add("getRoleTypeForView");
		}
		return dataViewNames;
	}

	@RequestMapping("/admin/createEditUser")
	public String createEditUser(@RequestParam(required=false) String id,Model model){
		User ssmObject = null;
		if(id!=null && !id.isEmpty()){
			Query query = entityManager.createNamedQuery("getUserById");
			query.setParameter("id", id);
			ssmObject= (User)query.getSingleResult();
			List<String> rolesList = new ArrayList<String>();
			for(RoleType roleType : ssmObject.getRoles()){
				rolesList.add(String.valueOf(roleType.getRoleTypeId()));
			}
			ssmObject.setRolesList(rolesList);
			model.addAttribute("mode","edit");
		}else{
			ssmObject = new User();
		}
		makeUIData(entityManager, ssmObject, model);
		model.addAttribute("entityName","User");
		model.addAttribute("formObject",ssmObject);
		return DefaultService.ADMIN+"createEditUser";
	}

	@RequestMapping(value="/admin/saveUser",method=RequestMethod.POST)
	@Transactional
	public String saveUser(@Valid @ModelAttribute("formObject") User formObject,BindingResult result,Model model){
		String verifyPassword = (formObject.getUserLogin()!=null)?formObject.getUserLogin().getVerifyPassword():null;
		boolean createNew = verifyPassword!=null;
		if(createNew && formObject.getUserId()!=null){
			validateUser(formObject, result, verifyPassword);
		}else{
			model.addAttribute("mode","edit");
		}
		if(result.hasErrors()){
			makeUIData(entityManager,formObject,model);
			model.addAttribute("formObject",formObject);
			return DefaultService.ADMIN+"createEditUser";
		}else{
			//map the roles
			if(formObject.getRolesList()!=null && !formObject.getRolesList().isEmpty()){
				List<RoleType> roles = new ArrayList<RoleType>();
				for(String roleTypeId : formObject.getRolesList()){
					RoleType roleType = new RoleType();
					roleType.setRoleTypeId(Integer.valueOf(roleTypeId));
					roles.add(roleType);
				}
				formObject.setRoles(roles);
			}
		}

		//convert the password and store if new user and assign the user Id to login
		if(createNew){
			String password = formObject.getUserLogin().getPassword();
			password = SecureTUtils.bCryptText(password);
			formObject.getUserLogin().setPassword(password);
			formObject.getUserLogin().setUserId(formObject.getUserId());
			formObject.getUserLogin().setUser(formObject);
		}
		
		adminService.saveObject(formObject, result, model,createNew);
		StringBuilder savedMessage = new StringBuilder();//we should start using resource bundles...
		if(createNew){
			savedMessage.append("Created");
		}else{
			savedMessage.append("Saved");
		}
		savedMessage.append(" User successfully: ").append(formObject.getUserId());
		model.addAttribute("saved", savedMessage.toString());
		return  DefaultService.ADMIN+"viewObjects";
	}

	private void validateUser(User formObject, BindingResult result, String verifyPassword) {
		Query query = null;
		query = entityManager.createNamedQuery("getUserById");
		query.setParameter("id", formObject.getUserId().toLowerCase());
		int resultsCount = query.getResultList().size();
		if(resultsCount>0){
			FieldError fieldError = new FieldError("formObject", "userId", "UserId  already exists");
			result.addError(fieldError);
		}
		if(formObject.getUserLogin().getPassword().isEmpty()){
			FieldError fieldError = new FieldError("formObject", "userLogin.password", "Password is empty");
			result.addError(fieldError);
			
		}
		if(formObject.getUserLogin().getVerifyPassword().isEmpty()){
			FieldError fieldError = new FieldError("formObject", "userLogin.verifyPassword", "Verify Password is empty");
			result.addError(fieldError);
			
		}
		if(!formObject.getUserLogin().getPassword().isEmpty() && !formObject.getUserLogin().getPassword().equals(verifyPassword)){
			FieldError fieldError = new FieldError("formObject", "userLogin.verifyPassword", "Password did not match");
			result.addError(fieldError);
		}
	}
	
	@RequestMapping("/admin/changePassword")
	public ModelAndView changePassword(@RequestParam String userId, Model model){
		if(userId!=null){
			loadUserForChangePassword(userId, model);
			return new ModelAndView(DefaultService.ADMIN+"changePassword",model.asMap());
		}
		return new ModelAndView(new RedirectView(DefaultService.ADMIN+"viewObjects?entityName=User"));
	}

	private void loadUserForChangePassword(String userId, Model model) {
		Query query = null;
		query = entityManager.createNamedQuery("getUserLoginById");
		query.setParameter("id", userId);
		UserLogin formObject = (UserLogin)query.getSingleResult();
		formObject.setPassword("");	
		formObject.setVerifyPassword("");	
		model.addAttribute("formObject", formObject);
	}

	@Transactional
	@RequestMapping("/admin/savePassword")
	public String savePassword(@Valid @ModelAttribute("formObject") UserLogin formObject,BindingResult result,Model model){
		if(!result.hasErrors()){
			if(formObject.getPassword().equals(formObject.getVerifyPassword())){
				formObject.setPassword(SecureTUtils.bCryptText(formObject.getPassword()));
				if(!formObject.getEnabled()){
					formObject.setDisabledTimestamp(new Timestamp(new Date().getTime()));
				}
				entityManager.merge(formObject);
				model.addAttribute("saved", true);
			}else{
				FieldError fieldError = new FieldError("formObject", "verifyPassword", "Password did not match");
				result.addError(fieldError); 
			}
		}
		model.addAttribute("formObject", formObject);
		return DefaultService.ADMIN+"changePassword";
	}

	@RequestMapping("/user/profile")
	public String userProfile(@AuthenticationPrincipal org.springframework.security.core.userdetails.User loggedInUser,Model model){
		model.addAttribute("isProfileEdit", true);
		return createEditUser(loggedInUser.getUsername(), model);
	}
	
	@RequestMapping(value="/user/saveUser",method=RequestMethod.POST)
	@Transactional
	public String saveUserProfile(@Valid @ModelAttribute("formObject") User formObject,BindingResult result,@AuthenticationPrincipal  org.springframework.security.core.userdetails.User loggedInUser,Model model){
		model.addAttribute("isProfileEdit", true);
		if(!loggedInUser.getUsername().equals(formObject.getUserId())){
			FieldError fieldError = new FieldError("formObject", "userId", "You cannot edit this user");
			result.addError(fieldError); 
		}else{
			Query userQuery = entityManager.createNamedQuery("getUserById");
	        userQuery.setParameter("id", loggedInUser.getUsername());
	        User user = (User)userQuery.getSingleResult();
	        ///set back the organization and roles
	        formObject.setRoles(user.getRoles());
	        formObject.setOrganization(user.getOrganization());
	        formObject.setCreatedTimestamp(user.getCreatedTimestamp());
		}
		
		saveUser(formObject, result, model);
		return createEditUser(loggedInUser.getUsername(), model);
	}

	@RequestMapping("/user/changePassword")
	public String changeUserPassword(@AuthenticationPrincipal  org.springframework.security.core.userdetails.User loggedInUser, Model model){
		model.addAttribute("isProfileEdit", true);
		String userId = loggedInUser.getUsername();
		if(userId!=null){
			loadUserForChangePassword(userId, model);
		}
		return DefaultService.ADMIN+"changePassword";
	}

	@Transactional
	@RequestMapping("/user/savePassword")
	public String saveUserPassword(@Valid @ModelAttribute("formObject") UserLogin formObject,BindingResult result,@AuthenticationPrincipal  org.springframework.security.core.userdetails.User loggedInUser,Model model){
		model.addAttribute("isProfileEdit", true);
		if(!loggedInUser.getUsername().equals(formObject.getUserId())){
			FieldError fieldError = new FieldError("formObject", "userId", "You cannot edit this user");
			result.addError(fieldError); 
		}
		return savePassword(formObject, result, model);
	}
	
	public static void saveLastLoginTimestamp(EntityManager entityManager, String userId) {
		Query userQuery = entityManager.createNamedQuery("getUserById");
        userQuery.setParameter("id", userId);
        User user = (User)userQuery.getSingleResult();
        user.getUserLogin().setLastLoginTimestamp(new Timestamp(new Date().getTime()));
        entityManager.persist(user);
	}
	
}
