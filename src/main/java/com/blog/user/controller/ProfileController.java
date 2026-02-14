package com.blog.user.controller;

import com.blog.post.service.PostService;
import com.blog.user.domain.User;
import com.blog.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final PostService postService;

    public ProfileController(UserService userService,
                             PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping
    public String profile(Authentication authentication, Model model) {

        User user = userService.findByEmail(authentication.getName());

        model.addAttribute("user", user);
        model.addAttribute("posts",
                postService.findByAuthorId(user.getId()));

        return "profile/view";
    }

    @GetMapping("/edit")
    public String editForm(Authentication authentication, Model model) {

        User user = userService.findByEmail(authentication.getName());

        model.addAttribute("user", user);

        return "profile/edit";
    }

    @PostMapping("/edit")
    public String updateProfile(@RequestParam String name,
                                Authentication authentication) {

        User user = userService.findByEmail(authentication.getName());

        user.setName(name.trim());
        userService.save(user);

        return "redirect:/profile";
    }
}