package com.blog.e2e.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@ActiveProfiles("test")
public abstract class BaseTest {

    protected static final String BASE_URL = "http://localhost:8080";
    protected WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = DriverManager.getDriver();
    }

    @AfterEach
    void tearDown() {
        DriverManager.quitDriver();
    }
}