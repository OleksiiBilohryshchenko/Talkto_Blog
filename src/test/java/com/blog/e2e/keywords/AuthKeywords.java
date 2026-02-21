package com.blog.e2e.keywords;

import com.blog.e2e.core.WaitUtils;
import com.blog.e2e.pages.LoginPage;
import org.openqa.selenium.WebDriver;

public class AuthKeywords {

    private final WebDriver driver;
    private final LoginPage loginPage;
    private final String baseUrl;

    public AuthKeywords(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        this.loginPage = new LoginPage(driver);
    }

    public void loginAs(String email, String password) {
        loginPage.open(baseUrl);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        WaitUtils.waitForUrlContains(driver, "/posts");
    }
}