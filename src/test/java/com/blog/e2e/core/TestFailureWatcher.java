package com.blog.e2e.core;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class TestFailureWatcher implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {

        if (DriverManager.getDriver() != null) {

            String testName = context.getDisplayName()
                .replace("()", "")
                .replaceAll("[^a-zA-Z0-9]", "_");

            ScreenshotUtils.take(
                DriverManager.getDriver(),
                "FAILED_" + testName
            );
        }
    }
}