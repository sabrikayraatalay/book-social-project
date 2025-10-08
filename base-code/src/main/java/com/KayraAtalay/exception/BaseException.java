package com.KayraAtalay.exception;

@SuppressWarnings("serial")
public class BaseException extends RuntimeException {

	public BaseException(ErrorMessage errorMessage) {
		super(errorMessage.prepareErrorMessage());
	}

}