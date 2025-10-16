package com.KayraAtalay.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.KayraAtalay.model.Review;
import com.KayraAtalay.model.User;
import com.KayraAtalay.model.Book;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	
	List<Review> findReviewsByBookId(Long bookId);
	
	List<Review> findReviewsByBookTitle(String title);
	
	boolean existsByUserAndBook(User user, Book book);
	
	@Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId AND r.rating IS NOT NULL")
    BigDecimal findAverageRatingByBookId(@Param("bookId") Long bookId);

    //calculates reviewCount
    Long countByBookId(Long bookId);

}
