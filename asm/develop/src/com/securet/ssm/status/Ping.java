package com.securet.ssm.status;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
		mailContext.put("from","st@securet.in");
		mailContext.put("subject","Test Mail from ${name!}!");
		mailContext.put("template","test.ftl");
		mailContext.put("contentType","text/html");
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
		entityManager.persist(organization);
		//organizationService.setOrganization(organization);
		//organizationService.createOrganization();
		return "ping";
	}
	
	@RequestMapping(value = "/uploadTest", method = RequestMethod.GET)
	public String uploadTest(Model model){
		return "uploadTest";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public @ResponseBody String provideUploadInfo() {
		return "You can upload a file by posting to this same URL.";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody String handleFileUpload(@RequestParam("name") String name, @RequestParam("file") MultipartFile file,Model model,HttpServletRequest request) {
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				String storageFilePath = "uploads/temp/";
				String filePath=null;
				URL url = Thread.currentThread().getContextClassLoader().getResource("../../"+storageFilePath);
				if(url!=null){
					filePath = url.getPath()+file.getOriginalFilename();
				}
				_logger.debug("destFilePath" + filePath);
				File destFile = new File(filePath);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(destFile));
				stream.write(bytes);
				stream.close();
				String finalFileName = "/"+storageFilePath+file.getOriginalFilename();
				model.addAttribute("filePath",finalFileName);
				return "You successfully uploaded " + name + " into " + name + "-uploaded ! Here is the link <a href='"+request.getContextPath()+finalFileName+"'> "+file.getOriginalFilename()+"</a>";
			} catch (Exception e) {
				e.printStackTrace();
				return "You failed to upload " + name + " => " + e.getMessage();
			}
		} else {
			return "You failed to upload " + name + " because the file was empty.";
		}
	}

}
