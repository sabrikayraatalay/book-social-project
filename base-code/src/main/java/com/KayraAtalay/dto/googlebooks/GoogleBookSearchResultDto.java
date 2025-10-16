package com.KayraAtalay.dto.googlebooks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GoogleBookSearchResultDto {
    private String googleBookId;
    private String title;
    private List<String> authors;
    private String thumbnailUrl; //Url for book cover photo
}