package com.securet.ssm.spring;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.securet.ssm.services.ticket.TicketService;

/**
 * Handles decorator requests from SiteMesh.
 */
@Controller
public class SiteMeshController {

	@PersistenceContext(type=PersistenceContextType.TRANSACTION)
	protected EntityManager entityManager;

	
	public void setEntityManager(EntityManager entityManager){
		this.entityManager=entityManager;
	}
	
	/**
	 * Map all SiteMesh decorator requests.  Note that we use the pattern
	 * \\w+ to prevent an outside source from getting access to files we might
	 * not want made available.
	 */
	@RequestMapping("decorator/{name:\\w+}.ftl")
	public String decorator(@PathVariable String name, ModelMap map,@AuthenticationPrincipal User user,HttpServletRequest request) {
		// Make the current date available to our templates.
		// We can put anything else we want in here.
		map.put("now", new Date());
		map.put("serverName",request.getServerName());
		map.put("port",request.getServerPort());
		if(name.equals("clientlayout")){
			TicketService.fetchTicketStats(entityManager,user,map);
		}
		return "decorator/" + name;
	}


}
