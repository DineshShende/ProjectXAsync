package com.projectx.async.repositoryImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.projectx.async.domain.RetriggerDetails;
import com.projectx.async.repository.RetriggerDetailsRepository;
import com.projectx.data.domain.RetriggerList;



@Component
@Profile(value={"Dev"})
@PropertySource(value="classpath:/application.properties")
public class RetriggerDetailsRepositoryImpl implements
		RetriggerDetailsRepository {

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	Environment env;
	
	@Override
	public RetriggerDetails save(RetriggerDetails retriggerDetails) {

		RetriggerDetails savedEntity=restTemplate.postForObject(env.getProperty("data.url")+"/retriggerDetails", retriggerDetails, RetriggerDetails.class);
		
		return savedEntity;
	
	}

	@Override
	public List<RetriggerDetails> findAll() {
		
		
		RetriggerList savedEntity=restTemplate.getForObject(env.getProperty("data.url")+"/retriggerDetails/findAll",  RetriggerList.class);
		
		return savedEntity.getList();
		
		
	}

	@Override
	public Boolean deleteById(Long retriggerId) {

		Boolean savedEntity=restTemplate.getForObject(env.getProperty("data.url")+"/retriggerDetails/deleteById/"+retriggerId,  Boolean.class);
		
		return savedEntity;
		

	}

}
