package com.KayraAtalay.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoAuthorIU {
	
	@NotBlank(message = "Author name cannot be blank")
    @Size(min = 2, max = 50, message = "Author name must be between 2 and 50 characters")
	@Pattern(regexp = "^[a-zA-Z .'-]+$", message = "Author name can only contain letters, spaces, periods, and apostrophes")
	private String name;
	
    @Min(value = 0, message = "Birth year cannot be a negative number")
    @Max(value = 2025, message = "Birth year cannot be in the future") // Current year check
    @Digits(integer = 4, fraction = 0, message = "Birth year must be a 4-digit number")
	private Integer birthYear;
	
    @Size(min = 2, max = 50, message = "Country name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z .'-]+$", message = "Country name can only contain letters, spaces, periods, and apostrophes")
	private String country;
	

}
