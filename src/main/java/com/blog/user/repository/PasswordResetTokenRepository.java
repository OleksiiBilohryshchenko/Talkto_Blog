package com.blog.user.repository;

import com.blog.user.domain.PasswordResetToken;
import com.blog.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken>
    findByTokenHashAndUsedFalseAndExpiresAtAfter(
            String tokenHash,
            LocalDateTime now
    );

    void deleteByUser(User user);

    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}