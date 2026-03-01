package com.blog.post.service;

import com.blog.post.domain.Post;
import com.blog.post.repository.PostRepository;
import com.blog.user.domain.User;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {

  private final PostRepository postRepository;

  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Transactional
  public Post create(String title, String content, User author) {

    if (author == null) {
      throw new IllegalArgumentException("Author cannot be null");
    }

    if (title == null || title.trim().isEmpty()) {
      throw new IllegalArgumentException("Title cannot be empty");
    }

    if (content == null || content.trim().isEmpty()) {
      throw new IllegalArgumentException("Content cannot be empty");
    }

    Post post = new Post(
        title.trim(),
        content.trim(),
        author
    );

    return postRepository.save(post);
  }

  /**
   * Backward-compatible method for tests.
   */
  public List<Post> findAll() {
    return postRepository.findAllWithAuthor();
  }

  /**
   * Production pagination method.
   */
  public Page<Post> findAll(int page, int size) {

    if (page < 0) {
      throw new IllegalArgumentException("Page index must not be negative");
    }

    if (size <= 0) {
      throw new IllegalArgumentException("Page size must be greater than zero");
    }

    PageRequest pageable = PageRequest.of(page, size);

    return postRepository.findAllByOrderByCreatedAtDesc(pageable);
  }

  public Post findById(Long id) {
    return postRepository.findByIdWithAuthor(id)
        .orElseThrow(() -> new NoSuchElementException("Post not found"));
  }

  public List<Post> findByAuthorId(Long authorId) {

    if (authorId == null) {
      throw new IllegalArgumentException("Author id cannot be null");
    }

    return postRepository.findByAuthorId(authorId);
  }
}