package com.projectx.async.controller;


import java.security.SecureRandom;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.projectx.async.domain.EmailDTO;
import com.projectx.async.domain.EventDeferredObject;


@RestController
@RequestMapping(value="/sendVerificationDetails")
public class SendVerificationDetailsController {
	
	private static Log logger = LogFactory.getLog(SendVerificationDetailsController.class);
	
	private ConcurrentMap<String , EventDeferredObject<Integer>> requestMap=new  ConcurrentHashMap<String , EventDeferredObject<Integer>>(); 
	
	SecureRandom random=new SecureRandom();
	
	Lock lock1=new ReentrantLock();

	@RequestMapping(value="/sendEmail/{email}")
	public EventDeferredObject<Integer> sendEmail(@PathVariable String email)
	{
		logger.info("Client received: " + email);
		
		EventDeferredObject<Integer> eventDeferredObject = null;
		
		EmailDTO emailDTO=new EmailDTO(random.nextLong(), email, "Hi There");
		
		System.out.println("Size:"+requestMap.size());
		
		//lock1.lock();
		if(!requestMap.containsKey(emailDTO.getEmail()))
		{
			System.out.println("New request:"+emailDTO);
			eventDeferredObject=new EventDeferredObject<Integer>(emailDTO.getCustomerId(), emailDTO.getEmail(),new Date());
			requestMap.put(emailDTO.getEmail(), eventDeferredObject);
			
			Thread t1=new Thread(eventDeferredObject);
			
			t1.start();
			
				
		}
		else
		{
			System.out.println("Existing request:"+emailDTO);
			eventDeferredObject=requestMap.get(emailDTO.getEmail());
			//System.out.println("Dummy result set"+eventDeferredObject);
			//eventDeferredObject.setResult(false);
			//System.out.println(eventDeferredObject);
		}
		//lock1.unlock();
		
		
		
		eventDeferredObject.onCompletion(new Runnable() {
			
			@Override
			public void run() {
				
				//lock1.lock();
				if(requestMap.containsKey(emailDTO.getEmail()))
				{	
					System.out.println("removed:"+requestMap.get(emailDTO.getEmail()));
					requestMap.remove(emailDTO.getEmail());
					
				}
				//lock1.unlock();
				
			}
		});
		
		return eventDeferredObject;
	}
		
}
/*
@Autowired
private RequestStore requestStore;

@ModelAttribute("requestStore")
	private RequestStore getRequestStore() {
		return requestStore;
	}
*/	
	 