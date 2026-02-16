package com.blog.user.service;

import com.blog.user.domain.PasswordResetToken;
import com.blog.user.domain.User;
import com.blog.user.repository.PasswordResetTokenRepository;
import com.blog.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    @Mock
    PasswordResetTokenRepository tokenRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    PasswordResetService service;

    @Test
    void createResetToken_success() {
        User user = new User("John", "mail", "pass");

        when(tokenRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        String rawToken = service.createResetToken(user);

        assertThat(rawToken).isNotBlank();

        verify(tokenRepository).deleteByUser(user);
        verify(tokenRepository).save(any(PasswordResetToken.class));
    }

    @Test
    void validateToken_success() {
        User user = new User("John", "mail", "pass");
        PasswordResetToken token = mock(PasswordResetToken.class);

        when(tokenRepository
                .findByTokenHashAndUsedFalseAndExpiresAtAfter(
                        anyString(),
                        any(LocalDateTime.class)))
                .thenReturn(Optional.of(token));

        PasswordResetToken result = service.validateToken("raw");

        assertThat(result).isEqualTo(token);
    }

    @Test
    void validateToken_invalid() {
        when(tokenRepository
                .findByTokenHashAndUsedFalseAndExpiresAtAfter(
                        anyString(),
                        any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.validateToken("raw"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void resetPassword_success() {
        User user = new User("John", "mail", "old");
        PasswordResetToken token = mock(PasswordResetToken.class);

        when(token.getUser()).thenReturn(user);
        when(tokenRepository
                .findByTokenHashAndUsedFalseAndExpiresAtAfter(
                        anyString(),
                        any(LocalDateTime.class)))
                .thenReturn(Optional.of(token));

        when(passwordEncoder.encode("new")).thenReturn("encoded");

        service.resetPassword("raw", "new");

        assertThat(user.getPassword()).isEqualTo("encoded");

        verify(token).markUsed();
        verify(userRepository).save(user);
    }

    @Test
    void resetPassword_emptyPassword() {
        assertThatThrownBy(() ->
                service.resetPassword("raw", " "))
                .isInstanceOf(IllegalArgumentException.class);
    }
}