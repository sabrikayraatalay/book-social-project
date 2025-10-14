package com.KayraAtalay.controller.impl;

import java.util.List;

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

import com.KayraAtalay.controller.IRestAuthorController;
import com.KayraAtalay.controller.RestBaseController;
import com.KayraAtalay.controller.RootEntity;
import com.KayraAtalay.dto.DtoAuthor;
import com.KayraAtalay.dto.DtoAuthorIU;
import com.KayraAtalay.service.IAuthorService;
import com.KayraAtalay.utils.PageableRequest;
import com.KayraAtalay.utils.RestPageableEntity;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rest/api/author")
public class RestAuthorControllerImpl extends RestBaseController implements IRestAuthorController {

	@Autowired
	private IAuthorService authorService;

	@PostMapping("/admin/save")
	@PreAuthorize("hasAuthority('ADMIN')")
	@Override
	public RootEntity<DtoAuthor> saveAuthor(@Valid @RequestBody DtoAuthorIU authorRequest) {
		return ok(authorService.saveAuthor(authorRequest));
	}

	@PutMapping("/admin/update/{authorId}") 
	@PreAuthorize("hasAuthority('ADMIN')")
	@Override
	public RootEntity<DtoAuthor> updateAuthor(@PathVariable Long authorId,
			@Valid @RequestBody DtoAuthorIU authorRequest) {
		return ok(authorService.updateAuthor(authorId, authorRequest));
	}

	@DeleteMapping("/admin/delete/{authorId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	@Override
	public void deleteAuthor(@PathVariable Long authorId) {
		authorService.deleteAuthor(authorId);

	}

	@GetMapping("/list/pageable")
	@Override
	public RootEntity<RestPageableEntity<DtoAuthor>> findAllPageable(PageableRequest pageableRequest) {
		Page<DtoAuthor> page = authorService.findAllPageable(toPageable(pageableRequest));
		return ok(toPageableResponse(page, page.getContent()));
	}

	@GetMapping("/{authorId}")
	@Override
	public RootEntity<DtoAuthor> findAuthorById(@PathVariable Long authorId) {
		return ok(authorService.findAuthorById(authorId));
	}

	@GetMapping("find-by-name")
	@Override
	public RootEntity<List<DtoAuthor>> searchAuthorsByName(@RequestParam String name) {
		return ok(authorService.searchAuthorsByName(name));
	}

}
