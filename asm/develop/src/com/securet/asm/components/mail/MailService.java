package com.securet.asm.components.mail;

import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.securet.asm.utils.FTLUtil;

public class MailService {
	
		
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
		
		mailSender.setUsername("sharad@securet.in");
		mailSender.setPassword("password@123");

/*		mailSender.setUsername("sharadbhushank@gmail.com");
		mailSender.setPassword("#*#shabhu123");
*/
		mailSender.setJavaMailProperties(mailProperties);
		
        SimpleMailMessage msg = new SimpleMailMessage();
        String subject = (String)mailContext.get("subject");
        @SuppressWarnings("unchecked")
		Map<String,?> bodyParameters = (Map<String,?>)mailContext.get("bodyParameters");
        subject = FTLUtil.processString(subject, bodyParameters);
        msg.setSubject(subject);
        msg.setTo((String)mailContext.get("to"));
        if(mailContext.get("bcc")!=null){
        	msg.setBcc((String)mailContext.get("bcc"));
        }
        if(mailContext.get("replyTo")!=null){
        	msg.setReplyTo((String)mailContext.get("replyTo"));
        }
        if(mailContext.get("from")!=null){
        	msg.setFrom((String)mailContext.get("from"));
        }        
        String mailTemplate = (String)mailContext.get("template"); 
        String messageText = FTLUtil.processTemplate(mailTemplate, bodyParameters);        
        msg.setText(messageText);
        mailSender.send(msg);
        
	}
	
}
