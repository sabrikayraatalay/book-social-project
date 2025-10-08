package com.KayraAtalay.exception;

import lombok.Getter;

@Getter
public enum MessageType {

	NO_RECORD_EXIST("1003", "No records found"), GENERAL_EXCEPTION("9999", "There is an error"),
	TOKEN_EXPIRED("1005", "Token is expired"), WRONG_TOKEN("1006", "This token is not exist"),
	USERNAME_NOT_FOUND("1007", "Could not find the username"),
	USERNAME_ALREADY_EXISTS("1008", "This username is already exists"),
	USERNAME_OR_PASSWORD_INVALID("1009", "Wrong username or password"),
	REFRESH_TOKEN_NOT_FOUND("1010", "Could not find the refresh token"),
	REFRESH_TOKEN_EXPIRED("1011", "This refresh token is expired");

	private String code;

	private String message;

	MessageType(String code, String message) {
		this.code = code;
		this.message = message;

	}

}