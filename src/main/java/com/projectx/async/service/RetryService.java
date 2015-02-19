package com.projectx.async.service;

import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.projectx.async.domain.BooleanStatus;
import com.projectx.async.domain.CountObject;
import com.projectx.async.domain.EmailMessageDTO;
import com.projectx.async.domain.RetriggerDetails;
import com.projectx.async.repository.RetriggerDetailsRepository;
import com.projectx.async.rest.RetriggerDTO;


@Component
@PropertySource(value="classpath:/application.properties")
public class RetryService {

	@Autowired
	RetriggerDetailsRepository retriggerDetailsRepository;
	
	@Autowired
	RestTemplate restTemplate; 
	
	@Autowired
	Environment env;
	
	@Autowired
	Gson gson;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Scheduled(fixedRate=30000)
	public void retryLogin()
	{
		
		List<RetriggerDetails> list=retriggerDetailsRepository.findAll();
		
		
		
		AsyncRestTemplate asyncRestTemplate=new AsyncRestTemplate();
		
		
		for(int i=0;i<list.size();i++)
		{
		
			CountObject countObject=new CountObject(i);
			
			HttpEntity<Object> entity=new HttpEntity<Object>(gson.fromJson(list.get(i).getData(), EmailMessageDTO.class));
			
			try{
				
			log.debug("Started retry for"+list.get(i));
				
			ListenableFuture<ResponseEntity<Integer>>		
			 status=asyncRestTemplate.exchange(env.getProperty("async.url")+"/sendVerificationDetails/sendEmailAsync", HttpMethod.POST,
					 entity, Integer.class);
			 
				 
			status.addCallback(new ListenableFutureCallback<ResponseEntity<Integer>>() {

				@Override
				public void onSuccess(ResponseEntity<Integer> result) {
					
					if(result.getBody().equals(new Integer(2)))
					{
						log.debug("Sucess retry for"+list.get(countObject.getCount()));
						retriggerDetailsRepository.deleteById(list.get(countObject.getCount()).getRetriggerId());
					}
					
				}

				@Override
				public void onFailure(Throwable t) {

					log.debug("Failure retry for"+list.get(countObject.getCount()));
					
					try {
						throw t;
					} catch (Throwable e) {
						
						//System.out.println(e.getStackTrace()+""+e.getLocalizedMessage()+""+e.toString()+""+e.);
						
						
					}
				}

				
			});
		
			log.debug("Ended retry for"+list.get(i));
		}catch(Exception e)
			{
			
			}
			
		
		}
	
		/*
		
		Predicate<RetriggerDetails> predicate=(e)->{
			
			BooleanStatus booleanStatus=new BooleanStatus(false); 
			
			asyncRestTemplate.exchange(env.getProperty("async.url")+e.getService(), HttpMethod.POST,
					 new HttpEntity<Object>(gson.fromJson(e.getData(), Object.class)), Integer.class)
					 
					 .addCallback(new ListenableFutureCallback<ResponseEntity<Integer>>() {

						@Override
						public void onSuccess(ResponseEntity<Integer> result) {
							
							if(result.getBody().equals(new Integer(2)))
							{
								System.out.println("Sucess");
								booleanStatus.setStatus(true);
								//retriggerDetailsRepository.deleteById(list.get(i).getRetriggerId());
								
							}
							booleanStatus.setStatus(false);
							
						}
		
						@Override
						public void onFailure(Throwable t) {
							booleanStatus.setStatus(false);
						}
		

			});
			return booleanStatus.getStatus().equals(true);

			
		};

		
		list.parallelStream().filter(predicate)
		.forEach(c-> retriggerDetailsRepository.deleteById(c.getRetriggerId()));
		
		*/

		

		/*
		
		 
				Predicate<RetriggerDetails> predicate=(e)-> 
		restTemplate.postForObject(env.getProperty("async.url")+e.getService(),gson.fromJson(e.getData(), Object.class) , Integer.class).equals(new Integer(2));
				
 
		 
		Integer result=restTemplate.postForObject(env.getProperty("async.url")+list.get(i).getService(),gson.fromJson(list.get(i).getData(), Object.class) , Integer.class);
		
		if(result.equals(new Integer(2)))
			retriggerDetailsRepository.deleteById(list.get(i).getRetriggerId());
		*/

		
		/*
		 
		 list.forEach(e->{
		
		Boolean result=restTemplate.postForObject(env.getProperty("asyn.url")+e.getService(), e.getData(), Boolean.class);
		 System.out.println(e.getData());
			
			Boolean result=restTemplate.postForObject(env.getProperty("asyn.url")+e.getService(), e.getData(), Boolean.class);
			
			if(result)
				retriggerDetailsRepository.deleteById(e.)
			
		
		});
		 
		 */
	}
	
	
}
