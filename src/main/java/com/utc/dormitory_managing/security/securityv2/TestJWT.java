//package com.utc.dormitory_managing.security.securityv2;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.SignatureException;
//import io.jsonwebtoken.security.Keys;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.util.Date;
//
//import org.springframework.beans.factory.annotation.Value;
//
//public class TestJWT {
//	@Value("${app.jwtSecret}")
//	private String jwtSecret;
//
//	@Value("${app.jwtExpirationAT}")
//	private int jwtExpirationAT;
//
//	@Value("${app.jwtExpirationRT}")
//	private int jwtExpirationRT;
//	public String token() {
//		Date now = new Date();
//		Date expiryDate = new Date(now.getTime() + jwtExpirationAT);
//		Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
//		String jwt = "";
//		try {
//			jwt =Jwts.builder().setSubject("check").setIssuedAt(new Date())
//					.setExpiration(new Date((new Date()).getTime() + jwtExpirationAT))
//					.signWith(secretKey, SignatureAlgorithm.HS256).compact();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	
//	
//    public static void main(String[] args) {
//        String jwtSecret = "your_secret_key_here"; // Đảm bảo rằng bạn sử dụng cùng một secret key
//        Key secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
//        
//        String token = "your_jwt_token_here"; // JWT mà bạn nhận được từ client
//        
//        try {
//            // Giải mã và xác thực token
//            String userId = Jwts.parserBuilder()
//                                 .setSigningKey(secretKey)
//                                 .build()
//                                 .parseClaimsJws(token) // Phân tích JWT
//                                 .getBody()
//                                 .getSubject(); // Lấy thông tin userId
//
//            System.out.println("Token hợp lệ, User ID: " + userId);
//        } catch (SignatureException e) {
//            System.out.println("Lỗi xác thực JWT: Chữ ký không hợp lệ!");
//        } catch (Exception e) {
//            System.out.println("Lỗi khác khi xác thực token: " + e.getMessage());
//        }
//    }
//}
