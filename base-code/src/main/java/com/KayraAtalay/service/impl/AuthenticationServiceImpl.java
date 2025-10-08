package com.KayraAtalay.service.impl;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.KayraAtalay.dto.AuthRequest;
import com.KayraAtalay.dto.AuthResponse;
import com.KayraAtalay.dto.DtoUser;
import com.KayraAtalay.dto.RefreshTokenRequest;
import com.KayraAtalay.exception.BaseException;
import com.KayraAtalay.exception.ErrorMessage;
import com.KayraAtalay.exception.MessageType;
import com.KayraAtalay.jwt.JwtService;
import com.KayraAtalay.model.RefreshToken;
import com.KayraAtalay.model.User;
import com.KayraAtalay.repository.RefreshTokenRepository;
import com.KayraAtalay.repository.UserRepository;
import com.KayraAtalay.service.IAuthenticationService;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	//Below injections are for authentication 
	
	@Autowired
	private AuthenticationProvider authenticationProvider; 
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	
	
	
	private User createUser(AuthRequest request) {
		
		User user = new User();
		user.setCreateTime(new Date());
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		return user;
	}
	
	private RefreshToken createRefreshToken(User user) {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setCreateTime(new Date());
		refreshToken.setExpireDate(new Date(System.currentTimeMillis() + 1000*60*60*4));
		refreshToken.setRefreshToken(UUID.randomUUID().toString());
		refreshToken.setUser(user);
		
		return refreshToken;
	}

	@Override
	public DtoUser register(AuthRequest request) {
		
		Optional<User> optional = userRepository.findByUsername(request.getUsername());	
		
		if (optional.isPresent()) {
			throw new BaseException(new ErrorMessage(MessageType.USERNAME_ALREADY_EXISTS, " : " + request.getUsername()));
		}
		
		User savedUser = userRepository.save(createUser(request));
		DtoUser dtoUser = new DtoUser();
	    BeanUtils.copyProperties(savedUser, dtoUser);
		
		
		return dtoUser;
	}

	@Override
	public AuthResponse authenticate(AuthRequest request) {
		try {
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
			
			authenticationProvider.authenticate(authenticationToken);
			
			Optional<User> optUser = userRepository.findByUsername(request.getUsername());
			
			String accessToken = jwtService.generateToken(optUser.get());
			RefreshToken savedRefreshToken = refreshTokenRepository.save(createRefreshToken(optUser.get()));
			
			return new AuthResponse(accessToken, savedRefreshToken.getRefreshToken());
			
			
		} catch (Exception e) {
			throw new BaseException(new ErrorMessage(MessageType.USERNAME_OR_PASSWORD_INVALID, e.getMessage()));		
		   }
		
	}
	
	public boolean isValidRefreshToken(RefreshToken refreshToken) {
		return new Date().before(refreshToken.getExpireDate());
	}

	@Override
	public AuthResponse refreshToken(RefreshTokenRequest request) {
		
		Optional<RefreshToken> optional = refreshTokenRepository.findByRefreshToken(request.getRefreshToken());
		
		 if(optional.isEmpty()) {
			 throw new BaseException(new ErrorMessage(MessageType.REFRESH_TOKEN_NOT_FOUND, request.getRefreshToken()));
		 }
		 
		 if(!isValidRefreshToken(optional.get())) {
			 throw new BaseException(new ErrorMessage(MessageType.REFRESH_TOKEN_EXPIRED, request.getRefreshToken()));
		 }
		 
		 User user = optional.get().getUser();
		 String accessToken = jwtService.generateToken(user);
		 RefreshToken savedRefreshToken =  refreshTokenRepository.save(createRefreshToken(user));
		 
		return new AuthResponse(accessToken, savedRefreshToken.getRefreshToken());
	}

}
