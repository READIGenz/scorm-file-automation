package com.lh.core.page;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.lh.core.utils.AssertionMessages;
import com.lh.core.utils.DriverManager;
import com.lh.core.utils.ErrorCodeReader;
import com.microsoft.playwright.*;
import cucumber.api.Scenario;
import org.apache.log4j.LogManager;
import org.junit.Assert;

import java.io.IOException;
import java.util.Properties;

public class BasePage {
	protected Browser browser;
	Playwright playwright;
	protected BrowserContext context;
	public static Page page;
	public static Page lh_Page;
	public static Page lx_Page;
	public static Page sn_Page;
	public static Page os_Page;
	public static Scenario sc;
	public static final org.apache.log4j.Logger log = LogManager.getLogger(BasePage.class);
	public static boolean isScreenshotCapturedAfterFailure = false;
	Properties properties;
	private static ExtentReports extent;
	public static String screenshotFolderPath;
	public ExtentTest s;

	private static final String LOAD_STATE = "LOAD";
    private static final String PRIVACY_BUTTON_XPATH = "//button[@id='cm-acceptAll']";
    private static final String REPORT_HTML = "Report.html";
    private static final String CHROME_SUFFIX = "_chrome";
    private static final String SCREENSHOTS_FOLDER = "/screenshots";
    private static final String UTF_8 = "utf-8";
    private static final String REPORT_TITLE = "Reports";
    private static final String REPORT_NAME = "Reports - Automation Testing";
    private static final String ACCESSIBILITY_RESULTS = "Accessibility results:";
    private static final String ID = "ID";
    private static final String IMPACT = "Impact";
    private static final String DESCRIPTION = "Description";
    private static final String HELP = "Help";
    private static final String TAGS = "Tags";
    private static final String FAILURE_SUMMARY = "Failure Summary: ";
    private static final String IMPACT_LABEL = "Impact: ";
    private static final String DESCRIPTION_LABEL = "Description: ";
    private static final String TAGS_LABEL = "Tags: ";
    private static final String HELP_LABEL = "Help: ";
    private static final String ID_LABEL = "ID: ";
    private static final String FULL_PAGE_SCREENSHOT_PREFIX = "fullPageScreenshot-";
    private static final String PNG_EXTENSION = ".png";
    private static final String ERROR_EVALUATING_SELECTOR = "Error evaluating selector: ";
    private static final String NO_VIOLATIONS_FOUND = "No violations found.";
    private static final String IMAGE_PNG = "image/png";
	private static final String VIOLATIONS = "violations";
    private static final String HELP_URL = "helpUrl";
    private static final String NODES = "nodes";
    private static final String TARGET = "target";
    private static final String DEFAULT_TAGS = "-";
    private static final String TAGS_SEPARATOR = ", ";
    private static final String EMPTY_STRING = "";
    private static final int FIRST_ELEMENT_INDEX = 0;


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

	public void takeScreenshot() {
		byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
		String testname = sc.getName();
		sc.embed(screenshot, "image/png");
		sc.write(testname);
	}

	public void logAssert_Fail(String errMsg) throws InterruptedException {

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

		log.info(AssertionMessages.URL_is + url);
		if (url != null) {
			try {
				page.navigate(url);  // Navigate to the determined URL
				takeScreenshot();
			} catch (Exception e) {
				log.error(AssertionMessages.URL_Launch_Failed + url + e.getMessage());// Log an error if navigation fails
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