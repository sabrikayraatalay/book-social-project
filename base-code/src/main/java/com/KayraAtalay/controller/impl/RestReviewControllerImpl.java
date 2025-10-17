package com.KayraAtalay.controller.impl;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.KayraAtalay.controller.IRestReviewController;
import com.KayraAtalay.controller.RestBaseController;
import com.KayraAtalay.controller.RootEntity;
import com.KayraAtalay.dto.DtoReview;
import com.KayraAtalay.dto.DtoReviewIU;
import com.KayraAtalay.service.IReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rest/api/book-social/review")
public class RestReviewControllerImpl extends RestBaseController implements IRestReviewController {

	@Autowired
	private IReviewService reviewService;

	@PostMapping("/save")
	@Override
	@PreAuthorize("isAuthenticated()")
	public RootEntity<DtoReview> saveReview(@Valid @RequestBody DtoReviewIU reviewRequest, Principal principal) {

		String username = principal.getName();

		return ok(reviewService.saveReview(reviewRequest, username));
	}

	@DeleteMapping("/delete/{reviewId}")
	@Override
	@PreAuthorize("isAuthenticated()")
	public void deleteReview(@PathVariable Long reviewId, Principal principal) {

		String username = principal.getName();

		reviewService.deleteReview(reviewId, username);

	}

	@GetMapping("/find-with-book-id/{bookId}")
	@Override
	public RootEntity<List<DtoReview>> findReviewsByBookId(@PathVariable Long bookId) {
		return ok(reviewService.findReviewsByBookId(bookId));
	}

	@GetMapping("/find-with-book-title")
	@Override
	public RootEntity<List<DtoReview>> findReviewsByBookTitle(@RequestParam String title) {
		return ok(reviewService.findReviewsByBookTitle(title));
	}

}
