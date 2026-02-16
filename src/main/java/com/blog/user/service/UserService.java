package com.blog.user.service;

import com.blog.user.domain.User;
import com.blog.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User registerUser(String name, String email, String rawPassword) {

    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("Email already exists");
    }

    String encodedPassword = passwordEncoder.encode(rawPassword);

    User user = new User(name, email, encodedPassword);

    return userRepository.save(user);
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found"));
  }

  public User save(User user) {
    return userRepository.save(user);
  }

  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }
}