package com.KayraAtalay.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.KayraAtalay.handler.AuthEntryPoint;
import com.KayraAtalay.jwt.JwtAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	public static final String REGISTER = "/rest/api/book-social/register";
	public static final String AUTHENTICATE = "/rest/api/book-social/authenticate";
	public static final String REFRESH_TOKEN = "/rest/api/book-social/refreshToken";

	@Autowired
	private AuthenticationProvider authenticationProvider;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	private AuthEntryPoint authEntryPoint;

	@SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().
				cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.authorizeHttpRequests(request -> request.requestMatchers(REGISTER, AUTHENTICATE, REFRESH_TOKEN,"/rest/api/book-social/book/list/pageable",
				        "/rest/api/book-social/book/find-by-title",
				        "/rest/api/book-social/book/{bookId}",
				        "/rest/api/book-social/author/find-by-name")
						.permitAll().anyRequest().authenticated())
				.exceptionHandling().authenticationEntryPoint(authEntryPoint).and()
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	// Bu Bean, Spring Security'e hangi adreslere izin vereceğimizi söyler
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		// Sadece bizim React uygulamamızın çalıştığı adrese izin veriyoruz
		configuration.setAllowedOrigins(List.of("http://localhost:5173"));

		// Hangi HTTP metotlarına (GET, POST vb.) izin verileceği
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

		// Hangi başlıklara (Header) izin verileceği (JWT için 'Authorization' şart)
		configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration); // Bu ayarları tüm API yolları için geçerli yap

		return source;
	}

}