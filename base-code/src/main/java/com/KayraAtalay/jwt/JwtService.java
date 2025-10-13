package com.KayraAtalay.jwt;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.KayraAtalay.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    public static final String SECRET_KEY = "/gL7+WzWy/GRbIKlBw7zKqgMRFxu04dBpA1T+A9vj6U=";

    // Create JWT token including user's role
    @SuppressWarnings("deprecation")
	public String generateToken(UserDetails userDetails) {
        User user = (User) userDetails;
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", user.getRole().name()) // Add role claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)) // 2 hours expiry
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract any claim from JWT token
    public <T> T getClaim(String token, Function<Claims, T> claimsFunc) {
        Claims claims = getClaims(token);
        return claimsFunc.apply(claims);
    }

    // Extract raw claims
    @SuppressWarnings("deprecation")
	public Claims getClaims(String token) {
        return Jwts.parser() // Use parserBuilder instead of deprecated parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Get username from token
    public String getUsernameByToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    // Get role from token
    public String getRoleByToken(String token) {
        return getClaim(token, claims -> claims.get("role", String.class));
    }

    // Check if token is still valid
    public boolean isTokenValid(String token) {
        Date expireDate = getClaim(token, Claims::getExpiration);
        return new Date().before(expireDate);
    }

    // Generate key from secret
    public Key getKey() {
        byte[] bytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(bytes);
    }
}
