package com.blog.e2e.cucumber.steps;

import com.blog.e2e.core.DriverManager;
import com.blog.e2e.keywords.AuthKeywords;
import com.blog.e2e.keywords.RegisterKeywords;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.stereotype.Component;

public class LoginSteps {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private String baseUrl;

    private RegisterKeywords register;
    private AuthKeywords auth;

    private String email;

    @Before
    public void setUp() {
        driver = DriverManager.getDriver();
        baseUrl = "http://localhost:" + port;
        register = new RegisterKeywords(driver, baseUrl);
        auth = new AuthKeywords(driver, baseUrl);
    }

    @Given("user is registered")
    public void userIsRegistered() {
        email = register.registerNewUser("Cucumber User", "password123");
    }

    @Given("user is on login page")
    public void userIsOnLoginPage() {
        driver.get(baseUrl + "/login");
    }

    @When("user enters valid credentials")
    public void userEntersValidCredentials() {
        auth.loginAs(email, "password123");
    }

    @When("user submits login form")
    public void userSubmitsLoginForm() {
    }

    @Then("user should be redirected to posts page")
    public void userShouldBeRedirectedToPostsPage() {
        Assertions.assertTrue(
            driver.getCurrentUrl().contains("/posts")
        );
    }
}