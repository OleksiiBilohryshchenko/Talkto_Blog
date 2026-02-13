package com.blog.comment.controller;

import com.blog.comment.service.CommentService;
import com.blog.post.domain.Post;
import com.blog.post.service.PostService;
import com.blog.user.domain.User;
import com.blog.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
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
                             @RequestParam String content,
                             Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postService.findById(postId);

        commentService.addComment(content, user, post);

        return "redirect:/posts/" + postId;
    }
}