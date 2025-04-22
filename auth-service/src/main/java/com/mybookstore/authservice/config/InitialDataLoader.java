package com.mybookstore.authservice.config;

import com.mybookstore.authservice.model.UserEntity;
import com.mybookstore.authservice.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

/**
 * Initialisiert Standarddaten wie einen Admin-Benutzer beim Start der
 * Anwendung.
 * Erstellt einen Admin-Benutzer, falls noch keiner in der Datenbank existiert.
 */
@Component
public class InitialDataLoader implements ApplicationRunner {

    private static final Logger logger = Logger.getLogger(InitialDataLoader.class.getName());

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Konstruktor mit Dependency Injection.
     * 
     * @param userRepository  Repository für den Zugriff auf User-Entitäten
     * @param passwordEncoder Service zum Verschlüsseln von Passwörtern
     */
    public InitialDataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Wird beim Start der Anwendung ausgeführt.
     * Erstellt einen Admin-Benutzer, falls noch keiner existiert.
     * 
     * @param args Startargumente der Anwendung
     */
    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        // Überprüfen, ob bereits ein Admin-Benutzer existiert
        if (userRepository.findByUsername("test").isEmpty()) {
            // Admin-Benutzer erstellen
            UserEntity admin = new UserEntity();
            admin.setUsername("test");
            admin.setPassword(passwordEncoder.encode("test")); // Passwort test wird verschlüsselt
            admin.setRole("ROLE_ADMIN");

            // In der Datenbank speichern
            userRepository.save(admin);

            logger.info("🔧 Admin-Benutzer wurde erstellt (Username: test, Passwort: test)");
        } else {
            logger.info("✅ Admin-Benutzer test existiert bereits");
        }
    }
}