package com.questapp.QuestApp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${questapp.app.secret}")
    private String APP_SECRET;

    @Value("${accessToken.expires.in}")
    private long EXPIRES_IN;

    public String generateToken(Authentication auth) {
        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);
        SecretKey key = Keys.hmacShaKeyFor(APP_SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder().subject(Long.toString(userDetails.getId()))
                .issuedAt(new Date()).expiration(expireDate)
                .signWith(key).compact();
    }

    Long getUserIdFromToken(String token) {
        Claims body = getDecoded(token).getPayload();
        return Long.parseLong(body.getSubject());
    }

    boolean validateToken(String token) {
        try {
            getDecoded(token);
            return !isTokenExpired(token);
        }catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException |
                IllegalArgumentException e){
            return false;
        }
    }

    boolean isTokenExpired(String token) {
        Date expiration = getDecoded(token).getPayload().getExpiration();
        return expiration.before(new Date());
    }

    Jws<Claims> getDecoded(String token) {
        SecretKey key = Keys.hmacShaKeyFor(APP_SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
    }

    public String generateTokenByUserId(Long id) {
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);
        SecretKey key = Keys.hmacShaKeyFor(APP_SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder().subject(Long.toString(id))
                .issuedAt(new Date()).expiration(expireDate)
                .signWith(key).compact();
    }
}
