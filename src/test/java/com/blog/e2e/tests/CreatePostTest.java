package com.blog.e2e.tests;

import com.blog.e2e.core.BaseTest;
import com.blog.e2e.keywords.AuthKeywords;
import com.blog.e2e.keywords.PostKeywords;
import com.blog.e2e.keywords.RegisterKeywords;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class CreatePostTest extends BaseTest {

    private static final String BASE_URL = "http://localhost:8080";

    @Test
    void shouldCreatePostSuccessfully() {

        RegisterKeywords register = new RegisterKeywords(driver, BASE_URL);
        AuthKeywords auth = new AuthKeywords(driver, BASE_URL);
        PostKeywords post = new PostKeywords(driver, BASE_URL);

        String email = register.registerNewUser("Post User", "password123");
        auth.loginAs(email, "password123");

        String uniqueTitle = "Test Post " + UUID.randomUUID();

        post.createPost(uniqueTitle, "This is automated content.");

        Assertions.assertTrue(
                post.isPostVisible(uniqueTitle),
                "Created post should be visible in posts list"
        );
    }
}