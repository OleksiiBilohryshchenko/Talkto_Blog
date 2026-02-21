package com.blog.e2e.core;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtils {

    private ScreenshotUtils() {}

    public static void take(WebDriver driver, String stepName) {

        try {
            File src = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            File destination = new File(
                    "target/screenshots/" + stepName + "_" + timestamp + ".png"
            );

            destination.getParentFile().mkdirs();
            Files.copy(src.toPath(), destination.toPath());

        } catch (IOException e) {
            throw new RuntimeException("Screenshot failed", e);
        }
    }
}