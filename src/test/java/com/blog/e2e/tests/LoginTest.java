package com.blog.e2e.tests;

import com.blog.e2e.core.BaseTest;
import com.blog.e2e.keywords.AuthKeywords;
import com.blog.e2e.keywords.RegisterKeywords;
import org.junit.jupiter.api.Test;

public class LoginTest extends BaseTest {

    @Test
    void shouldLoginSuccessfully() {
        RegisterKeywords register = new RegisterKeywords(driver, baseUrl);
        AuthKeywords auth = new AuthKeywords(driver, baseUrl);

        String email = register.registerNewUser("Login User", "password123");
        auth.loginAs(email, "password123");
    }
}