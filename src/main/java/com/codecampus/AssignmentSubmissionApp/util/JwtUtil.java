package com.codecampus.AssignmentSubmissionApp.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // Generate Key (UPDATED)
    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract Username
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract Expiration
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic Claim Extractor
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    // UPDATED parser (no deprecated parser())
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())   // ✅ replaces setSigningKey()
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Expiration Check
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Generate Token
    public String generateToken(UserDetails userDetails) {
        String authorities= userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("authorities",authorities)
                .issuedAt(new Date())                 
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) 
                .signWith(getSignKey())             
                .compact();
    }

    //Extracts the authorities string from the token
    public String extractAuthorities(String token){
        return extractClaim(token, claims -> claims.get("authorities",String.class));
    }
    // ✅ Validate Token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}