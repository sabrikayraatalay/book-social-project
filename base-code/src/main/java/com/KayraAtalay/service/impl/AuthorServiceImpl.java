package com.KayraAtalay.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.KayraAtalay.dto.DtoAuthor;
import com.KayraAtalay.dto.DtoAuthorIU;
import com.KayraAtalay.exception.BaseException;
import com.KayraAtalay.exception.ErrorMessage;
import com.KayraAtalay.exception.MessageType;
import com.KayraAtalay.model.Author;
import com.KayraAtalay.repository.AuthorRepository;
import com.KayraAtalay.service.IAuthorService;
import com.KayraAtalay.utils.DtoConverter;



@Service
public class AuthorServiceImpl implements IAuthorService {

	@Autowired
	private AuthorRepository authorRepository;

	private Author createAuthor(DtoAuthorIU authorRequest) {
		Author author = new Author();
		author.setName(authorRequest.getName());
		author.setBirthYear(authorRequest.getBirthYear());
		author.setCountry(authorRequest.getCountry());
		return author;
	}

	@Override
	@Transactional
	public DtoAuthor saveAuthor(DtoAuthorIU authorRequest) {

		Author author = createAuthor(authorRequest);

		Author savedAuthor = authorRepository.save(author);

		return DtoConverter.toDto(savedAuthor);
	}

	@Override
	@Transactional
	public DtoAuthor updateAuthor(Long authorId, DtoAuthorIU authorRequest) {

		Optional<Author> optAuthor = authorRepository.findById(authorId);

		if (optAuthor.isEmpty()) {
			throw new BaseException(new ErrorMessage(MessageType.AUTHOR_NOT_FOUND, authorId.toString()));
		}															

		Author authorToUpdate = optAuthor.get();

		BeanUtils.copyProperties(authorRequest, authorToUpdate);

		Author updatedAuthor = authorRepository.save(authorToUpdate);

		return DtoConverter.toDto(updatedAuthor);
	}

	@Override
	@Transactional
	public void deleteAuthor(Long authorId) {

		Optional<Author> optAuthor = authorRepository.findById(authorId);

		if (optAuthor.isEmpty()) {
			throw new BaseException(new ErrorMessage(MessageType.AUTHOR_NOT_FOUND, authorId.toString()));
		}

		Author author = optAuthor.get();

		if (!author.getBooks().isEmpty()) {
			throw new BaseException(new ErrorMessage(MessageType.INVALID_OPERATION,
					"This author has at least 1 book so you can not delete this author"));
		}

		authorRepository.delete(author);

	}

	@Override
	public Page<DtoAuthor> findAllPageable(Pageable pageable) {
		Page<Author> page = authorRepository.findAll(pageable);

		return page.map(DtoConverter::toDto);
	}

	@Override
	public DtoAuthor findAuthorById(Long authorId) {
		Optional<Author> optAuthor = authorRepository.findById(authorId);

		if (optAuthor.isEmpty()) {
			throw new BaseException(new ErrorMessage(MessageType.AUTHOR_NOT_FOUND, authorId.toString()));
		}

		return DtoConverter.toDto(optAuthor.get());
	}

	@Override
	public List<DtoAuthor> searchAuthorsByName(String name) {

		List<Author> authorListByName = authorRepository.findByNameStartingWithIgnoreCase(name);

		return authorListByName.stream().map(DtoConverter::toDto).collect(Collectors.toList());
	}

}
