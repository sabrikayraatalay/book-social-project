package com.KayraAtalay.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.KayraAtalay.dto.BookUpdateRequest;
import com.KayraAtalay.dto.DtoBook;
import com.KayraAtalay.dto.DtoBookIU;
import com.KayraAtalay.dto.googlebooks.AddBookFromGoogleRequest;

public interface IBookService {
	
	 // ADMIN
    public DtoBook saveBook(DtoBookIU bookRequest);
    public DtoBook updateBook(Long bookId, BookUpdateRequest bookUpdateRequest);
    public void deleteBook(Long bookId);
    DtoBook saveBookFromGoogleApi(AddBookFromGoogleRequest request);

    // ADMIN and USER
    public Page<DtoBook> findAllPageable(Pageable pageable);
    public DtoBook findBookById(Long bookId);
    public List<DtoBook> searchBooksByTitle(String title);
    public Set<DtoBook> findBooksByAuthorId(Long authorId);

}
