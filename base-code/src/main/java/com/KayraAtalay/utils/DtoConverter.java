package com.KayraAtalay.utils;

import org.springframework.beans.BeanUtils;

import com.KayraAtalay.dto.DtoUser;
import com.KayraAtalay.model.User;

public class DtoConverter {

	public static DtoUser toDto(User user) {
		DtoUser dtoUser = new DtoUser();

		BeanUtils.copyProperties(user, dtoUser);

		return dtoUser;

	}
	
}
