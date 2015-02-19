package com.projectx.async.controller;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	Gson gson;
	
	private static Log logger = LogFactory.getLog(SendVerificationDetailsController.class);
	
//	private ConcurrentMap<String , EventDeferredObject<ResponseEntity<Integer>>> requestMap=new  ConcurrentHashMap<String , EventDeferredObject<ResponseEntity<Integer>>>();

	@RequestMapping(value="/sendEmailAsync",method=RequestMethod.POST)
	public EventDeferredObject<ResponseEntity<Integer>> sendEmail(@RequestBody EmailMessageDTO emailDTO)
	{
		logger.info("Client received: " + emailDTO);
		//EmailDTO emailDTO=new EmailDTO(email, message);
		
		EventDeferredObject<ResponseEntity<Integer>> eventDeferredObject = null;
		try
		{
			
			
			
			//if(!requestMap.containsKey(emailDTO.getEmail()))
			if(!requestStore.contains(emailDTO.getEmail()))
			{
				logger.info("New Request:"+emailDTO);
				eventDeferredObject=new EventDeferredObject<ResponseEntity<Integer>>(emailDTO.getEmail(),emailDTO.getUuid(),emailDTO.getMessage());
				
				//requestMap.put(emailDTO.getEmail(), eventDeferredObject);
				requestStore.add(eventDeferredObject);
				Thread t1=new Thread(eventDeferredObject);
				
				t1.start();
				
					
			}
			else
			{
				
				eventDeferredObject=requestStore.get(emailDTO.getEmail());
				logger.info("Existing Request:"+eventDeferredObject.getUuid());
				//eventDeferredObject=requestMap.get(emailDTO.getEmail());
				
			}
		
			eventDeferredObject.onCompletion(new Runnable() {
				
				@Override
				public void run() {
					
					//if(requestMap.containsKey(emailDTO.getEmail()))
					if(requestStore.contains(emailDTO.getEmail()))
					{	
						requestStore.delete(emailDTO.getEmail());
						//requestMap.remove(emailDTO.getEmail());
					}
					logger.info("completed:"+emailDTO);
				}
				
			});
			
			eventDeferredObject.onTimeout(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					logger.info("TimeOut for:"+emailDTO);
				}
			});

			
		}catch(Exception e)
		{
			logger.error("Error in sending email in controller",e);
			eventDeferredObject.setResult(new ResponseEntity<Integer>(1,HttpStatus.OK));
		}
		
		return eventDeferredObject;
	}
		
	
	
	@RequestMapping(value="/get",method=RequestMethod.GET)
	public String hi()
	{
		return "Hi";
	}
	
}
/*
 
 
	@RequestMapping(value="/accept",method=RequestMethod.POST)
	public Object acceptRetriggerRequest(@RequestBody Object object)
	{
			
		String jsonString=gson.toJson(object);
		
		Object inp=gson.fromJson(jsonString, Object.class);
		
		Object result=restTemplate.postForObject("http://localhost:9080/asycn/receiveObject", inp, Object.class);
		
		System.out.println("Object Result:"+result);
		
		return null;
	}

 
@Autowired
private RequestStore requestStore;

@ModelAttribute("requestStore")
	private RequestStore getRequestStore() {
		return requestStore;
	}
*/	
	 