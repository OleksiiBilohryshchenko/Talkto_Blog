package com.blog.user.controller;

import com.blog.post.service.PostService;
import com.blog.user.domain.User;
import com.blog.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
@AutoConfigureMockMvc
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PostService postService;

    @Test
    @WithMockUser(username = "mail")
    void profile_shouldReturnProfileView() throws Exception {

        User user = new User("John", "mail", "password");

        when(userService.findByEmail("mail")).thenReturn(user);
        when(postService.findByAuthorId(anyLong()))
            .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/profile"))
            .andExpect(status().isOk())
            .andExpect(view().name("profile/view"))
            .andExpect(model().attributeExists("user"))
            .andExpect(model().attributeExists("posts"));
    }

    @Test
    @WithMockUser(username = "mail")
    void editForm_shouldReturnEditView() throws Exception {

        User user = new User("John", "mail", "password");

        when(userService.findByEmail("mail")).thenReturn(user);

        mockMvc.perform(get("/profile/edit"))
            .andExpect(status().isOk())
            .andExpect(view().name("profile/edit"))
            .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser(username = "mail")
    void updateProfile_shouldRedirectAndUpdateName() throws Exception {

        User user = new User("John", "mail", "password");

        when(userService.findByEmail("mail")).thenReturn(user);

        mockMvc.perform(post("/profile/edit")
                .with(csrf())
                .param("name", "NewName"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/profile"));

        verify(userService).save(any(User.class));
    }
}