package com.blog.e2e.tests;

import com.blog.e2e.core.BaseTest;
import com.blog.e2e.keywords.AuthKeywords;
import com.blog.e2e.keywords.RegisterKeywords;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RegisterTest extends BaseTest {

    @Test
    void shouldRegisterAndLoginSuccessfully() {

        RegisterKeywords register = new RegisterKeywords(driver, BASE_URL);
        AuthKeywords auth = new AuthKeywords(driver, BASE_URL);

        String email = register.registerNewUser("Test User", "password123");

        auth.loginAs(email, "password123");

        Assertions.assertTrue(
                driver.getCurrentUrl().contains("/posts"),
                "User should be redirected to posts after login"
        );
    }
}