package com.supan.storage.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Service to generate and validate secure tokens for file access
 */
@Service
public class StorageTokenService {

    @Value("${app.image.token.secret}")
    private String tokenSecret;

    @Value("${app.image.token.expiration:300}") // Default 5 minutes
    private long tokenExpirationInSeconds;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(tokenSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generate a token for accessing a specific file path
     */
    public String generateToken(String filePath) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + tokenExpirationInSeconds * 1000);

        return Jwts.builder()
                .setSubject(filePath)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generate a token with custom expiration time
     */
    public String generateToken(String filePath, long expirationInSeconds) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationInSeconds * 1000);

        return Jwts.builder()
                .setSubject(filePath)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validate a token and return the file path if valid
     */
    public String validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            return null; // Token is invalid or expired
        }
    }
}
