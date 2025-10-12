package com.KayraAtalay.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoAuthor extends DtoBase {
	
	private String name;
	
	private Set<DtoBookWithoutAuthor> books = new HashSet<>();

}
