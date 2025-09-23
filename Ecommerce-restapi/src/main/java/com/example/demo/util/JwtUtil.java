// // package com.example.demo.util;

// // import io.jsonwebtoken.Jwts;
// // import io.jsonwebtoken.SignatureAlgorithm;

// // import java.util.Date;
// // import javax.crypto.spec.SecretKeySpec;
// // import java.security.Key;

// // public class JwtUtil {
// //     private static final String SECRET = "aVeryLongRandomSecretKeyForJwtToken123456!"; // Replace with your actual secret key

// //     public static String generateToken(String email) {
// //         Key hmacKey = new SecretKeySpec(SECRET.getBytes(), SignatureAlgorithm.HS256.getJcaName());
// //         return Jwts.builder()
// //                 .setSubject(email)
// //                 .setIssuedAt(new Date())
// //                 .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
// //                 .signWith(hmacKey, SignatureAlgorithm.HS256)
// //                 .compact();
// //     }

// //     public static String getSecret() {
// //         return SECRET;
// //     }
// // }

// package com.example.demo.util;

// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;

// import java.util.Date;
// import javax.crypto.spec.SecretKeySpec;
// import java.security.Key;

// public class JwtUtil {
//     private static final String SECRET = "mysecretkey12345";

//     public static String generateToken(String email) {
//         Key hmacKey = new SecretKeySpec(SECRET.getBytes(), SignatureAlgorithm.HS256.getJcaName());
//         return Jwts.builder()
//                 .setSubject(email)
//                 .setIssuedAt(new Date())
//                 .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
//                 .signWith(hmacKey, SignatureAlgorithm.HS256)
//                 .compact();
//     }
//     public static void validateToken(String token) {
//         Key hmacKey = new SecretKeySpec(SECRET.getBytes(), SignatureAlgorithm.HS256.getJcaName());
//         Jwts.parserBuilder()
//                 .setSigningKey(hmacKey)
//                 .build()
//                 .parseClaimsJws(token);
//     }
// }

package com.example.demo.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // or load from properties

    public String generateToken(String username) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 3600_000)) // 1 hour
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
