package com.projectx.async.domain;

import java.util.Date;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

public class EventDeferredObject<T> extends DeferredResult<Integer> implements Runnable {

	private String email;
	
	private String message;
	
	
	
	
	
	@Override
	public void run() {
		
		RestTemplate restTemplate=new RestTemplate();
		
		EmailMessageDTO emailMessageDTO=new EmailMessageDTO(email, message);
		
//		
		
		Boolean result=restTemplate.postForObject("http://localhost:9080/asycn/sendEmail", emailMessageDTO, Boolean.class);
		
		if(result==true)
			this.setResult(2);
		else
			this.setResult(1);
	}
	
	
	public EventDeferredObject() {
		
	}

	public EventDeferredObject(String email,String message) {
		//super(10000L,new Integer(0));
		super(50000L, new Integer(0));
	
		this.email = email;
		this.message=message;
		
		
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


	@Override
	public String toString() {
		return "EventDeferredObject [email=" + email + ", message=" + message
				+ "]";
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
		EventDeferredObject other = (EventDeferredObject) obj;
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
