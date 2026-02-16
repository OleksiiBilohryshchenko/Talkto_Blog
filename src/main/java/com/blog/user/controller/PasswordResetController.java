package com.blog.user.controller;

import com.blog.mail.MailService;
import com.blog.user.repository.UserRepository;
import com.blog.user.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/password")
public class PasswordResetController {

  private final MailService mailService;

  @Value("${app.base-url}")
  private String baseUrl;

  private final UserRepository userRepository;
  private final PasswordResetService passwordResetService;

  public PasswordResetController(MailService mailService, UserRepository userRepository,
      PasswordResetService passwordResetService) {
    this.mailService = mailService;
    this.userRepository = userRepository;
    this.passwordResetService = passwordResetService;
  }

  /**
   * Displays password reset request form.
   */
  @GetMapping("/forgot")
  public String showForgotPasswordForm() {
    return "password/forgot";
  }

  /**
   * Handles password reset request submission. Always returns success view to prevent account
   * enumeration.
   */
  @PostMapping("/forgot")
  public String handleForgotPassword(@RequestParam("email") String email) {

    if (email != null && !email.isBlank()) {
      userRepository.findByEmail(email.trim())
          .ifPresent(user -> {
            String rawToken = passwordResetService.createResetToken(user);

            String normalizedBaseUrl = baseUrl.endsWith("/")
                ? baseUrl.substring(0, baseUrl.length() - 1)
                : baseUrl;

            String resetLink = normalizedBaseUrl + "/password/reset?token=" + rawToken;

            mailService.sendPasswordResetEmail(user.getEmail(), resetLink);
          });
    }

    return "password/success";
  }

  /**
   * Displays reset form if token is valid.
   */
  @GetMapping("/reset")
  public String showResetForm(@RequestParam("token") String token,
      Model model) {

    try {
      passwordResetService.validateToken(token);
      model.addAttribute("token", token);
      return "password/reset";

    } catch (IllegalArgumentException ex) {
      model.addAttribute("message", "Invalid or expired token.");
      return "password/error";
    }
  }

  /**
   * Handles new password submission.
   */
  @PostMapping("/reset")
  public String handlePasswordReset(@RequestParam("token") String token,
      @RequestParam("password") String password,
      Model model) {

    if (password == null || password.trim().length() < 8) {
      model.addAttribute("token", token);
      model.addAttribute("error", "Password must be at least 8 characters.");
      return "password/reset";
    }

    try {
      passwordResetService.resetPassword(token, password.trim());
      return "redirect:/login?resetSuccess";

    } catch (IllegalArgumentException ex) {
      model.addAttribute("message", "Invalid or expired token.");
      return "password/error";
    }
  }
}