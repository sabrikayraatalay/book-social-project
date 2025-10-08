package com.KayraAtalay.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.KayraAtalay.utils.PageableRequest;
import com.KayraAtalay.utils.PagerUtil;
import com.KayraAtalay.utils.RestPageableEntity;

public class RestBaseController {

	public Pageable toPageable(PageableRequest request) {
		return PagerUtil.toPageable(request);
	}

	public <T> RestPageableEntity<T> toPageableResponse(Page<?> page, List<T> content) {

		return PagerUtil.toPageableResponse(page, content);

	}

	public <T> RootEntity<T> ok(T payload) {
		return RootEntity.ok(payload);
	}

}