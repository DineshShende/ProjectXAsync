package com.projectx.async.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.projectx.async.domain.RetriggerDetails;




@Repository
public interface RetriggerDetailsRepository {
	
	public RetriggerDetails save(RetriggerDetails retriggerDetails );
	
	public List<RetriggerDetails> findAll();
	
	public Boolean deleteById(Long retriggerId);

}
