package com.KayraAtalay.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoBook extends DtoBase {
	
	private String googleBooksId;

	private String title;

	private String authorName;

	private BigDecimal rating;

	private Integer publicationYear;

	private Integer reviewCount;

}
