package com.KayraAtalay.controller.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.KayraAtalay.controller.IRestBookController;
import com.KayraAtalay.controller.RestBaseController;
import com.KayraAtalay.controller.RootEntity;
import com.KayraAtalay.dto.BookUpdateRequest;
import com.KayraAtalay.dto.DtoBook;
import com.KayraAtalay.dto.DtoBookIU;
import com.KayraAtalay.service.IBookService;
import com.KayraAtalay.utils.PageableRequest;
import com.KayraAtalay.utils.RestPageableEntity;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rest/api/book-social/book")
public class RestBookControllerImpl extends RestBaseController implements IRestBookController {

	@Autowired
	private IBookService bookService;

	@PostMapping("/admin/save")
	@Override
	@PreAuthorize("hasAuthority('ADMIN')")
	public RootEntity<DtoBook> saveBook(@Valid @RequestBody DtoBookIU bookRequest) {
		return ok(bookService.saveBook(bookRequest));
	}

	@PutMapping("/admin/update/{bookId}")
	@Override
	@PreAuthorize("hasAuthority('ADMIN')")
	public RootEntity<DtoBook> updateBook(@PathVariable Long bookId,
			@Valid @RequestBody BookUpdateRequest bookUpdateRequest) {
		return ok(bookService.updateBook(bookId, bookUpdateRequest));
	}

	@DeleteMapping("/admin/delete/{bookId}")
	@Override
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteBook(@PathVariable Long bookId) {
		bookService.deleteBook(bookId);
	}

	@GetMapping("/list/pageable")
	@Override
	public RootEntity<RestPageableEntity<DtoBook>> findAllPageable(PageableRequest pageable) {
		Page<DtoBook> page = bookService.findAllPageable(toPageable(pageable));
		return ok(toPageableResponse(page, page.getContent()));
	}

	@GetMapping("/{bookId}")
	@Override
	public RootEntity<DtoBook> findBookById(@PathVariable Long bookId) {
		return ok(bookService.findBookById(bookId));
	}

	@GetMapping("/find-by-title")
	@Override
	public RootEntity<List<DtoBook>> searchBooksByTitle(@RequestParam String title) {
		return ok(bookService.searchBooksByTitle(title));
	}

	@GetMapping("/find-by-author-id/{authorId}")
	@Override
	public RootEntity<Set<DtoBook>> findBooksByAuthorId(@PathVariable Long authorId) {
		return ok(bookService.findBooksByAuthorId(authorId));
	}

}