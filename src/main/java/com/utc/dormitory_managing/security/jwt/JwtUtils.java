package com.utc.dormitory_managing.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.utc.dormitory_managing.apis.error.BadRequestAlertException;
import com.utc.dormitory_managing.entity.User;
import com.utc.dormitory_managing.repository.UserRepo;
import com.utc.dormitory_managing.security.service.UserDetailsImpl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

   @Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpirationAT}")
	private String jwtExpirationAT;

	@Value("${app.jwtExpirationRT}")
	private String jwtExpirationRT;
	
	@Autowired
	private UserRepo userRepo;

  public String generateAccessToken(Authentication authentication) {

//    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
//
//    return Jwts.builder()
//        .setSubject((userPrincipal.getUsername()))
//        .setIssuedAt(new Date())
//        .setExpiration(new Date((new Date()).getTime() + jwtExpirationAT))
//        .signWith(key(), SignatureAlgorithm.HS256)
//        .compact();
	  
	  	UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		Date now = new Date();
		Long jwt = Long.parseLong(jwtExpirationAT);
		Date expiryDate = new Date(now.getTime() + jwt);
		
		String accessToken = Jwts.builder().setSubject(userPrincipal.getId())
				.setIssuedAt(new Date())
				.setExpiration(expiryDate)
				.setId(UUID.randomUUID().toString())
		        .signWith(key(), SignatureAlgorithm.HS256).compact();
		return accessToken;
  }
  
  public String generateRefreshToken(Authentication authentication) {

	    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		Date now = new Date();
		Long jwt = Long.parseLong(jwtExpirationRT);
		Date expiryDate = new Date(now.getTime() + jwt);
		return Jwts.builder().setSubject(String.valueOf(userPrincipal.getId())).setIssuedAt(new Date())
				.setExpiration(expiryDate)
				.setId(UUID.randomUUID().toString())
		        .signWith(key(), SignatureAlgorithm.HS256).compact();
	  }
  
//  private Key key() {
//    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
//  }
  
  private Key key() {
      SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
//	  SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
//      String secretString = Encoders.BASE64.encode(key.getEncoded());
//      System.err.println("Secret key: " + secretString);
      return key;
  }

  public String getUserNameFromJwtToken(String token) {
	  	Claims claims = Jwts.parserBuilder().setSigningKey(key()).build()
	              .parseClaimsJws(token).getBody();
		Optional<User> user = userRepo.findByUserId(claims.getSubject());
		if(user.isEmpty()) throw new BadRequestAlertException("Not Found User", "user", "missing");
		return user.get().getUsername();
  }

  public String getUserIdFromJwtToken(String token) {
	  return Jwts.parserBuilder().setSigningKey(key()).build()
              .parseClaimsJws(token).getBody().getSubject();
}
  
  public boolean validateJwtToken(String authToken) {
    try {
    	Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
        return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }
}
