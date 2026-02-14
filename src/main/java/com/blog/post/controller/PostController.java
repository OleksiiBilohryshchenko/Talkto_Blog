package com.blog.post.controller;

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

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final UserRepository userRepository;

    public PostController(PostService postService,
                          CommentService commentService,
                          UserRepository userRepository) {
        this.postService = postService;
        this.commentService = commentService;
        this.userRepository = userRepository;
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
        model.addAttribute("comments", commentService.getCommentsForPost(id));
        model.addAttribute("comment", new com.blog.comment.domain.Comment());

        return "posts/view";
    }

    @GetMapping("/new")
    public String newPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "posts/new";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("post") Post post,
                         BindingResult bindingResult,
                         Authentication authentication,
                         Model model) {

        if (bindingResult.hasErrors()) {
            return "posts/new";
        }

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        postService.create(post.getTitle(), post.getContent(), user);

        return "redirect:/posts";
    }
}