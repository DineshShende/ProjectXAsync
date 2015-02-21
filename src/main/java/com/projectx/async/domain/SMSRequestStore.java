package com.projectx.async.domain;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Scope(value="singleton", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class SMSRequestStore {

	private static final long serialVersionUID = -1779666204730031281L;
	
	private ConcurrentMap<Long , SMSEventDeferredObject<ResponseEntity<Integer>>> requestMap;
	
	
	
	public SMSRequestStore() {
		System.out.println("In constructor of smsRequestStore");
		this.requestMap = new  ConcurrentHashMap<Long , SMSEventDeferredObject<ResponseEntity<Integer>>>();
	}

	public SMSRequestStore(ConcurrentMap<Long , SMSEventDeferredObject<ResponseEntity<Integer>>> requestMap) {
		System.out.println("In constructor of smsRequestStore");
		this.requestMap = requestMap;
	}

	
	public SMSEventDeferredObject<ResponseEntity<Integer>> add(SMSEventDeferredObject<ResponseEntity<Integer>> request) {
		requestMap.put(request.getMobile(),request);
		return request;
	}
	
	public SMSEventDeferredObject<ResponseEntity<Integer>> get(Long mobile) {
		SMSEventDeferredObject<ResponseEntity<Integer>> fetched=requestMap.get(mobile);
		
		return fetched;
	}

	
	public void delete(Long key) {
		requestMap.remove(key);
	}

	
	public Boolean contains(Long key)
	{
		return requestMap.containsKey(key);
	}
	
	
	
	public int getSize() {
		return requestMap.size();
	}
	
	public void clear() {
		requestMap=new  ConcurrentHashMap<Long , SMSEventDeferredObject<ResponseEntity<Integer>>>();
	}

	
}
