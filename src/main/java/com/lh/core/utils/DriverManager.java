package com.lh.core.utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

import java.nio.file.Paths;

public class DriverManager {

    private static Browser browser;
    private static Playwright playwright;

    public static void setUp(String browserName) {
        String edgePath = "C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe";

        String headless = System.getProperty("headless", "false");
        boolean enableHeadless = Boolean.parseBoolean(headless);

        playwright = Playwright.create();


        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(enableHeadless)
                .setTimeout(60000);

        switch (browserName.toLowerCase()) {
            case "edge":
                options.setExecutablePath(Paths.get(edgePath));
                browser = playwright.chromium().launch(options);
                break;

            case "firefox":
                browser = playwright.firefox().launch(options);
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }
    }

    public static void tearDown() {
        if (browser != null) {
            browser.contexts().clear();
            browser.close();
            browser = null;
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    public static Browser getBrowser() {
        return browser;
    }

}
