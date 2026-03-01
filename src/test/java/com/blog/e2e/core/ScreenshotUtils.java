package com.blog.e2e.core;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtils {

    private ScreenshotUtils() {}

    public static void take(WebDriver driver, String stepName) {

        try {
            File src = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE);

            String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));

            Path screenshotsDir = Paths.get("target", "screenshots");
            Files.createDirectories(screenshotsDir);

            Path destination = screenshotsDir.resolve(
                stepName + "_" + timestamp + ".png"
            );

            Files.copy(
                src.toPath(),
                destination,
                StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {
            throw new RuntimeException("Screenshot failed", e);
        }
    }
}