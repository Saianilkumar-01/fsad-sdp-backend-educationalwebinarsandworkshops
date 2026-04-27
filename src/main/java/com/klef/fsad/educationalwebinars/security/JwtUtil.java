package com.klef.fsad.educationalwebinars.security;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil 
{
    // Strong secret key (you can override in application.properties)
    @Value("${jwt.secret:MySuperStrongSecretKeyForJWT2026KlefFSDVijayawada@1234567890ABCDEF}")
    private String SECRET;
    
    public String generateToken(UserDetails userDetails) 
    {
        String role = userDetails.getAuthorities()
                .iterator().next().getAuthority();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) // 10 minutes
                .setId(UUID.randomUUID().toString())           // Makes every token unique
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) 
    {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) 
    {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) 
    {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) 
    {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .setAllowedClockSkewSeconds(30)   // Allow 30 seconds difference
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() 
    {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
}
