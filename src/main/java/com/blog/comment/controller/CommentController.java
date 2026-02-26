package com.blog.comment.controller;

import com.blog.comment.domain.Comment;
import com.blog.comment.service.CommentService;
import com.blog.post.domain.Post;
import com.blog.post.service.PostService;
import com.blog.user.domain.User;
import com.blog.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

  private final CommentService commentService;
  private final PostService postService;
  private final UserRepository userRepository;

  public CommentController(CommentService commentService,
      PostService postService,
      UserRepository userRepository) {
    this.commentService = commentService;
    this.postService = postService;
    this.userRepository = userRepository;
  }

  @PostMapping
  public String addComment(@PathVariable Long postId,
      @Valid @ModelAttribute("comment") Comment comment,
      BindingResult bindingResult,
      Authentication authentication,
      Model model) {

    Post post = postService.findById(postId);

    if (bindingResult.hasErrors()) {
      model.addAttribute("post", post);
      model.addAttribute("comments",
          commentService.getCommentsForPost(postId));
      model.addAttribute("comment", comment); // важливо
      return "posts/view";
    }

    User user = userRepository.findByEmail(authentication.getName())
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    commentService.addComment(comment.getContent(), user, post);

    return "redirect:/posts/" + postId;
  }
}