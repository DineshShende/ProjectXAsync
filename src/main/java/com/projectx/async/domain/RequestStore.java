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
	
	private ConcurrentMap<String , EventDeferredObject<Boolean>> requestMap;
	
	
	
	public RequestStore() {
		System.out.println("In constructor of requestStore");
		this.requestMap = new  ConcurrentHashMap<String , EventDeferredObject<Boolean>>();
	}

	public RequestStore(ConcurrentMap<String , EventDeferredObject<Boolean>> requestMap) {
		System.out.println("In constructor of requestStore");
		this.requestMap = requestMap;
	}

	
	public EventDeferredObject<Boolean> add(EventDeferredObject<Boolean> request) {
		requestMap.put(request.getEmail(), request);
		return request;
	}
	
	public EventDeferredObject<Boolean> get(String email) {
		EventDeferredObject<Boolean> fetched=requestMap.get(email);
		
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
		requestMap=new  ConcurrentHashMap<String , EventDeferredObject<Boolean>>();
	}
	
	
}