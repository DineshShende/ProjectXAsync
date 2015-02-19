package com.projectx.async.fixtures.retrigger;



import java.util.Date;
import java.util.UUID;

import com.google.gson.Gson;
import com.projectx.async.domain.EmailMessageDTO;
import com.projectx.async.domain.RetriggerDetails;



public class RetriggerDetailsDataFixtures {

	private static Gson gson=new Gson();
	
	
	public static RetriggerDetails standardRetriggerDetails()
	{
		EmailMessageDTO emailMessageDTO=new EmailMessageDTO("dineshshe@gmail.com",UUID.randomUUID(), "Hi");
		
		return new RetriggerDetails("vendorRequest", gson.toJson(emailMessageDTO));
	}
	
	public static String standardJsonRetrigger(RetriggerDetails retriggerDetails)
	{
		return gson.toJson(standardRetriggerDetails());
	}
	
	
	
}
