package com.projectx.data.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.projectx.async.domain.RetriggerDetails;


public class RetriggerList {

	List<RetriggerDetails> list;

	@JsonCreator
	public RetriggerList(List<RetriggerDetails> list) {
		super();
		this.list = list;
	}

	public RetriggerList() {
		super();
	}

	public List<RetriggerDetails> getList() {
		return list;
	}

	public void setList(List<RetriggerDetails> list) {
		this.list = list;
	}

	
	
}
