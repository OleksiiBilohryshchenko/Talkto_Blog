package com.blog.user.controller;

import com.blog.mail.MailService;
import com.blog.user.domain.PasswordResetToken;
import com.blog.user.domain.User;
import com.blog.user.repository.UserRepository;
import com.blog.user.service.PasswordResetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PasswordResetController.class)
@AutoConfigureMockMvc(addFilters = false)
class PasswordResetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MailService mailService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordResetService passwordResetService;

    @Test
    void showForgotPasswordForm_shouldReturnView() throws Exception {
        mockMvc.perform(get("/password/forgot"))
            .andExpect(status().isOk())
            .andExpect(view().name("password/forgot"));
    }

    @Test
    void handleForgotPassword_existingUser_shouldSendEmail() throws Exception {

        User user = new User("John", "test@mail.com", "pass");

        when(userRepository.findByEmail("test@mail.com"))
            .thenReturn(Optional.of(user));

        when(passwordResetService.createResetToken(user))
            .thenReturn("raw-token");

        mockMvc.perform(post("/password/forgot")
                .param("email", "test@mail.com"))
            .andExpect(status().isOk())
            .andExpect(view().name("password/success"));

        verify(mailService).sendPasswordResetEmail(
            eq("test@mail.com"),
            contains("raw-token")
        );
    }

    @Test
    void showResetForm_validToken_shouldReturnResetView() throws Exception {

        PasswordResetToken token =
            new PasswordResetToken(
                "hash",
                new User("John", "mail", "pass"),
                LocalDateTime.now().plusMinutes(10)
            );

        when(passwordResetService.validateToken("valid"))
            .thenReturn(token);

        mockMvc.perform(get("/password/reset")
                .param("token", "valid"))
            .andExpect(status().isOk())
            .andExpect(view().name("password/reset"))
            .andExpect(model().attributeExists("token"));
    }

    @Test
    void showResetForm_invalidToken_shouldReturnErrorView() throws Exception {

        when(passwordResetService.validateToken("invalid"))
            .thenThrow(new IllegalArgumentException());

        mockMvc.perform(get("/password/reset")
                .param("token", "invalid"))
            .andExpect(status().isOk())
            .andExpect(view().name("password/error"))
            .andExpect(model().attributeExists("message"));
    }

    @Test
    void handlePasswordReset_shortPassword_shouldReturnResetView() throws Exception {

        mockMvc.perform(post("/password/reset")
                .param("token", "token")
                .param("password", "123"))
            .andExpect(status().isOk())
            .andExpect(view().name("password/reset"))
            .andExpect(model().attributeExists("error"));
    }

    @Test
    void handlePasswordReset_valid_shouldRedirect() throws Exception {

        doNothing().when(passwordResetService)
            .resetPassword("token", "password123");

        mockMvc.perform(post("/password/reset")
                .param("token", "token")
                .param("password", "password123"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login?resetSuccess"));
    }
}