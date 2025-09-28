package com.example.ecommerce_api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret; // should be sufficiently long

    @Value("${jwt.expiration-ms:900000}")
    private long expirationMs;

    private Key key;

    @PostConstruct
    public void init() {
        // if secret is base64, decode accordingly. For simplicity we derive key from bytes:
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String subject) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateAndGetSubject(String token) throws JwtException {
        Jws<Claims> parsed = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        return parsed.getBody().getSubject();
    }
}

