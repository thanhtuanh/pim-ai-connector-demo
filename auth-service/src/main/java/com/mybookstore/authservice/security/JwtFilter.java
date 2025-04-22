package com.mybookstore.authservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Value("${mybookstore.app.jwtSecret}")
    private String jwtSecret;

    @Value("${jwt.validation.enabled:true}")
    private boolean jwtValidationEnabled;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!jwtValidationEnabled) {
            logger.info("JWT-Validierung ist deaktiviert");
            filterChain.doFilter(request, response);
            return;
        }

        // Prüfen, ob es sich um einen API-Endpunkt handelt (passt für /api/books)
        String path = request.getServletPath();

        // Public-Pfade, die keine Authentifizierung benötigen
        if (path.startsWith("/public") ||
                path.startsWith("/api-docs") ||
                path.startsWith("/swagger") ||
                path.equals("/health")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Debug-Ausgabe für den aktuellen Pfad und Authorization-Header
        logger.debug("Pfad der Anfrage: {}", path);
        String authHeader = request.getHeader("Authorization");
        logger.debug("Authorization-Header: {}", authHeader);

        try {
            String token = extractToken(authHeader);

            if (token != null) {
                // Token validieren und Benutzer authentifizieren
                validateTokenAndAuthenticate(token);
            } else {
                logger.debug("Kein Token in der Anfrage gefunden");
            }
        } catch (Exception e) {
            logger.error("Fehler bei der JWT-Verarbeitung: {}", e.getMessage());
            // Wir lassen die Anfrage trotzdem durch, da Spring Security sie ggf. ablehnen
            // wird,
            // wenn eine geschützte Ressource angefragt wird
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private void validateTokenAndAuthenticate(String token) {
        try {
            // Prüfen, ob das Token das richtige Format hat
            if (token.isEmpty() || !token.contains(".")) {
                logger.error("Ungültiges JWT-Token-Format");
                return;
            }

            // Secret-Key erstellen
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            // Token parsen und Claims extrahieren
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Benutzername aus dem Token extrahieren
            String username = claims.getSubject();

            // Rollen aus dem Token extrahieren (falls vorhanden)
            String role = claims.get("role", String.class);
            List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority(role != null ? role : "ROLE_USER"));

            // Erstellen eines Authentication-Objekts
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null,
                    authorities);

            // Setzen der Authentifizierung im SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.debug("Benutzer {} erfolgreich authentifiziert mit Rolle {}", username, role);
        } catch (Exception e) {
            logger.error("JWT-Validierung fehlgeschlagen: {}", e.getMessage());
        }
    }
}