package com.utc.dormitory_managing.security.securityv2;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.utc.dormitory_managing.utils.exception.UnauthorizedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

//	@Autowired
//	private InvalidTokenRepo invalidTokenRepo;

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpirationAT}")
	private int jwtExpirationAT;

	@Value("${app.jwtExpirationRT}")
	private int jwtExpirationRT;

public static final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	public String generateAccessToken(Authentication authentication) {
		System.err.println(jwtSecret);
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationAT);
		Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
		String jwt = "";
		try {
			jwt =Jwts.builder().setSubject((userPrincipal.getId())).setIssuedAt(new Date())
					.setExpiration(new Date((new Date()).getTime() + jwtExpirationAT))
					.signWith(secretKey, SignatureAlgorithm.HS256).compact();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jwt;
	}

	public String generateRefreshToken(String uid) {

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationRT);
		Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
		return Jwts.builder().setSubject(String.valueOf(uid)).setId(UUID.randomUUID().toString())
				.setIssuedAt(new Date()).setExpiration(expiryDate).signWith(secretKey, SignatureAlgorithm.HS256).compact();
	}

	private Key key() {
		return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
//		return Keys.secretKeyFor(SignatureAlgorithm.HS256)
	}
	
	public String getUserIdFromJWT(String token) {
		Claims claims = Jwts.parserBuilder()
	            .setSigningKey(secretKey)
	            .build()
	            .parseClaimsJws(token) // Phân tích JWT
	            .getBody();
		System.err.println("check44");
		return String.valueOf(claims.getSubject());
	}
	public boolean validateToken(String authToken) {
		try {
			System.err.println("check2");
			String jwt =Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(authToken) // Phân tích JWT
            .getBody()
            .getSubject();
			return true;
		} catch (SignatureException ex) {
			System.out.println("Invalid JWT signature");
			throw new UnauthorizedException("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			System.out.println("Invalid JWT token");
			throw new UnauthorizedException("Invalid JWT signature");
		} catch (ExpiredJwtException ex) {
			System.out.println("Expired JWT token");
			throw new UnauthorizedException("Invalid JWT signature");
		} catch (UnsupportedJwtException ex) {
			System.out.println("Unsupported JWT token");
			throw new UnauthorizedException("Invalid JWT signature");
		} catch (IllegalArgumentException ex) {
			System.out.println("JWT claims string is empty.");
			throw new UnauthorizedException("Invalid JWT signature");
		}
	}
	
}