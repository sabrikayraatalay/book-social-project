package com.KayraAtalay.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.KayraAtalay.dto.DtoReview;
import com.KayraAtalay.dto.DtoReviewIU;
import com.KayraAtalay.exception.BaseException;
import com.KayraAtalay.exception.ErrorMessage;
import com.KayraAtalay.exception.MessageType;
import com.KayraAtalay.model.Book;
import com.KayraAtalay.model.Review;
import com.KayraAtalay.model.User;
import com.KayraAtalay.repository.BookRepository;
import com.KayraAtalay.repository.ReviewRepository;
import com.KayraAtalay.repository.UserRepository;
import com.KayraAtalay.service.IReviewService;
import com.KayraAtalay.utils.DtoConverter;

@Service
public class ReviewServiceImpl implements IReviewService {

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BookServiceImpl bookService;


	private User checkUser(String username) {
		Optional<User> optUser = userRepository.findByUsername(username);

		if (optUser.isEmpty()) {
			throw new BaseException(new ErrorMessage(MessageType.USERNAME_NOT_FOUND, username));
		}
		return optUser.get();
	}

	@Override
	@Transactional
	public DtoReview saveReview(DtoReviewIU reviewRequest, String username) {
		User user = checkUser(username);

		Book book = bookRepository.findById(reviewRequest.getBookId()).orElseThrow(() -> new BaseException(
				new ErrorMessage(MessageType.BOOK_NOT_FOUND, reviewRequest.getBookId().toString())));

		if (reviewRepository.existsByUserAndBook(user, book) == true) {
			throw new BaseException(
					new ErrorMessage(MessageType.INVALID_OPERATION, "You already have a review for this book"));
		}

		Review review = new Review();

		review.setBook(book);
		review.setContent(reviewRequest.getContent());
		review.setRating(reviewRequest.getRating());
		review.setUser(user);

		Review savedReview = reviewRepository.save(review);

		bookService.updateBookRating(book.getId());

		return DtoConverter.toDto(savedReview);
	}

	@Override
	@Transactional
	public void deleteReview(Long reviewId, String username) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(
				() -> new BaseException(new ErrorMessage(MessageType.REVIEW_NOT_FOUND, reviewId.toString())));

		User user = checkUser(username);

		if (!review.getUser().getId().equals(user.getId())) {
			throw new BaseException(new ErrorMessage(MessageType.INVALID_OPERATION,
					"You do not have a review with id : " + reviewId.toString()));
		}

		reviewRepository.delete(review);

		bookService.updateBookRating(review.getBook().getId());

	}

	@Override
	@Transactional(readOnly = true)
	public List<DtoReview> findReviewsByBookId(Long bookId) {
		List<Review> reviews = reviewRepository.findReviewsByBookId(bookId);

		return reviews.stream().map(DtoConverter::toDto).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<DtoReview> findReviewsByBookTitle(String title) {
		List<Review> reviews = reviewRepository.findReviewsByBookTitle(title);

		return reviews.stream().map(DtoConverter::toDto).collect(Collectors.toList());
	}

}
