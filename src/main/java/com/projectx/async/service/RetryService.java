package com.projectx.async.service;

import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;





import com.google.gson.Gson;
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
	
	
	//@Scheduled(fixedRate=10000)
	public void retryLogin()
	{
		List<RetriggerDetails> list=retriggerDetailsRepository.findAll();
		
		/*
		for(int i=0;i<list.size();i++)
		{
			Integer result=restTemplate.postForObject(env.getProperty("async.url")+list.get(i).getService(),gson.fromJson(list.get(i).getData(), Object.class) , Integer.class);
			
			if(result.equals(new Integer(2)))
				retriggerDetailsRepository.deleteById(list.get(i).getRetriggerId());
		}
		*/
		
	
		Predicate<RetriggerDetails> predicate=(e)-> 
			restTemplate.postForObject(env.getProperty("async.url")+e.getService(),gson.fromJson(e.getData(), Object.class) , Integer.class).equals(new Integer(2));
		
		list.parallelStream().filter(predicate)
		.forEach(c-> retriggerDetailsRepository.deleteById(c.getRetriggerId()));
		
			
		
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
