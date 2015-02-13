package com.projectx.async.repository;


import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.projectx.async.config.Application;
import com.projectx.async.domain.RetriggerDetails;

import static com.projectx.async.fixtures.retrigger.RetriggerDetailsDataFixtures.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
@ActiveProfiles("Dev")
public class RetriggerDetailsRepositoryTest {

	@Autowired
	RetriggerDetailsRepository retriggerDetailsRepository;
	
	

	
	@Before
	public void setUp()
	{
		
	}
	
	@Test
	public void environmentTest() {
		
	}
	

	@Test
	public void saveAndGet()
	{
		
		RetriggerDetails savedEntity=retriggerDetailsRepository.save(standardRetriggerDetails());
		
		assertEquals(1, retriggerDetailsRepository.findAll().size());
		
		retriggerDetailsRepository.deleteById(savedEntity.getRetriggerId());
		
	}
	
	@Test
	public void findAll()
	{
		RetriggerDetails savedEntity=retriggerDetailsRepository.save(standardRetriggerDetails());
		
		List<RetriggerDetails> list=(List<RetriggerDetails>) retriggerDetailsRepository.findAll();
	
		assertEquals(1, list.size());
		
		retriggerDetailsRepository.deleteById(savedEntity.getRetriggerId());
		
	}
	
	@Test
	public void deleteById()
	{
		RetriggerDetails savedEntity=retriggerDetailsRepository.save(standardRetriggerDetails());
		
		assertEquals(savedEntity, retriggerDetailsRepository.findAll().get(0));
		
		retriggerDetailsRepository.deleteById(savedEntity.getRetriggerId());
		
		assertEquals(0,retriggerDetailsRepository.findAll().size());
	}


}
