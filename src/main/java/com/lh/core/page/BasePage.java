package com.lh.core.page;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.lh.core.utils.DriverManager;
import com.lh.core.utils.ErrorCodeReader;
import com.microsoft.playwright.*;
import cucumber.api.Scenario;
import org.apache.log4j.LogManager;
import org.junit.Assert;

import java.io.IOException;

public class BasePage {
	protected Browser browser;
	protected BrowserContext context;
	public static Page page;
	public static Scenario sc;
	public static final org.apache.log4j.Logger log = LogManager.getLogger(BasePage.class);
	public static boolean isScreenshotCapturedAfterFailure = false;
	public ExtentTest s;

	public BasePage() {
		this.browser = DriverManager.getBrowser();
		this.context = browser != null ? browser.newContext() : null;
	}

	public void setScenario(Scenario scenario) {
		sc = scenario;
	}

	public void waitAndClick(String object) {
		try {
			page.isVisible(object);
			page.click(object);
		} catch (Exception e) {
			throw e;
		}
	}

	public void enterText(String object, String data) {

		try {
			if (page.locator(object).isVisible() == true) {
				page.locator(object).fill(data);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean waitForElement(String locator, int timeoutMilliseconds) {
		boolean status = false;
		try {
			page.waitForLoadState();
			page.waitForSelector(locator, new Page.WaitForSelectorOptions().setTimeout(timeoutMilliseconds));
			status = page.locator(locator).isVisible();
		} catch (Exception e) {
			throw e;
		}
		return status;
	}

	public static void takeScreenshot() {
		byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
		String testname = sc.getName();
		sc.embed(screenshot, "image/png");
		sc.write(testname);
	}

	public static void logAssert_Fail(String errMsg) throws InterruptedException {

		log.error(errMsg);
		takeScreenshot();
		isScreenshotCapturedAfterFailure = true;
		sc.write(errMsg);
		Assert.fail(errMsg);
	}

	public void launchBrowser(String application) throws IOException, InterruptedException {
		page = browser.newPage();
		String url = null;
		url = application;

		if (url != null) {
			try {
				page.navigate(url);  // Navigate to the determined URL
				takeScreenshot();
			} catch (Exception e) {
				log.error(url + e.getMessage());// Log an error if navigation fails
			}
		}
		else {
			log.error(ErrorCodeReader.getErrorCode("ET1001"));   // Log a message if the URL is null
		}

	}

	public static void clickAndSwitchToNewTab(String buttonSelector) {
		// Click the button that opens the new tab
		page.locator(buttonSelector).click();
		// Wait for the new tab to open (only once)
		Page newTab = page.context().waitForPage(() -> {
		});
		// Wait for the new tab to load
		newTab.waitForLoadState();
		// Return the new tab Page object
		page = newTab; // Set the current page to the new tab
	}
}
// Common methods that can be used across different pages
// ...