package com.blog.e2e.keywords;

import com.blog.e2e.core.ScreenshotUtils;
import com.blog.e2e.core.WaitUtils;
import com.blog.e2e.pages.PostPage;
import org.openqa.selenium.WebDriver;

public class PostKeywords {

    private final WebDriver driver;
    private final PostPage postPage;
    private final String baseUrl;

    public PostKeywords(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        this.postPage = new PostPage(driver);
    }

    public void createPost(String title, String content) {

        postPage.openCreateForm(baseUrl);
        postPage.enterTitle(title);
        postPage.enterContent(content);
        postPage.submit();

        WaitUtils.waitForUrlContains(driver, "/posts");

        ScreenshotUtils.take(driver, "create_post_success");
    }

    public boolean isPostVisible(String title) {
        return postPage.isPostVisible(title);
    }
}