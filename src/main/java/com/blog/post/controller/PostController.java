package com.blog.post.controller;

import com.blog.comment.service.CommentService;
import com.blog.post.domain.Post;
import com.blog.post.service.PostService;
import com.blog.user.domain.User;
import com.blog.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;
    private final CommentService commentService;

    public PostController(PostService postService, UserRepository userRepository, CommentService commentService) {
        this.postService = postService;
        this.userRepository = userRepository;
        this.commentService = commentService;
    }

    @GetMapping
    public String list(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "posts/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        Post post = postService.findById(id);

        model.addAttribute("post", post);
        model.addAttribute("comments",
                commentService.getCommentsForPost(post));

        return "posts/view";
    }

    @GetMapping("/new")
    public String newPostForm() {
        return "posts/new";
    }

    @PostMapping
    public String create(@RequestParam String title,
                         @RequestParam String content,
                         Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        postService.create(title, content, user);

        return "redirect:/posts";
    }
}