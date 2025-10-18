package com.KayraAtalay.exception;

@SuppressWarnings("serial")
public class BaseException extends RuntimeException {
	
	private final ErrorMessage errorMessage;

	public BaseException(ErrorMessage errorMessage) {
		super(errorMessage.prepareErrorMessage());
		this.errorMessage = errorMessage;
	}
	
	public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

}