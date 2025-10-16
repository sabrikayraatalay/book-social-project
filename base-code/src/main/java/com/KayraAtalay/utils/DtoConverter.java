package com.KayraAtalay.utils;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.KayraAtalay.dto.DtoAuthor;
import com.KayraAtalay.dto.DtoBook;
import com.KayraAtalay.dto.DtoBookWithoutAuthor;
import com.KayraAtalay.dto.DtoUser;
import com.KayraAtalay.model.Author;
import com.KayraAtalay.model.Book;
import com.KayraAtalay.model.User;

public class DtoConverter {

	public static DtoUser toDto(User user) {
		DtoUser dtoUser = new DtoUser();

		BeanUtils.copyProperties(user, dtoUser);

		return dtoUser;
	}

	public static DtoAuthor toDto(Author author) {
		DtoAuthor dto = new DtoAuthor();
		dto.setId(author.getId());
		dto.setName(author.getName());
		dto.setBirthYear(author.getBirthYear());
		dto.setCountry(author.getCountry());
		dto.setCreateTime(author.getCreateTime());

		Set<Book> books = author.getBooks();
		if (books == null || books.isEmpty()) {
			return dto;
		}

		// Book to DtoBookWithoutAuthor
		Set<DtoBookWithoutAuthor> dtoBooks = author.getBooks().
			stream().map(book -> {
			DtoBookWithoutAuthor bookDto = new DtoBookWithoutAuthor();
			bookDto.setId(book.getId());
			bookDto.setTitle(book.getTitle());
			bookDto.setPublicationYear(book.getPublicationYear());
			bookDto.setRating(book.getRating());
			return bookDto;
		}).collect(Collectors.toSet());

		dto.setBooks(dtoBooks);

		return dto;
	}
	
	
	public static DtoBook toDto(Book book) {
		DtoBook dtoBook = new DtoBook();
		Author author = book.getAuthor();
		dtoBook.setId(book.getId());
		dtoBook.setCreateTime(book.getCreateTime());
		dtoBook.setAuthorName(author.getName());
		dtoBook.setPublicationYear(book.getPublicationYear());
		dtoBook.setRating(book.getRating());
		dtoBook.setReviewCount(book.getReviewCount());
		dtoBook.setTitle(book.getTitle());
		dtoBook.setGoogleBooksId(book.getGoogleBooksId());
		
		return dtoBook;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
