package com.blog.e2e.pages;

import com.blog.e2e.core.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private final WebDriver driver;

    private static final By EMAIL_INPUT =
            By.cssSelector("[data-testid='login-email-input']");

    private static final By PASSWORD_INPUT =
            By.cssSelector("[data-testid='login-password-input']");

    private static final By SUBMIT_BUTTON =
            By.cssSelector("[data-testid='login-submit-btn']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/login");
    }

    public void enterEmail(String email) {
        WaitUtils.waitForVisibility(driver, EMAIL_INPUT).sendKeys(email);
    }

    public void enterPassword(String password) {
        WaitUtils.waitForVisibility(driver, PASSWORD_INPUT).sendKeys(password);
    }

    public void clickLogin() {
        WaitUtils.waitForClickable(driver, SUBMIT_BUTTON).click();
    }
}