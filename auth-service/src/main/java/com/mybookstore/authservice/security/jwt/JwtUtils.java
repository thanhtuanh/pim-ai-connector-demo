package com.mybookstore.authservice.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${mybookstore.app.jwtSecret}")
    private String jwtSecret;

    @Value("${mybookstore.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // Angepasst, um mit Standard-UserDetails zu arbeiten 
    public String generateJwtToken(UserDetails userDetails) {
        return generateTokenFromUsername(userDetails.getUsername());
    }

    // Einfache Methode für die Tokengenerierung mit einem Benutzernamen
    public String generateToken(String username) {
        return generateTokenFromUsername(username);
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Ungültige JWT-Signatur: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Ungültiges JWT-Token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT-Token ist abgelaufen: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT-Token wird nicht unterstützt: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT Claims-String ist leer: {}", e.getMessage());
        }

        return false;
    }
}