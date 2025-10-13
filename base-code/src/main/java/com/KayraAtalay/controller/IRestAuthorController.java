package com.KayraAtalay.controller;

import java.util.List;

import com.KayraAtalay.dto.DtoAuthor;
import com.KayraAtalay.dto.DtoAuthorIU;
import com.KayraAtalay.utils.PageableRequest;
import com.KayraAtalay.utils.RestPageableEntity;

public interface IRestAuthorController {

	//ADMIN 
	public RootEntity<DtoAuthor> saveAuthor(DtoAuthorIU authorRequest);
	
	public RootEntity<DtoAuthor> updateAuthor(Long authorId, DtoAuthorIU authorRequest);
	
	public void deleteAuthor(Long authorId);
	
	//ADMIN AND USER 
	public RootEntity<RestPageableEntity<DtoAuthor>> findAllPageable(PageableRequest pageableRequest);
	
	public RootEntity<DtoAuthor> findAuthorById(Long authorId);
	
	public RootEntity<List<DtoAuthor>> searchAuthorsByName(String name);
}
