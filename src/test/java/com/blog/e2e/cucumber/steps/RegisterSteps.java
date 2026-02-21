package com.blog.e2e.cucumber.steps;

import com.blog.e2e.core.DriverManager;
import com.blog.e2e.keywords.RegisterKeywords;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.web.server.LocalServerPort;

public class RegisterSteps {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private String baseUrl;
    private RegisterKeywords register;

    @Before
    public void setUp() {
        driver = DriverManager.getDriver();
        baseUrl = "http://localhost:" + port;
        register = new RegisterKeywords(driver, baseUrl);
    }

    @Given("user is on register page")
    public void userIsOnRegisterPage() {
        driver.get(baseUrl + "/register");
    }

    @When("user fills valid registration form")
    public void userFillsValidRegistrationForm() {
        register.registerNewUser("BDD User", "password123");
    }

    @Then("user should be redirected to login page")
    public void userShouldBeRedirectedToLoginPage() {
        Assertions.assertTrue(
                driver.getCurrentUrl().contains("/login")
        );
    }
}