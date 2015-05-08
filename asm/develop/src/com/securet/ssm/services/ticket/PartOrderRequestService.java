package com.securet.ssm.services.ticket;

import java.util.List;

import javax.transaction.Transactional;

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

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import com.securet.ssm.persistence.objects.Enumeration;
import com.securet.ssm.persistence.objects.PartOrderRequest;
import com.securet.ssm.persistence.objects.ServiceSparePart;
import com.securet.ssm.persistence.objects.Ticket;
import com.securet.ssm.persistence.objects.User;
import com.securet.ssm.persistence.objects.querydsl.jpa.JPAServiceSparePart;
import com.securet.ssm.persistence.views.SimplePartOrderRequest;
import com.securet.ssm.persistence.views.SimpleServiceSparePart;
import com.securet.ssm.services.DefaultService;
import com.securet.ssm.utils.SecureTUtils;

@Controller
@Service
@Repository
public class PartOrderRequestService extends BaseTicketService{

	private static final String PO_INITIATED = "PO_INITIATED";
	private static final JPAServiceSparePart jpaServiceSparePart = JPAServiceSparePart.serviceSparePart;
	
	@RequestMapping(value="/tickets/newPORequest")
	public String newPORequest(@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser,@RequestParam("id") String ticketId,Model model){
		
		newPORequestDetails(customUser, ticketId, model);
		
		return DefaultService.TICKET+"newPORequest";
	}

	private void newPORequestDetails(org.springframework.security.core.userdetails.User customUser, String ticketId, Model model) {
		Ticket ticket = getUserTickets(customUser, ticketId);
		
		List<SimpleServiceSparePart> simpleServiceSpareParts = findVendorServiceSpareParts(ticket);
		model.addAttribute("vendorServiceSpareParts", simpleServiceSpareParts);
		
		if(!model.containsAttribute("partOrderRequest")){
			PartOrderRequest partOrderRequest = new PartOrderRequest();
			partOrderRequest.setTicket(ticket);
			model.addAttribute("partOrderRequest",partOrderRequest);
		}
	}

	private List<SimpleServiceSparePart> findVendorServiceSpareParts(Ticket ticket) {
		JPAQuery findVendorSpares = new JPAQuery(entityManager);
		findVendorSpares.from(jpaServiceSparePart)
		.where(jpaServiceSparePart.vendorOrganization.eq(ticket.getResolver().getOrganization()));
		QBean<SimpleServiceSparePart> simpleServiceSparePartResultExpr = Projections.bean(SimpleServiceSparePart.class, jpaServiceSparePart.sparePartId,jpaServiceSparePart.partName,jpaServiceSparePart.partDescription,jpaServiceSparePart.cost);
		List<SimpleServiceSparePart> simpleServiceSpareParts = findVendorSpares.list(simpleServiceSparePartResultExpr);
		return simpleServiceSpareParts;
	}

	@Transactional
	@RequestMapping(value="/tickets/initiatePORequest")
	public String initiatePORequest(@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser,@ModelAttribute("partOrderRequest") PartOrderRequest partOrderRequest,Model model,BindingResult result){
		if(partOrderRequest.getTicket()!=null && SecureTUtils.isEmpty(partOrderRequest.getTicket().getTicketId())){
			FieldError fieldError = new FieldError("partOrderRequest", "ticket.ticketId", "Please select spare part");
			result.addError(fieldError);
		}
		if(partOrderRequest.getServiceSparePart()!=null && partOrderRequest.getServiceSparePart().getSparePartId()==0){
			FieldError fieldError = new FieldError("partOrderRequest", "serviceSparePart.sparePartId", "Please select spare part");
			result.addError(fieldError);
		}
		
		
		if(result.hasErrors()){
			newPORequestDetails(customUser, partOrderRequest.getTicket().getTicketId(), model);
			return DefaultService.TICKET+"newPORequest";
		}else{
			
			User user = new User();
			user.setUserId(customUser.getUsername());
			partOrderRequest.setInitiatedBy(user);
			
			Enumeration status = new Enumeration();
			status.setEnumerationId(PO_INITIATED);
			partOrderRequest.setStatus(status);

			ServiceSparePart serviceSparePart = fetchServiceSparePartFromId(partOrderRequest);
			
			partOrderRequest.setCost(serviceSparePart.getCost());
			
			entityManager.persist(partOrderRequest);
			model.addAttribute("savedPORRequest","Part Order Request has be added");
			
		}
		
		//if all fine take the user to ticket page
		return editTicketDetails(partOrderRequest.getTicket().getTicketId(),customUser, model);
	}

	private ServiceSparePart fetchServiceSparePartFromId(PartOrderRequest partOrderRequest) {
		JPAQuery findSparePart = new JPAQuery(entityManager);
		findSparePart.from(jpaServiceSparePart).where(jpaServiceSparePart.sparePartId.eq(partOrderRequest.getServiceSparePart().getSparePartId()));
		ServiceSparePart serviceSparePart = findSparePart.singleResult(jpaServiceSparePart);
		return serviceSparePart;
	}
	
	@RequestMapping(value="/tickets/modifyPORequestStatus")
	public String modifyPORequestStatus(@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser,@ModelAttribute("partOrderRequest") PartOrderRequest partOrderRequest,Model model,BindingResult result){
		SimplePartOrderRequest simplPartOrderRequest = fetchSimplePartOrderRequestFromId(partOrderRequest);
		model.addAttribute("partOrderRequest", simplPartOrderRequest);
		return DefaultService.TICKET+"modifyPORequestStatus";
	}

	
	
	@Transactional
	@RequestMapping(value="/tickets/updatePORequestStatus",method=RequestMethod.POST)
	public String updatePORequestStatus(@AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser,@ModelAttribute("partOrderRequest") PartOrderRequest partOrderRequest,Model model,BindingResult result){
		if(partOrderRequest.getStatus()!=null && partOrderRequest.getStatus().getEnumDescription()!=null){
			partOrderRequest = updatePORequestStatus(customUser, partOrderRequest);
			model.addAttribute("savedPORRequest","Part Order Request has be added");
		}
		return editTicketDetails(partOrderRequest.getTicket().getTicketId(), customUser, model);
	}

	
}
