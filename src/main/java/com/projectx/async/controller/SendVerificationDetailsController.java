package com.projectx.async.controller;


import java.security.SecureRandom;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.projectx.async.domain.EmailDTO;
import com.projectx.async.domain.EmailMessageDTO;
import com.projectx.async.domain.EventDeferredObject;
import com.projectx.async.domain.RequestStore;


@RestController
@RequestMapping(value="/sendVerificationDetails")
public class SendVerificationDetailsController {
	
	@Autowired
	RequestStore requestStore;
	
	private static Log logger = LogFactory.getLog(SendVerificationDetailsController.class);
	
	private ConcurrentMap<String , EventDeferredObject<Integer>> requestMap=new  ConcurrentHashMap<String , EventDeferredObject<Integer>>();

	@RequestMapping(value="/sendEmailAsync",method=RequestMethod.POST)
	public EventDeferredObject<Integer> sendEmail(@RequestBody EmailMessageDTO emailDTO)
	{
		//EmailDTO emailDTO=new EmailDTO(email, message);
		
		logger.info("Client received: " + emailDTO);
		
		EventDeferredObject<Integer> eventDeferredObject = null;
		
		if(!requestMap.containsKey(emailDTO.getEmail()))
		//if(!requestStore.contains(emailDTO.getEmail()))
		{
			logger.info("New Request");
			eventDeferredObject=new EventDeferredObject<Integer>(emailDTO.getEmail(),emailDTO.getMessage());
			
			requestMap.put(emailDTO.getEmail(), eventDeferredObject);
			//requestStore.add(eventDeferredObject);
			Thread t1=new Thread(eventDeferredObject);
			
			t1.start();
			
				
		}
		else
		{
			logger.info("Existing Request");
			//eventDeferredObject=requestStore.get(emailDTO.getEmail());
			eventDeferredObject=requestMap.get(emailDTO.getEmail());
			
		}
	
		eventDeferredObject.onCompletion(new Runnable() {
			
			@Override
			public void run() {
				
				if(requestMap.containsKey(emailDTO.getEmail()))
				//if(requestStore.contains(emailDTO.getEmail()))
				{	
					//requestStore.delete(emailDTO.getEmail());
					requestMap.remove(emailDTO.getEmail());
				}
				logger.info("completed");
			}
		});
		
		return eventDeferredObject;
	}
		
	@RequestMapping(value="/accept",method=RequestMethod.POST)
	public Object acceptRetriggerRequest(@RequestBody Object object)
	{
		Gson gson=new Gson();
		
		String jsonString=gson.toJson(object);
		
		System.out.println("The json"+jsonString);
		
		Object inp=gson.fromJson(jsonString, Object.class);
		
		System.out.println("Object constructed:"+inp);
		
		RestTemplate restTemplate=new RestTemplate();
		
		Object result=restTemplate.postForObject("http://localhost:9080/asycn/receiveObject", inp, Object.class);
		
		System.out.println("Object Result:"+result);
		
		return null;
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
	 