package com.blog.e2e.pages;

import com.blog.e2e.core.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PostPage {

    private final WebDriver driver;

    private static final By TITLE_INPUT =
            By.cssSelector("[data-testid='post-title-input']");

    private static final By CONTENT_TEXTAREA =
            By.cssSelector("[data-testid='post-content-textarea']");

    private static final By SUBMIT_BUTTON =
            By.cssSelector("[data-testid='post-submit-btn']");

    private static final By POST_TITLE_LINK =
            By.cssSelector("[data-testid='post-title-link']");

    public PostPage(WebDriver driver) {
        this.driver = driver;
    }

    public void openCreateForm(String baseUrl) {
        driver.get(baseUrl + "/posts/new");
    }

    public void enterTitle(String title) {
        WaitUtils.waitForVisibility(driver, TITLE_INPUT).sendKeys(title);
    }

    public void enterContent(String content) {
        WaitUtils.waitForVisibility(driver, CONTENT_TEXTAREA).sendKeys(content);
    }

    public void submit() {
        WaitUtils.waitForClickable(driver, SUBMIT_BUTTON).click();
    }

    public boolean isPostVisible(String title) {
        WaitUtils.waitForVisibility(driver, POST_TITLE_LINK);
        return driver.getPageSource().contains(title);
    }
}