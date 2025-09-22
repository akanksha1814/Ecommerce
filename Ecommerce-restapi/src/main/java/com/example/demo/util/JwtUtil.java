// package com.example.demo.util;

// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;

// import java.util.Date;
// import javax.crypto.spec.SecretKeySpec;
// import java.security.Key;

// public class JwtUtil {
//     private static final String SECRET = "aVeryLongRandomSecretKeyForJwtToken123456!"; // Replace with your actual secret key

//     public static String generateToken(String email) {
//         Key hmacKey = new SecretKeySpec(SECRET.getBytes(), SignatureAlgorithm.HS256.getJcaName());
//         return Jwts.builder()
//                 .setSubject(email)
//                 .setIssuedAt(new Date())
//                 .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
//                 .signWith(hmacKey, SignatureAlgorithm.HS256)
//                 .compact();
//     }

//     public static String getSecret() {
//         return SECRET;
//     }
// }

package com.example.demo.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class JwtUtil {
    private static final String SECRET = "mysecretkey12345";

    public static String generateToken(String email) {
        Key hmacKey = new SecretKeySpec(SECRET.getBytes(), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(hmacKey, SignatureAlgorithm.HS256)
                .compact();
    }
    public static void validateToken(String token) {
        Key hmacKey = new SecretKeySpec(SECRET.getBytes(), SignatureAlgorithm.HS256.getJcaName());
        Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(token);
    }
}

