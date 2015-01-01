package com.projectx.async.controller;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.projectx.async.domain.EmailDTO;
import com.projectx.async.domain.EventDeferredObject;






@RestController
@RequestMapping(value="/sendVerificationDetails")
public class SendVerificationDetailsController {
	
	private ConcurrentMap<String , EventDeferredObject<Boolean>> requestMap=new  ConcurrentHashMap<String , EventDeferredObject<Boolean>>(); 

	@RequestMapping(value="/sendEmail",method=RequestMethod.POST)
	public EventDeferredObject<Boolean> sendEmail(@RequestBody EmailDTO emailDTO)
	{
		EventDeferredObject<Boolean> eventDeferredObject = null;
		
		System.out.println("Size:"+requestMap.size());
		
		if(!requestMap.containsKey(emailDTO.getEmail()))
		{
			System.out.println("New request:"+emailDTO.getEmail());
			eventDeferredObject=new EventDeferredObject<Boolean>(emailDTO.getCustomerId(), emailDTO.getEmail());
			requestMap.put(emailDTO.getEmail(), eventDeferredObject);
			
			Thread t1=new Thread(eventDeferredObject);
			
			t1.start();
			
			
			
		}
		else
		{
			System.out.println("Existing request:"+emailDTO.getEmail());
			eventDeferredObject=requestMap.get(emailDTO.getEmail());
			
		}
		
		eventDeferredObject.onCompletion(new Runnable() {
			
			@Override
			public void run() {
				
				if(requestMap.containsKey(emailDTO.getEmail()))
				{	
					requestMap.remove(emailDTO.getEmail());
					System.out.println("removed:"+emailDTO.getEmail());
				}
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
	 