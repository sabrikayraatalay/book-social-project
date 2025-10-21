package com.KayraAtalay.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.KayraAtalay.controller.IRestAuthenticationController;
import com.KayraAtalay.controller.RestBaseController;
import com.KayraAtalay.controller.RootEntity;
import com.KayraAtalay.dto.AuthRequest;
import com.KayraAtalay.dto.AuthResponse;
import com.KayraAtalay.dto.DtoUser;
import com.KayraAtalay.dto.RefreshTokenRequest;
import com.KayraAtalay.service.IAuthenticationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rest/api/book-social")
public class RestAuthenticationControllerImpl extends RestBaseController implements IRestAuthenticationController {

	@Autowired
	private IAuthenticationService authenticationService;

	@PostMapping("/register")
	@Override
	public RootEntity<DtoUser> register(@Valid @RequestBody AuthRequest request) {
		return ok(authenticationService.register(request));
	}

	@PostMapping("/authenticate")
	@Override
	public RootEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
		return ok(authenticationService.authenticate(request));
	}

	@PostMapping("/refreshToken")
	@Override
	public RootEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
		return ok(authenticationService.refreshToken(request));
	}

}
