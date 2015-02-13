package com.projectx.async.domain;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;



@Component
@Scope(value="singleton", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class RequestStore  implements Serializable {

	private static final long serialVersionUID = -1779666204730031281L;
	
	private ConcurrentMap<String , EventDeferredObject<Integer>> requestMap;
	
	
	
	public RequestStore() {
		System.out.println("In constructor of requestStore");
		this.requestMap = new  ConcurrentHashMap<String , EventDeferredObject<Integer>>();
	}

	public RequestStore(ConcurrentMap<String , EventDeferredObject<Integer>> requestMap) {
		System.out.println("In constructor of requestStore");
		this.requestMap = requestMap;
	}

	
	public EventDeferredObject<Integer> add(EventDeferredObject<Integer> request) {
		requestMap.put(request.getEmail(), request);
		return request;
	}
	
	public EventDeferredObject<Integer> get(String email) {
		EventDeferredObject<Integer> fetched=requestMap.get(email);
		
		return fetched;
	}

	
	public void delete(String key) {
		requestMap.remove(key);
	}

	
	public Boolean contains(String key)
	{
		return requestMap.containsKey(key);
	}
	
	
	
	public int getSize() {
		return requestMap.size();
	}
	
	public void clear() {
		requestMap=new  ConcurrentHashMap<String , EventDeferredObject<Integer>>();
	}
	
	
}