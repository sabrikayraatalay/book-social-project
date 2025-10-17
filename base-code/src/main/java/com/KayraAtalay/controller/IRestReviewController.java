package com.KayraAtalay.controller;

import java.security.Principal;
import java.util.List;

import com.KayraAtalay.dto.DtoReview;
import com.KayraAtalay.dto.DtoReviewIU;

public interface IRestReviewController {

	// AUTHENTICATED USER
	RootEntity<DtoReview> saveReview(DtoReviewIU reviewRequest, Principal principal);

	void deleteReview(Long reviewId, Principal principal);

	// ALL USERS
	RootEntity<List<DtoReview>> findReviewsByBookId(Long bookId);

	RootEntity<List<DtoReview>> findReviewsByBookTitle(String title);

}
