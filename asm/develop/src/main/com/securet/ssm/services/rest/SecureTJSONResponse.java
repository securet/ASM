package com.securet.ssm.services.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.securet.ssm.persistence.objects.SecureTObject.SimpleObject;

public class SecureTJSONResponse {

	private String status;
	private Object messages;
    @JsonView(SimpleObject.class)
	private Object data;
	
	public SecureTJSONResponse() {
	}
	
	public SecureTJSONResponse(String status, Object messages, Object data) {
		this.status=status;
		this.messages=messages;
		this.data=data;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Object getMessages() {
		return messages;
	}
	public void setMessages(Object messages) {
		this.messages = messages;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
