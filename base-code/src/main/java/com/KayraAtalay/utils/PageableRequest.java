package com.KayraAtalay.utils;

import lombok.Data;

@Data
public class PageableRequest {
	
	private int pageNumber;
	
	private int pageSize;
	
	private String columnName;
	
	private boolean asc;

}