package com.blog.e2e.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseTest {

    protected WebDriver driver;

    @Value("${local.server.port}")
    protected int port;

    protected String baseUrl;

    @RegisterExtension
    static TestFailureWatcher failureWatcher = new TestFailureWatcher();

    @BeforeEach
    void setUp() {
        driver = DriverManager.getDriver();
        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    void tearDown() {
        DriverManager.quitDriver();
    }
}