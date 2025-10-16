package com.KayraAtalay.dto.googlebooks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleBookItem {
    private String id; //which is taken from google
    private VolumeInfo volumeInfo;
}