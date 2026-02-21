package com.blog.e2e.cucumber.steps;

import com.blog.e2e.core.DriverManager;
import com.blog.e2e.keywords.AuthKeywords;
import com.blog.e2e.keywords.PostKeywords;
import com.blog.e2e.keywords.RegisterKeywords;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

public class PostSteps {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private String baseUrl;

    private RegisterKeywords register;
    private AuthKeywords auth;
    private PostKeywords post;

    private String email;
    private String title;

    @Before
    public void setUp() {
        driver = DriverManager.getDriver();
        baseUrl = "http://localhost:" + port;

        register = new RegisterKeywords(driver, baseUrl);
        auth = new AuthKeywords(driver, baseUrl);
        post = new PostKeywords(driver, baseUrl);
    }

    @Given("user is registered and logged in")
    public void userIsRegisteredAndLoggedIn() {
        email = register.registerNewUser("BDD Post User", "password123");
        auth.loginAs(email, "password123");
    }

    @When("user creates a new post")
    public void userCreatesNewPost() {
        title = "BDD Post " + UUID.randomUUID();
        post.createPost(title, "BDD generated content");
    }

    @Then("new post should be visible in posts list")
    public void newPostShouldBeVisibleInPostsList() {
        Assertions.assertTrue(
                post.isPostVisible(title),
                "Created post should be visible"
        );
    }
}