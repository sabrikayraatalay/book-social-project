package com.KayraAtalay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.KayraAtalay.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

	List<Author> findByNameStartingWithIgnoreCase(String name);

}
