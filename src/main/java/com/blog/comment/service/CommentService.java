package com.blog.comment.service;

import com.blog.comment.domain.Comment;
import com.blog.comment.repository.CommentRepository;
import com.blog.post.domain.Post;
import com.blog.user.domain.User;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CommentService {

  private final CommentRepository commentRepository;

  public CommentService(CommentRepository commentRepository) {
    this.commentRepository = commentRepository;
  }

  @Transactional
  public Comment addComment(String content, User author, Post post) {

    if (content == null || content.trim().isEmpty()) {
      throw new IllegalArgumentException("Comment cannot be empty");
    }

    Comment comment = new Comment();
    comment.setContent(content.trim());
    comment.setAuthor(author);
    comment.setPost(post);

    return commentRepository.save(comment);
  }

  public List<Comment> getCommentsForPost(Long postId) {
    return commentRepository.findByPostIdWithAuthor(postId);
  }
}