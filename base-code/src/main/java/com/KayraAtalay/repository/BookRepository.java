package com.KayraAtalay.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.KayraAtalay.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
	
	List<Book> findByTitleContainingIgnoreCase(String title);
	
	Set<Book> findByAuthorId(Long authorId);
	
	boolean existsByGoogleBooksId(String googleBooksId);

}
