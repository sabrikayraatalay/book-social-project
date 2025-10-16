package com.KayraAtalay.service.impl;

import com.KayraAtalay.dto.BookUpdateRequest;
import com.KayraAtalay.dto.DtoBook;
import com.KayraAtalay.dto.DtoBookIU;
import com.KayraAtalay.dto.googlebooks.AddBookFromGoogleRequest;
import com.KayraAtalay.dto.googlebooks.GoogleBookItem;
import com.KayraAtalay.dto.googlebooks.VolumeInfo;
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
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements IBookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GoogleBooksServiceImpl googleBooksService;
    
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
    public DtoBook saveBookFromGoogleApi(AddBookFromGoogleRequest request) {
        // Step 1: Check if this book already exists in our database to prevent duplicates.
        if (bookRepository.existsByGoogleBooksId(request.getGoogleBookId())) {
            throw new BaseException(
                    new ErrorMessage(MessageType.INVALID_OPERATION, "This book has already been added to the system."));
        }

        // Step 2: Fetch the full book details from Google using its ID.
        GoogleBookItem googleBook = googleBooksService.findBookById(request.getGoogleBookId());
        if (googleBook == null || googleBook.getVolumeInfo() == null) {
            throw new BaseException(new ErrorMessage(MessageType.BOOK_NOT_FOUND,
                    "Book details could not be found on Google Books for ID: " + request.getGoogleBookId()));
        }
        VolumeInfo volumeInfo = googleBook.getVolumeInfo();

        // Step 3: Find the author in our database. If not found, throw an error.
        Author author = getAuthorByName(volumeInfo);

        // Step 4: Create and save the new Book entity.
        Book newBook = new Book();
        newBook.setGoogleBooksId(googleBook.getId());
        newBook.setTitle(volumeInfo.getTitle());
        newBook.setAuthor(author);

        // Safely parse the publication year from the Google API response.
        if (volumeInfo.getPublishedDate() != null && !volumeInfo.getPublishedDate().isEmpty()) {
            String yearString = volumeInfo.getPublishedDate().substring(0, 4);
            newBook.setPublicationYear(Integer.parseInt(yearString));
        }

        newBook.setReviewCount(0);
        newBook.setRating(BigDecimal.ZERO);

        Book savedBook = bookRepository.save(newBook);
        return DtoConverter.toDto(savedBook);
    }

    /**
     * Helper method to find an author by name. Throws an exception if the author is not registered in the system.
     * @param volumeInfo The book data from Google API.
     * @return The found Author entity.
     */
    private Author getAuthorByName(VolumeInfo volumeInfo) {
        if (volumeInfo.getAuthors() == null || volumeInfo.getAuthors().isEmpty()) {
            throw new BaseException(new ErrorMessage(MessageType.INVALID_OPERATION,
                    "Google API did not provide an author for this book. It cannot be added automatically."));
        }

        String authorName = volumeInfo.getAuthors().get(0); // Use the first author from the list.

        // Find author by name (case-insensitive) or throw an exception if not found.
        return authorRepository.findByNameIgnoreCase(authorName)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.AUTHOR_NOT_FOUND,
                        "Author '" + authorName + "' is not registered in the system. Please add the author first.")));
    }

    /**
     * Saves a new book manually from data provided by the admin.
     * @param bookRequest DTO containing the book's title, authorId, and publicationYear.
     * @return The DTO of the newly saved book.
     */
    @Override
    @Transactional
    public DtoBook saveBook(DtoBookIU bookRequest) {
        Author author = authorRepository.findById(bookRequest.getAuthorId())
                .orElseThrow(() -> new BaseException(
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

    /**
     * Updates an existing book's details.
     * @param bookId The ID of the book to update.
     * @param bookUpdateRequest DTO containing the new title and publication year.
     * @return The DTO of the updated book.
     */
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

    /**
     * Deletes a book from the system if it has no reviews.
     * @param bookId The ID of the book to delete.
     */
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

    /**
     * Retrieves a paginated list of all books.
     * @param pageable Pagination information.
     * @return A Page of book DTOs.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DtoBook> findAllPageable(Pageable pageable) {
        return bookRepository.findAll(pageable).map(DtoConverter::toDto);
    }

    /**
     * Finds a single book by its ID.
     * @param bookId The ID of the book to find.
     * @return The DTO of the found book.
     */
    @Override
    @Transactional(readOnly = true)
    public DtoBook findBookById(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.BOOK_NOT_FOUND, bookId.toString())));
        return DtoConverter.toDto(book);
    }

    /**
     * Searches for books whose titles contain the given search term.
     * @param title The search term for the book title.
     * @return A list of matching book DTOs.
     */
    @Override
    @Transactional(readOnly = true)
    public List<DtoBook> searchBooksByTitle(String title) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
        return books.stream().map(DtoConverter::toDto).collect(Collectors.toList());
    }

    /**
     * Finds all books written by a specific author.
     * @param authorId The ID of the author.
     * @return A set of matching book DTOs.
     */
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