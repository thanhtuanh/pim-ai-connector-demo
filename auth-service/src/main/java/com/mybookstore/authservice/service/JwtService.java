package com.mybookstore.authservice.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    private static final String SECRET_KEY = "mysecretkeymysecretkeymysecretkey123456"; // min. 256 bit

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuer("auth-service")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 Tag
                .signWith(getSigningKey())
                .compact();

        log.debug("🎫 JWT für Benutzer '{}' generiert", username);
        return token;
    }

    public String extractUsername(String token) {
        String username = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        log.debug("👤 Benutzername aus JWT extrahiert: {}", username);
        return username;
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            log.debug("✅ JWT-Token ist gültig");
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("⚠️ JWT ist abgelaufen: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("⚠️ JWT wird nicht unterstützt: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("⚠️ JWT ist ungültig: {}", e.getMessage());
        } catch (SignatureException e) {
            log.warn("⚠️ JWT-Signatur ungültig: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("⚠️ JWT-String leer oder null: {}", e.getMessage());
        }

        return false;
    }
}
