package com.KayraAtalay.controller;

import java.util.List;
import java.util.Set;

import com.KayraAtalay.dto.BookUpdateRequest;
import com.KayraAtalay.dto.DtoBook;
import com.KayraAtalay.dto.DtoBookIU;
import com.KayraAtalay.utils.PageableRequest;
import com.KayraAtalay.utils.RestPageableEntity;

public interface IRestBookController {
		
		//ADMIN
	 	public RootEntity<DtoBook> saveBook(DtoBookIU bookRequest);
	    public RootEntity<DtoBook> updateBook(Long bookId, BookUpdateRequest bookUpdateRequest);
	    public void deleteBook(Long bookId);

	    // ADMIN and USER
	    public RootEntity<RestPageableEntity<DtoBook>> findAllPageable(PageableRequest pageable);
	    public RootEntity<DtoBook> findBookById(Long bookId);
	    public RootEntity<List<DtoBook>> searchBooksByTitle(String title);
	    public RootEntity<Set<DtoBook>> findBooksByAuthorId(Long authorId);

}
