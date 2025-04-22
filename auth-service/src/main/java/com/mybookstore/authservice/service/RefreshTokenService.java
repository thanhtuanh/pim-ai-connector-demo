package com.mybookstore.authservice.service;

import com.mybookstore.authservice.model.RefreshToken;
import com.mybookstore.authservice.model.UserEntity;
import com.mybookstore.authservice.repository.RefreshTokenRepository;
import com.mybookstore.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final Logger log = LoggerFactory.getLogger(RefreshTokenService.class);

    @Value("${mybookstore.app.refreshTokenDurationMs:86400000}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        log.debug("🔎 Suche nach RefreshToken mit Token: {}", token);
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Benutzer nicht gefunden"));

        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);

        Instant expiry = Instant.now().plusMillis(refreshTokenDurationMs);
        refreshToken.setExpiryDate(expiry); // <-- Das wird eventuell nicht korrekt übernommen

        String token = UUID.randomUUID().toString();
        refreshToken.setToken(token);

        log.info("🧪 Creating refresh token for user '{}'", user.getUsername());
        log.info("📅 Expiry: {}", expiry);
        log.info("🔐 Token: {}", token);

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        log.debug("⏰ Überprüfe Ablaufzeit des RefreshTokens");
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            log.warn("⚠️ RefreshToken abgelaufen: {}", token.getToken());
            refreshTokenRepository.delete(token);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Refresh token ist abgelaufen. Bitte melden Sie sich erneut an.");
        }

        log.debug("✅ RefreshToken ist noch gültig: {}", token.getToken());
        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        log.info("🧹 Lösche alle RefreshTokens für Benutzer-ID: {}", userId);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("❌ Benutzer mit ID {} nicht gefunden", userId);
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Benutzer nicht gefunden");
                });

        int deleted = refreshTokenRepository.deleteByUser(user);
        log.debug("🗑️ Gelöschte RefreshTokens: {}", deleted);
        return deleted;
    }
}
