package com.KayraAtalay.dto.googlebooks;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddBookFromGoogleRequest {
    @NotBlank(message = "Google Book ID cannot be blank")
    private String googleBookId;
}