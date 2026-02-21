package com.blog.e2e.keywords;

import com.blog.e2e.core.ScreenshotUtils;
import com.blog.e2e.core.WaitUtils;
import com.blog.e2e.pages.RegisterPage;
import org.openqa.selenium.WebDriver;

import java.util.UUID;

public class RegisterKeywords {

    private final WebDriver driver;
    private final RegisterPage registerPage;
    private final String baseUrl;

    public RegisterKeywords(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        this.registerPage = new RegisterPage(driver);
    }

    public String registerNewUser(String name, String password) {

        String uniqueEmail = "user_" + UUID.randomUUID() + "@test.com";

        registerPage.open(baseUrl);
        registerPage.enterName(name);
        registerPage.enterEmail(uniqueEmail);
        registerPage.enterPassword(password);
        registerPage.submit();

        WaitUtils.waitForUrlContains(driver, "/login");

        ScreenshotUtils.take(driver, "register_success");

        return uniqueEmail;
    }
}