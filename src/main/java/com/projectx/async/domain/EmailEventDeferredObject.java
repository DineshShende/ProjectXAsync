package com.projectx.async.domain;

import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import com.projectx.async.controller.SendVerificationDetailsController;


public class EmailEventDeferredObject<T> extends DeferredResult<ResponseEntity<Integer>> implements Runnable {

	private String email;

	private UUID uuid;
	
	private String message;
	
	
	
	@Override
	public void run() {
		
		RestTemplate restTemplate=new RestTemplate();
		
		EmailMessageDTO emailMessageDTO=new EmailMessageDTO(email,uuid, message);
		
		Log logger = LogFactory.getLog(SendVerificationDetailsController.class);
		
				
		Boolean result=restTemplate.postForObject("http://localhost:9080/asycn/sendEmail", emailMessageDTO, Boolean.class);
		
		if(result==true)
		{
			logger.debug("Send Status:True"+emailMessageDTO);
			this.setResult(new ResponseEntity<Integer>(2,HttpStatus.OK));
			
		}
		else
		{
			logger.debug("Send Status:False"+emailMessageDTO);
			this.setResult(new ResponseEntity<Integer>(1,HttpStatus.OK));
		}
	}
	
	
	public EmailEventDeferredObject() {
		
	}

	public EmailEventDeferredObject(String email,UUID uuid,String message) {
	
		super(10000L, (new ResponseEntity<Integer>(1,HttpStatus.OK)));
	
		this.email = email;
		this.message=message;
		this.uuid=uuid;
		
	}

	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}




	public UUID getUuid() {
		return uuid;
	}


	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}


	@Override
	public String toString() {
		return "EventDeferredObject [email=" + email + ", uuid=" + uuid
				+ ", message=" + message + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmailEventDeferredObject other = (EmailEventDeferredObject) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}

}
