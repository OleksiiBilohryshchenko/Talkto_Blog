package com.blog.e2e.pages;

import com.blog.e2e.core.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage {

    private final WebDriver driver;

    private static final By NAME_INPUT =
            By.cssSelector("[data-testid='register-name-input']");

    private static final By EMAIL_INPUT =
            By.cssSelector("[data-testid='register-email-input']");

    private static final By PASSWORD_INPUT =
            By.cssSelector("[data-testid='register-password-input']");

    private static final By SUBMIT_BUTTON =
            By.cssSelector("[data-testid='register-submit-btn']");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/register");
    }

    public void enterName(String name) {
        WaitUtils.waitForVisibility(driver, NAME_INPUT).sendKeys(name);
    }

    public void enterEmail(String email) {
        WaitUtils.waitForVisibility(driver, EMAIL_INPUT).sendKeys(email);
    }

    public void enterPassword(String password) {
        WaitUtils.waitForVisibility(driver, PASSWORD_INPUT).sendKeys(password);
    }

    public void submit() {
        WaitUtils.waitForClickable(driver, SUBMIT_BUTTON).click();
    }
}