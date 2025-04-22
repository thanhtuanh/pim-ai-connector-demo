package com.mybookstore.authservice.controller;

import com.mybookstore.authservice.dto.TokenRefreshRequest;
import com.mybookstore.authservice.dto.TokenRefreshResponse;
import com.mybookstore.authservice.model.RefreshToken;
import com.mybookstore.authservice.model.UserEntity;
import com.mybookstore.authservice.repository.UserRepository;
import com.mybookstore.authservice.service.JwtService;
import com.mybookstore.authservice.service.RefreshTokenService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
// @CrossOrigin-Annotation wurde entfernt
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserEntity user) {
        log.info("📥 Registrierungsversuch für Benutzer: {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        log.info("✅ Benutzer erfolgreich registriert: {}", user.getUsername());
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        log.info("🔐 Login-Anfrage erhalten für: {}", request.getUsername());

        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.warn("❌ Benutzer nicht gefunden: {}", request.getUsername());
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht gefunden");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("❌ Passwort falsch für Benutzer: {}", request.getUsername());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Falsches Passwort");
        }

        log.info("✅ Benutzer authentifiziert: {}", user.getUsername());

        String accessToken = jwtService.generateToken(user.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        log.debug("🎫 Token generiert für {}: {}", user.getUsername(), accessToken);
        log.debug("🔄 Refresh-Token: {}", refreshToken.getToken());

        AuthResponse response = new AuthResponse();
        response.setUsername(user.getUsername());
        response.setToken(accessToken);
        response.setRefreshToken(refreshToken.getToken());
        response.setRole(user.getRole());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        log.info("🔁 RefreshToken-Anfrage erhalten");

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(user.getUsername());
                    log.info("✅ Neues AccessToken für {} generiert", user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(accessToken, requestRefreshToken));
                })
                .orElseThrow(() -> {
                    log.warn("❌ RefreshToken ungültig oder abgelaufen: {}", requestRefreshToken);
                    return new ResponseStatusException(
                            HttpStatus.FORBIDDEN, "Refresh Token ist ungültig oder abgelaufen");
                });
    }

    @Getter
    @Setter
    static class AuthRequest {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    static class AuthResponse {
        private String username;
        private String token;
        private String refreshToken;
        private String role;
    }
}