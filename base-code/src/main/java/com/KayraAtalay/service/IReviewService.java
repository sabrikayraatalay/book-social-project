package com.KayraAtalay.service;

import java.util.List;

import com.KayraAtalay.dto.DtoReview;
import com.KayraAtalay.dto.DtoReviewIU;

public interface IReviewService {

	// #AUTHENTICATED USER
    DtoReview saveReview(DtoReviewIU reviewRequest, String username);
    void deleteReview(Long reviewId, String username); //users can only delete their own comment

    // #PUBLIC 
    List<DtoReview> findReviewsByBookId(Long bookId);
    
    List<DtoReview> findReviewsByBookTitle(String title);
	
}
