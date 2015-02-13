package com.projectx.async.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.projectx.async.domain.RetriggerDetails;
import com.projectx.async.repository.RetriggerDetailsRepository;
import com.projectx.async.rest.RetriggerDTO;

@RestController
@RequestMapping(value="/retrigger")
public class RetriggerController {

	@Autowired
	RetriggerDetailsRepository retriggerDetailsRepository;
	
	@Autowired
	Gson gson;
	
	@RequestMapping(method=RequestMethod.POST)
	public Boolean createRetrigger(@RequestBody RetriggerDTO retriggerDTO)
	{
		Object payload=retriggerDTO.getData();
		
		String jsonData=gson.toJson(payload);
		
		RetriggerDetails retriggerDetails=new RetriggerDetails(retriggerDTO.getService(), jsonData);
		
		RetriggerDetails details=retriggerDetailsRepository.save(retriggerDetails);
		
		if(details.getRetriggerId()!=null)
			return true;
		else
			return false;
		
	}
	
}
