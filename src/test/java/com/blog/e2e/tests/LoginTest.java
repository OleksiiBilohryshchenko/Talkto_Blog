package com.blog.e2e.tests;

import com.blog.e2e.core.BaseTest;
import com.blog.e2e.keywords.AuthKeywords;
import org.junit.jupiter.api.Test;

public class LoginTest extends BaseTest {

    private static final String BASE_URL = "http://localhost:8080";

    @Test
    void shouldLoginSuccessfully() {
        AuthKeywords auth = new AuthKeywords(driver, BASE_URL);
        auth.loginAs("register@gmail.com", "register12345");
    }
}