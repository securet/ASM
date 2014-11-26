package com.securet.ssm.status;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.securet.ssm.components.mail.MailService;
import com.securet.ssm.persistence.objects.Organization;
import com.securet.ssm.services.admin.OrganizationService;

@Controller
@RequestMapping("/status")
public class Ping{

	Logger _logger = LoggerFactory.getLogger(Ping.class);

	@Autowired
	private MailService mailService;

	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;
	
	@PersistenceContext(type=PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;

	private OrganizationService organizationService;
	
	public void setMailService(MailService mailService){
		this.mailService=mailService;
	}
	
	@Autowired
	public void setOrganizationalService(OrganizationService organizationService){
		this.organizationService=organizationService;
	}
	
	
	@RequestMapping("/ping")
	public String handleRequest(Model model) throws Exception {
		_logger.info("Ping ran");		
		return "ping";
	}
	
	@RequestMapping("/mailTest")
	public String mailTest(Model model) throws Exception{
		_logger.info("start sending email");
		Map<String,Object> mailContext = new HashMap<String,Object>();
		mailContext.put("to","sharadbhushank@gmail.com");
		mailContext.put("from","sharad@securet.in");
		mailContext.put("subject","Test Mail from ${name!}!");
		mailContext.put("template","test.ftl");
		Map<String,Object> bodyParameters = new HashMap<String, Object>();
		bodyParameters.put("name", "sharad");
		bodyParameters.put("company", "securet");
		mailContext.put("bodyParameters",bodyParameters);
		mailService.sendMail(mailContext);
		return "ping";
	}
	
	@RequestMapping("/entityTest")
	public String entityTest(Model model) throws Exception{
		//OrganizationService organizationService = new OrganizationService();
		//organizationService.setEntityManagerFactory(entityManagerFactory);
		//organizationService.setEntityManager(entityManager);
		Organization organization = new Organization();
		_logger.info("persist data");
		_logger.debug("persist data");
		organization.setName("securet");
		organizationService.setOrganization(organization);
		//organizationService.createOrganization();
		return "ping";
	}
	

}
