package com.blog.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens",
    indexes = {
        @Index(name = "idx_password_reset_token_hash", columnList = "token_hash"),
        @Index(name = "idx_password_reset_user_id", columnList = "user_id")
    })
public class PasswordResetToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "token_hash", nullable = false, length = 64)
  private String tokenHash;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;

  @Column(nullable = false)
  private boolean used;

  protected PasswordResetToken() {
  }

  public PasswordResetToken(String tokenHash, User user, LocalDateTime expiresAt) {
    this.tokenHash = tokenHash;
    this.user = user;
    this.expiresAt = expiresAt;
    this.used = false;
  }

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public String getTokenHash() {
    return tokenHash;
  }

  public User getUser() {
    return user;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }

  public boolean isUsed() {
    return used;
  }

  public void markUsed() {
    this.used = true;
  }
}