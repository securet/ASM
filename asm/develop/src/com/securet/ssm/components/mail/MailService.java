package com.securet.ssm.components.mail;

import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.securet.ssm.utils.FTLUtil;

public class MailService {
	
	private static final Logger _logger = LoggerFactory.getLogger(MailService.class);
		
    //private MailSender mailSender;
	@Autowired
    private JavaMailSenderImpl mailSender;
	
	@Autowired
	private Properties mailProperties;

	private boolean debug = false;
	private boolean auth = false;
	
	
	
    public void setMailSender(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    public void setMailProperties(Properties mailProperties){
    	this.mailProperties = mailProperties;
    }
    
	public boolean isDebug() {
		return debug;
	}


	public void setDebug(boolean debug) {
		this.debug = debug;
	}


	public boolean isAuth() {
		return auth;
	}


	public void setAuth(boolean auth) {
		this.auth = auth;
	}


	public void sendMail(Map<String,?> mailContext) {
		
		mailSender.setJavaMailProperties(mailProperties);
		
		MimeMessage msg = mailSender.createMimeMessage(); 
       // SimpleMailMessage msg = new SimpleMailMessage();
        String subject = (String)mailContext.get("subject");
        @SuppressWarnings("unchecked")
		Map<String,?> bodyParameters = (Map<String,?>)mailContext.get("bodyParameters");
        subject = FTLUtil.processString(subject, bodyParameters);
        try{
	        msg.setSubject(subject);
	        String to = (String)mailContext.get("to");
			msg.addRecipients(Message.RecipientType.TO, to);
	        if(mailContext.get("bcc")!=null){
	    		msg.addRecipients(Message.RecipientType.BCC, (String)mailContext.get("bcc"));
	        }
	        if(mailContext.get("replyTo")!=null){
	    		InternetAddress internetAddress = new InternetAddress((String)mailContext.get("replyTo"));
	    		Address[] addresses = {internetAddress};
	        	msg.setReplyTo(addresses);
	        }
	        if(mailContext.get("from")!=null){
	    		InternetAddress internetAddress = new InternetAddress((String)mailContext.get("from"));
	        	msg.setFrom(internetAddress);
	        }        
	        String mailTemplate = (String)mailContext.get("template"); 
	        String messageText = FTLUtil.processTemplate(mailTemplate, bodyParameters);        
	        //msg.setText(messageText);
	        //msg.setT
	        msg.addHeader("Content-Type", (String)mailContext.get("contentType"));
	        msg.setContent(messageText, (String)mailContext.get("contentType"));
	        mailSender.send(msg);
        }catch(MessagingException e){
        	_logger.error("Could not send email",e);
        }
	}
	
}
