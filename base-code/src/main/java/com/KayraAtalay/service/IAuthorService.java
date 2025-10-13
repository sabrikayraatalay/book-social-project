package com.KayraAtalay.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.KayraAtalay.dto.DtoAuthor;
import com.KayraAtalay.dto.DtoAuthorIU;

public interface IAuthorService {
	
	 // ADMIN
    public DtoAuthor saveAuthor(DtoAuthorIU authorRequest);
    
    public DtoAuthor updateAuthor(Long authorId, DtoAuthorIU authorRequest);
    
    public void deleteAuthor(Long authorId);

    // ADMIN and USER
    public  Page<DtoAuthor> findAllPageable(Pageable pageable);
    
    public DtoAuthor findAuthorById(Long authorId);
    
    public List<DtoAuthor> searchAuthorsByName(String name);

}
