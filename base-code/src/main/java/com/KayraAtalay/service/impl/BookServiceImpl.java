package com.KayraAtalay.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.KayraAtalay.dto.BookUpdateRequest;
import com.KayraAtalay.dto.DtoBook;
import com.KayraAtalay.dto.DtoBookIU;
import com.KayraAtalay.exception.BaseException;
import com.KayraAtalay.exception.ErrorMessage;
import com.KayraAtalay.exception.MessageType;
import com.KayraAtalay.model.Author;
import com.KayraAtalay.model.Book;
import com.KayraAtalay.repository.AuthorRepository;
import com.KayraAtalay.repository.BookRepository;
import com.KayraAtalay.repository.ReviewRepository;
import com.KayraAtalay.service.IBookService;
import com.KayraAtalay.utils.DtoConverter;

@Service
public class BookServiceImpl implements IBookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	@Transactional
	public void updateBookRating(Long bookId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.BOOK_NOT_FOUND, bookId.toString())));

		BigDecimal averageRating = reviewRepository.findAverageRatingByBookId(bookId);

		Long reviewCount = reviewRepository.countByBookId(bookId);

		book.setRating(averageRating == null ? BigDecimal.ZERO : averageRating);
		book.setReviewCount(reviewCount.intValue());

		bookRepository.save(book);
	}

	@Override
	@Transactional
	public DtoBook saveBook(DtoBookIU bookRequest) {
		Author author = authorRepository.findById(bookRequest.getAuthorId()).orElseThrow(() -> new BaseException(
				new ErrorMessage(MessageType.AUTHOR_NOT_FOUND, bookRequest.getAuthorId().toString())));

		Book book = new Book();
		book.setAuthor(author);
		book.setPublicationYear(bookRequest.getPublicationYear());
		book.setRating(BigDecimal.ZERO);
		book.setReviewCount(0);
		book.setTitle(bookRequest.getTitle());

		Book savedBook = bookRepository.save(book);
		return DtoConverter.toDto(savedBook);
	}

	@Override
	@Transactional
	public DtoBook updateBook(Long bookId, BookUpdateRequest bookUpdateRequest) {
		Book bookToUpdate = bookRepository.findById(bookId)
				.orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.BOOK_NOT_FOUND, bookId.toString())));

		bookToUpdate.setTitle(bookUpdateRequest.getTitle());
		bookToUpdate.setPublicationYear(bookUpdateRequest.getPublicationYear());

		Book savedBook = bookRepository.save(bookToUpdate);
		return DtoConverter.toDto(savedBook);
	}

	@Override
	@Transactional
	public void deleteBook(Long bookId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.BOOK_NOT_FOUND, bookId.toString())));

		if (book.getReviewCount() != null && book.getReviewCount() > 0) {
			throw new BaseException(new ErrorMessage(MessageType.INVALID_OPERATION,
					"Book with ID " + bookId + " cannot be deleted because it has reviews."));
		}
		bookRepository.delete(book);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "books", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
	public Page<DtoBook> findAllPageable(Pageable pageable) {

		System.out.println(">>> VERİTABANINDAN ÇEKİLİYOR! (Cache'lenmemiş)");

		return bookRepository.findAll(pageable).map(DtoConverter::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public DtoBook findBookById(Long bookId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.BOOK_NOT_FOUND, bookId.toString())));
		return DtoConverter.toDto(book);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DtoBook> searchBooksByTitle(String title) {
		List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
		return books.stream().map(DtoConverter::toDto).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Set<DtoBook> findBooksByAuthorId(Long authorId) {
		if (!authorRepository.existsById(authorId)) {
			throw new BaseException(new ErrorMessage(MessageType.AUTHOR_NOT_FOUND, authorId.toString()));
		}
		Set<Book> authorsBooks = bookRepository.findByAuthorId(authorId);
		return authorsBooks.stream().map(DtoConverter::toDto).collect(Collectors.toSet());
	}
}