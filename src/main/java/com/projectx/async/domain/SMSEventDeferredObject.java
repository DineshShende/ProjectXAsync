package com.projectx.async.domain;

import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import com.projectx.async.controller.SendVerificationDetailsController;

public class SMSEventDeferredObject<T> extends DeferredResult<ResponseEntity<Integer>> implements Runnable {

	private Long mobile;

	private UUID uuid;
	
	private String message;
	
	
	
	@Override
	public void run() {
		
		RestTemplate restTemplate=new RestTemplate();
		
		SMSMessageDTO smsMessageDTO=new SMSMessageDTO(mobile,uuid, message);
		
		Log logger = LogFactory.getLog(SendVerificationDetailsController.class);
		
				
		Boolean result=restTemplate.postForObject("http://localhost:9080/asycn/sendSMS", smsMessageDTO, Boolean.class);
		
		if(result==true)
		{
			logger.debug("Send Status:True"+smsMessageDTO);
			this.setResult(new ResponseEntity<Integer>(2,HttpStatus.OK));
			
		}
		else
		{
			logger.debug("Send Status:False"+smsMessageDTO);
			this.setResult(new ResponseEntity<Integer>(1,HttpStatus.OK));
		}
	}
	
	
	public SMSEventDeferredObject() {
		
	}

	public SMSEventDeferredObject(Long mobile,UUID uuid,String message) {
	
		super(10000L, (new ResponseEntity<Integer>(1,HttpStatus.OK)));
	
		this.mobile = mobile;
		this.message=message;
		this.uuid=uuid;
		
	}


	public Long getMobile() {
		return mobile;
	}


	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}


	public UUID getUuid() {
		return uuid;
	}


	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	@Override
	public String toString() {
		return "SMSEventDeferredObject [mobile=" + mobile + ", uuid=" + uuid
				+ ", message=" + message + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		SMSEventDeferredObject other = (SMSEventDeferredObject) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	

}
