package com.blog.user.repository;

import com.blog.user.domain.PasswordResetToken;
import com.blog.user.domain.User;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing password reset tokens.
 */
public interface PasswordResetTokenRepository
    extends JpaRepository<PasswordResetToken, Long> {

  /**
   * Finds active (not used and not expired) token by hash.
   */
      Optional<PasswordResetToken> findByTokenHashAndUsedFalseAndExpiresAtAfter(
        String tokenHash,
        LocalDateTime now
  );

      void deleteByUser(User user);

      void deleteByExpiresAtBefore(LocalDateTime dateTime);
}