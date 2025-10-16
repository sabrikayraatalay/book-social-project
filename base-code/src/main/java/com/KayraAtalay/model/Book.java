package com.KayraAtalay.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseEntity {
	
	@Column(unique = true, name = "google_books_id") 
    private String googleBooksId;

	@Column(name = "title", nullable = false)
	private String title;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id", nullable = false)
	private Author author;
	
	@Column(precision = 3, scale = 1) 
    private BigDecimal rating;

	@Column(name = "publication_year", nullable = false)
	private Integer publicationYear;
	
	@Column(name = "review_count")
	private Integer reviewCount;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
	private Set<Review> reviews = new HashSet<>();

}