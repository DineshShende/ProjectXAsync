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
import org.springframework.web.context.request.async.DeferredResult;

import com.google.gson.Gson;
import com.projectx.async.domain.EmailDTO;
import com.projectx.async.domain.EmailMessageDTO;
import com.projectx.async.domain.EmailEventDeferredObject;
import com.projectx.async.domain.EmailRequestStore;
import com.projectx.async.domain.SMSEventDeferredObject;
import com.projectx.async.domain.SMSMessageDTO;
import com.projectx.async.domain.SMSRequestStore;


@RestController
@RequestMapping(value="/sendVerificationDetails")
public class SendVerificationDetailsController {
	
	@Autowired
	EmailRequestStore requestStore;
	
	@Autowired
	SMSRequestStore smsRequestStore;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	Gson gson;
	
	private static Log logger = LogFactory.getLog(SendVerificationDetailsController.class);
	
	@RequestMapping(value="/sendEmailAsync",method=RequestMethod.POST)
	public DeferredResult<ResponseEntity<Integer>> sendEmail(@RequestBody EmailMessageDTO emailDTO)
	{
		logger.info("Client received: " + emailDTO);
	
		EmailEventDeferredObject<ResponseEntity<Integer>> eventDeferredObject = null;
		try
		{
			if(!requestStore.contains(emailDTO.getEmail()))
			{
				logger.info("New Request:"+emailDTO);
				eventDeferredObject=new EmailEventDeferredObject<ResponseEntity<Integer>>(emailDTO.getEmail(),emailDTO.getUuid(),emailDTO.getMessage());
				
				requestStore.add(eventDeferredObject);
				Thread t1=new Thread(eventDeferredObject);
				
				t1.start();
				
					
			}
			else
			{
			
				eventDeferredObject=requestStore.get(emailDTO.getEmail());
				
				logger.info("Existing Request:"+eventDeferredObject.getUuid());
				
				
				DeferredResult<ResponseEntity<Integer>> newDeferredResult=new DeferredResult<ResponseEntity<Integer>>();
				
				newDeferredResult.setResult(new ResponseEntity<Integer>(0,HttpStatus.OK));
								
				return newDeferredResult;
				
			}
		
			eventDeferredObject.onCompletion(new Runnable() {
				
				@Override
				public void run() {
					
					if(requestStore.contains(emailDTO.getEmail()))
					{	
						requestStore.delete(emailDTO.getEmail());

					}
					logger.info("completed:"+emailDTO);
				}
				
			});
			
			eventDeferredObject.onTimeout(new Runnable() {
				
				@Override
				public void run() {

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
		

	@RequestMapping(value="/sendSMSAsync",method=RequestMethod.POST)
	public DeferredResult<ResponseEntity<Integer>> sendSMS(@RequestBody SMSMessageDTO smsMessageDTO)
	{
		logger.info("Client received: " + smsMessageDTO);
	
		SMSEventDeferredObject<ResponseEntity<Integer>> eventDeferredObject = null;
		if(!smsRequestStore.contains(smsMessageDTO.getMobile()))
		{
			logger.info("New Request:"+smsMessageDTO);
			eventDeferredObject=new SMSEventDeferredObject<ResponseEntity<Integer>>(smsMessageDTO.getMobile(),smsMessageDTO.getUuid(),
			smsMessageDTO.getMessage());
				
			smsRequestStore.add(eventDeferredObject);
			Thread t1=new Thread(eventDeferredObject);
				
			t1.start();
				
		}
		else
		{
			eventDeferredObject=smsRequestStore.get(smsMessageDTO.getMobile());
				
			logger.info("Existing Request:"+eventDeferredObject.getUuid());
	
			DeferredResult<ResponseEntity<Integer>> newDeferredResult=new DeferredResult<ResponseEntity<Integer>>();
				
			newDeferredResult.setResult(new ResponseEntity<Integer>(0,HttpStatus.OK));
								
			return newDeferredResult;
				
		}
		
		eventDeferredObject.onCompletion(new Runnable() {
				
				@Override
				public void run() {
					
					if(smsRequestStore.contains(smsMessageDTO.getMobile()))
					{	
						smsRequestStore.delete(smsMessageDTO.getMobile());

					}
					logger.info("completed:"+smsMessageDTO);
				}
				
		});
			
		eventDeferredObject.onTimeout(new Runnable() {
				
				@Override
				public void run() {

					logger.info("TimeOut for:"+smsMessageDTO);
					
				}
		});

			
			
		return eventDeferredObject;
	}
		

	
}

	 