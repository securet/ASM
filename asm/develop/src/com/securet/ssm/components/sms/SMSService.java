package com.securet.ssm.components.sms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.securet.ssm.utils.FTLUtil;

public class SMSService {

	private static final Logger _logger = LoggerFactory.getLogger(SMSService.class);
	
	private String apiURL;
	private String userName;
	private String password;
	private String sender;
	
	public String getApiURL() {
		return apiURL;
	}

	public void setApiURL(String apiURL) {
		this.apiURL = apiURL;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * 
	 * @param input
	 * @return true if sent
	 * @throws UnsupportedEncodingException
	 */
	public boolean sendSMS(Map<String,Object> input) throws UnsupportedEncodingException{
		String contactNumber = (String)input.get("contactNumber");
		Map<String,?> bodyParameters = (Map<String,?>)input.get("bodyParameters");
        String mailTemplate = (String)input.get("template"); 
        String messageText = FTLUtil.processTemplate(mailTemplate, bodyParameters);
        StringBuilder builder = new StringBuilder();
        builder.append(apiURL).append("?user=").append(userName).append(":")
        .append(password).append("&senderID=").append(URLEncoder.encode(sender,"UTF-8")).append("&receipientno=")
        .append(contactNumber).append("&msgtxt=").append(URLEncoder.encode(messageText, "UTF-8"))
        .append("&state=4");
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        HttpGet getMethod = new HttpGet(builder.toString());
        try {
        	httpResponse = httpclient.execute(getMethod);
        	String response = EntityUtils.toString(httpResponse.getEntity());
        	_logger.info("SMS Satus: " + response);
        	if(!response.isEmpty() && response.indexOf("Status=0")!=-1){
        		return true;
        	}
		} catch (ClientProtocolException e) {
			_logger.error("Could not Send sms", e);
		} catch (IOException e) {
			_logger.error("Could not Send sms", e);
		}
        return false;
	}

}
