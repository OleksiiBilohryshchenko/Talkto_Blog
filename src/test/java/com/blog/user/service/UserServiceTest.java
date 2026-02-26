package com.blog.user.service;

import com.blog.user.domain.Role;
import com.blog.user.domain.User;
import com.blog.user.repository.RoleRepository;
import com.blog.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    UserService userService;

    @Test
    void registerUser_success() {
        Role role = mock(Role.class);

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);
        when(passwordEncoder.encode("raw")).thenReturn("encoded");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User user = userService.registerUser("John", "test@mail.com", "raw");

        assertThat(user.getEmail()).isEqualTo("test@mail.com");
        assertThat(user.getPassword()).isEqualTo("encoded");
        assertThat(user.getRoles()).contains(role);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_emailAlreadyExists() {
        when(userRepository.existsByEmail("test@mail.com")).thenReturn(true);

        assertThatThrownBy(() ->
            userService.registerUser("John", "test@mail.com", "raw"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Email already exists");

        verify(userRepository, never()).save(any());
        verify(roleRepository, never()).findByName(any());
    }

    @Test
    void findByEmail_success() {
        User user = new User("John", "mail", "pass");
        when(userRepository.findByEmail("mail")).thenReturn(Optional.of(user));

        User result = userService.findByEmail("mail");

        assertThat(result).isEqualTo(user);
    }

    @Test
    void findByEmail_notFound() {
        when(userRepository.findByEmail("mail")).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            userService.findByEmail("mail"))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void existsByEmail_passthrough() {
        when(userRepository.existsByEmail("mail")).thenReturn(true);

        assertThat(userService.existsByEmail("mail")).isTrue();
    }
}