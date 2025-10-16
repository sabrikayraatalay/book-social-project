package com.KayraAtalay.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoReviewIU {
	
	@NotBlank(message = "Content cannot be blank")
	private String content;
	
	@Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 10, message = "Rating must be at most 10")
	private Integer rating;
	
	@NotNull(message = "Book ID cannot be null")
	private Long bookId;

}
