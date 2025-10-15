package com.KayraAtalay.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookUpdateRequest {

	@NotNull(message = "Title cannot be null")
	@Size(min = 1, max = 255, message = "Title length must be between 1 and 255 characters")
	private String title;

	@NotNull(message = "Publication year cannot be null")
	@Min(value = 1000, message = "Publication year must be a 4-digit number")
	private Integer publicationYear;

}
