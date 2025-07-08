package com.lh.core.utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

import java.nio.file.Paths;

public class DriverManager {

	private static Browser browser;

    public static void setUp() {
    	String headless = System.getProperty("headless", "false");
    	Boolean enableHeadless = false;
    	if(headless.equals("true")) {
    		enableHeadless = true;
    	}

        String edgePath = "C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe";

    	Playwright playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setExecutablePath(Paths.get(edgePath))
                        .setHeadless(enableHeadless)
                        .setTimeout(60000)
        );
    }

    public static void tearDown() {
        if (browser != null) {
            browser.contexts().clear();
            browser.close();
            browser = null;
        }
    }

    public static Browser getBrowser() {
        return browser;
    }

}
