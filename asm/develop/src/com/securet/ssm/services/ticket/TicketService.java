package com.securet.ssm.services.ticket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.securet.ssm.components.authentication.SecureTAuthenticationSuccessHandler;
import com.securet.ssm.components.mail.MailService;
import com.securet.ssm.components.sms.SMSService;
import com.securet.ssm.persistence.objects.SecureTObject;
import com.securet.ssm.persistence.objects.Site;
import com.securet.ssm.persistence.objects.Ticket;
import com.securet.ssm.persistence.views.SimpleSite;
import com.securet.ssm.services.DefaultService;
import com.securet.ssm.services.admin.AdminService;
import com.securet.ssm.services.vo.DataTableCriteria;
import com.securet.ssm.services.vo.HPToolInput;
import com.securet.ssm.services.vo.DataTableCriteria.ColumnCriterias;
import com.securet.ssm.services.vo.ListObjects;

@Controller
@Repository
@Service
public class TicketService extends BaseTicketService {


	private static final Logger _logger = LoggerFactory.getLogger(TicketService.class);
	private static List<String> dataViewNames = null;  


	public static List<String> getDataViewNames() {
		if(dataViewNames==null){
			dataViewNames=new ArrayList<String>();
			dataViewNames.add("getServiceTypeForView");
		}
		return dataViewNames;
	}
	
	@RequestMapping("/tickets/newTicket")
	public String newTicket(Model model,@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser){
		String userId = customUser.getUsername();
		model.addAttribute("formObject",new Ticket());
		loadDefaults(model, userId);
		makeUIData(entityManager,model,"Ticket");
		return DefaultService.TICKET+"newTicket";
	}

	private void loadDefaults(Model model, String userId) {
		//List<SecureTObject> userAssignedSites = fetchQueriedObjects("getUserAssignedSites", "userId", userId);
		List<SecureTObject> serviceTypes = entityManager.createNamedQuery("getServiceTypeForView").getResultList();
		List<SecureTObject> severity = entityManager.createNamedQuery("getSeverityForView").getResultList();
		//model.addAttribute("userAssignedSites", userAssignedSites);
		model.addAttribute("serviceTypes", serviceTypes);
		model.addAttribute("severity", severity);
	}

	@RequestMapping(value="/tickets/getVendorsAndIssueTypes",produces="application/json")
	public @ResponseBody Map<String,Object> getVendorsAndIssueTypes(@RequestParam String serviceTypeId, @RequestParam String siteId){
		int serviceTypeIdInt=0;
		int siteIdInt=0;
		try{
			serviceTypeIdInt = Integer.parseInt(serviceTypeId);
			siteIdInt = Integer.parseInt(siteId);
		}catch(NumberFormatException e){
			_logger.error("Incorrect site/serviceType id :"+siteId+"/"+serviceTypeId,e);
		}
		return loadVendorsAndIssueTypes(serviceTypeIdInt, siteIdInt);
	}


	@RequestMapping("/tickets/listTickets")
	public String listTickets(@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser,@RequestParam(value="filterStatus",required=false) String filterStatus,Model model){
		boolean isReporter = !customUser.getAuthorities().contains(SecureTAuthenticationSuccessHandler.resolverAuthority);
		model.addAttribute("isReporter", isReporter);
		model.addAttribute("userName", customUser.getUsername());
		if(filterStatus!=null && !filterStatus.isEmpty()){
			model.addAttribute("filterStatus", filterStatus);
		}
		return DefaultService.TICKET+"listTickets";
	}

	@Transactional
	@RequestMapping(value="/tickets/listUserTickets",produces="application/json")
	public @ResponseBody ListObjects listUserTickets(@ModelAttribute DataTableCriteria columns,@RequestParam(value="filterStatus",required=false) String filterStatus,@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser){
		return listUserTickets(columns, filterStatus, customUser,true);
	}

	@Transactional
	@RequestMapping(value="/tickets/listAllTickets",produces="application/json")
	public @ResponseBody ListObjects listUserTickets(@ModelAttribute DataTableCriteria columns, @RequestParam(value="filterStatus",required=false) String filterStatus){
		return listUserTickets(columns, filterStatus, null,true);
	}

	
	@Transactional
	@RequestMapping(value="/tickets/saveTicket",method=RequestMethod.POST)
	public String saveTicket(@RequestParam(required=false) List<MultipartFile> ticketAttachments,@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser,@Valid @ModelAttribute("formObject") Ticket formObject, BindingResult result,Model model){
		validateAndSetDefaultsForTicket("formObject",formObject,result);		
		if(!result.hasErrors()){
			createTicketAndNotify(formObject,ticketAttachments, customUser,getMailService(),getSmsService());
			model.addAttribute("saved", true);
		}else{
			loadDefaults(model, customUser.getUsername());
			//preload all the necessary data...
			preloadTicketForm(formObject, model);
			return DefaultService.TICKET+"newTicket";
		}
		//all is fine.. load the user tickets
		return listTickets(customUser,null, model);
	}


