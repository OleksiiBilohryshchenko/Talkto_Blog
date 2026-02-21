package com.blog.e2e.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

public abstract class BaseTest {

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