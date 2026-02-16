package com.blog.post.service;

import com.blog.post.domain.Post;
import com.blog.post.repository.PostRepository;
import com.blog.user.domain.User;
import java.util.List;
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

  public List<Post> findAll() {
    return postRepository.findAllWithAuthor();
  }

  public Post findById(Long id) {
    return postRepository.findByIdWithAuthor(id)
        .orElseThrow(() -> new IllegalArgumentException("Post not found"));
  }

  public List<Post> findByAuthorId(Long authorId) {

    if (authorId == null) {
      throw new IllegalArgumentException("Author id cannot be null");
    }

    return postRepository.findByAuthorId(authorId);
  }
}