	@Transactional
	@RequestMapping("/tickets/modifyTicket")
	public String editTicket(@RequestParam("id") String ticketId,@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser, Model model){
		//find if the ticket is part of the user or his organization group and then allow them to edit
		Ticket currentTicket = getUserTicket(ticketId, customUser,getMailService(),getSmsService());
		if(currentTicket==null){
			return listTickets(customUser,null,model);
		}
		return loadEditTicketModel(model, currentTicket);
	}


	private String loadEditTicketModel(Model model, Ticket currentTicket) {
		//also load the archive...
		List<SecureTObject> ticketArchives = fetchQueriedObjects("getLatestTicketArchivesForTicketId", "ticketId", currentTicket.getTicketId());
		model.addAttribute("ticketArchives", ticketArchives);// pagination???
		model.addAttribute("formObject",currentTicket);
		return DefaultService.TICKET+"modifyTicket";
	}

	@Transactional
	@RequestMapping(value="/tickets/updateTicket",method=RequestMethod.POST)
	public String updateTicket(@RequestParam(required=false) List<MultipartFile> ticketAttachments,@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser,@Valid @ModelAttribute("formObject") Ticket formObject, BindingResult result,Model model){
		if(formObject.getStatus()==null || formObject.getStatus().getEnumerationId().isEmpty()){
			FieldError fieldError = new FieldError("formObject", "status.enumerationId", "Please select a valid status");
			result.addError(fieldError);
		}
		if(!result.hasErrors()){
			updateTicketAndNotify(formObject,ticketAttachments, customUser,getMailService(),getSmsService());
		}else{
			String responseString = editTicket(formObject.getTicketId(), customUser, model);
			return responseString;
		}
		return listTickets(customUser,null, model);
	}

	
	private void preloadTicketForm(Ticket formObject, Model model) {
		int siteId = formObject.getSite().getSiteId();
		int serviceTypeId = formObject.getServiceType()!=null?formObject.getServiceType().getServiceTypeId():0;
		boolean siteExists = siteId>0;
		boolean serviceTypeExists = serviceTypeId>0;
		if(siteExists){
			Query siteQuery = entityManager.createNamedQuery("getSiteById");
			siteQuery.setParameter("id", siteId);
			Site site = (Site)siteQuery.getSingleResult();
			model.addAttribute("selectedSite", site);
		}
		if(siteExists && serviceTypeExists){
			//load the user and issue types
			Map<String,Object> vendorsAndIssueTypes = loadVendorsAndIssueTypes(serviceTypeId, siteId);
			model.addAllAttributes(vendorsAndIssueTypes);
		}
	}

	
	private Map<ColumnCriterias,String> addSearchableFieldToColumnCriteria(String fieldPath, String value){
		Map<ColumnCriterias,String> userColumn = new HashMap<DataTableCriteria.ColumnCriterias, String>();
		userColumn.put(ColumnCriterias.data, fieldPath);
		userColumn.put(ColumnCriterias.searchable,"true");
		userColumn.put(ColumnCriterias.searchValue,value);
		return userColumn;
	}
	
	@RequestMapping(value="/tickets/searchSites",produces="application/json")
	public @ResponseBody List<SimpleSite> searchSitesForTickets(@RequestParam String searchString,@RequestParam int resultsSize,@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser){
		List<SimpleSite> sites = searchSites(searchString, resultsSize, customUser);
		return sites;
	}


	@RequestMapping(value="/tickets/hpToolTrigger")
	public String hpToolTriggerForm(@ModelAttribute("hpInput") HPToolInput hpToolInput,Model model, BindingResult result){
		model.addAttribute("hpToolInput", hpToolInput);
		return DefaultService.TICKET+"hpToolTrigger";
	}
	
	@Transactional
	@RequestMapping(value="/tickets/hpToolTriggerSubmit",method=RequestMethod.POST)
	public String hpToolTrigger(@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser,@ModelAttribute("hpInput") HPToolInput hpToolInput,@RequestParam("trigger") String trigger,Model model, BindingResult result){
		SimpleDateFormat sdf = new SimpleDateFormat(MDDYYYY_HHMMSS_A);
		if(trigger.equals("end cashout")){
			String endedAt = sdf.format(new Date());
			hpToolInput.setEndedAt(endedAt);
		}else{
			String startedAt = sdf.format(new Date());
			hpToolInput.setStartedAt(startedAt);
		}
		
		try{
			Ticket ticket = (Ticket)parseHPToolMessage(customUser, hpToolInput, result);
			if(ticket.getTicketId()!=null){
				model.addAttribute("saved", "Saved the trigger ticketId:"+ticket.getTicketId());
			}
		}catch(Exception e){
			_logger.error("Could not trigger HP input",e);
			model.addAttribute("error", "Incorrect Input!");
		}
		
		return DefaultService.TICKET+"hpToolTrigger";
	}
	
}