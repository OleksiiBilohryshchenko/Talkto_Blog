package com.blog.comment.domain;

import com.blog.post.domain.Post;
import com.blog.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Comment cannot be empty")
  @Size(min = 2, max = 2000, message = "Comment must be between 2 and 2000 characters")
  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "author_id", nullable = false)
  private User author;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  public Comment() {
  }

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content != null ? content.trim() : null;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }
}