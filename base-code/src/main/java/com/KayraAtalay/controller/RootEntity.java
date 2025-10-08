package com.KayraAtalay.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RootEntity<T> {
	
	private Integer status;
	
	private T payload;
	
	
	public static <T> RootEntity<T> ok(T payload){
		RootEntity<T> rootEntity = new RootEntity<>();
		
		rootEntity.setStatus(200);
		rootEntity.setPayload(payload);
		
		return rootEntity;
	}

}