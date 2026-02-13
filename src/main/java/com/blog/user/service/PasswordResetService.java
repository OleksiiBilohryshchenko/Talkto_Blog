package com.blog.user.service;

import com.blog.user.domain.PasswordResetToken;
import com.blog.user.domain.User;
import com.blog.user.repository.PasswordResetTokenRepository;
import com.blog.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class PasswordResetService {

    private static final int TOKEN_TTL_MINUTES = 30;

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(PasswordResetTokenRepository tokenRepository,
                                UserRepository userRepository,
                                PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new password reset token.
     * All previous tokens for the user are removed to prevent reuse.
     * Returns raw token (only for email delivery).
     */
    @Transactional
    public String createResetToken(User user) {
        tokenRepository.deleteByUser(user);

        String rawToken = UUID.randomUUID().toString();
        String hash = sha256(rawToken);

        PasswordResetToken token = new PasswordResetToken(
                hash,
                user,
                LocalDateTime.now().plusMinutes(TOKEN_TTL_MINUTES)
        );

        tokenRepository.save(token);
        return rawToken;
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to hash reset token", ex);
        }
    }

    @Transactional(readOnly = true)
    public PasswordResetToken validateToken(String rawToken) {
        String hash = sha256(rawToken);

        return tokenRepository
                .findByTokenHashAndUsedFalseAndExpiresAtAfter(
                        hash,
                        LocalDateTime.now()
                )
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid or expired token"));
    }

    @Transactional
    public void resetPassword(String rawToken, String newPassword) {

        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Password must not be empty");
        }

        PasswordResetToken token = validateToken(rawToken);

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));

        token.markUsed();

        userRepository.save(user);
    }
}