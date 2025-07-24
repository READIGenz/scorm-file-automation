package com.lh.core.page;

import com.aventstack.extentreports.ExtentTest;
import com.lh.core.SCORMReport.FileWriterService;
import com.lh.core.SCORMReport.HtmlReportGenerator;
import com.lh.core.SCORMReport.ReportGenerator;
import com.lh.core.SCORMReport.SCORMReportService;
import com.lh.core.SCORMReport.model.ScormData;
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
	public static final org.apache.log4j.Logger logger = LogManager.getLogger(BasePage.class);
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

		logger.error(errMsg);
		takeScreenshot();
		isScreenshotCapturedAfterFailure = true;
		sc.write(errMsg);
		Assert.fail(errMsg);
	}

	public void launchBrowser(String application) throws IOException, InterruptedException {
		Browser browser = DriverManager.getBrowser();
		page = browser.newPage();
		String url = null;
		url = application;

		if (url != null) {
			try {
				page.navigate(url);  // Navigate to the determined URL
				takeScreenshot();
			} catch (Exception e) {
				logger.error(url + e.getMessage());// Log an error if navigation fails
			}
		}
		else {
			logger.error(ErrorCodeReader.getErrorCode("ET1001"));   // Log a message if the URL is null
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

	public void scormTesting() throws InterruptedException {
		page.waitForLoadState();
		// Wait for the iframe and get its locator
		page.waitForSelector("//iframe[@id='SCO']", new Page.WaitForSelectorOptions().setTimeout(2000));

		ElementHandle iframeElement = page.querySelector("#SCO");
		if (iframeElement == null) {
			logger.info("Iframe element not found!");
			return;
		}

		Frame scormFrame = iframeElement.contentFrame();

		if (scormFrame == null) {
			logger.info("SCORM frame not loaded yet!");
			return;
		}

		logger.info("Frame URL: " + scormFrame.url());  // ✅ Now it's safe
		page.waitForTimeout(10000); // give time to load the API

		logger.info("SCORM session initialized successfully");

		ScormData data = new ScormData();

		// Detect SCORM version
		String version = page.evaluate("() => typeof API_1484_11 !== 'undefined' ? 'SCORM 2004' : (typeof API !== 'undefined' ? 'SCORM 1.2' : 'Unknown')").toString();
		data.version = version;
		logger.info("SCORM Version: " + version);

		if (version.equals("SCORM 2004")) {
			// SCORM 2004 API calls
			data.studentId = page.evaluate("() => API_1484_11?.GetValue('cmi.learner_id') || 'Not Found'").toString();
			data.studentName = page.evaluate("() => API_1484_11?.GetValue('cmi.learner_name') || 'Not Found'").toString();
			data.objectivesCount = page.evaluate("() => API_1484_11?.GetValue('cmi.objectives._count') || 'Not Found'").toString();
			data.audioPref = page.evaluate("() => API_1484_11?.GetValue('cmi.learner_preference.audio_level') || 'Not Found'").toString();
			data.lessonLocation = page.evaluate("() => API_1484_11?.GetValue('cmi.location') || 'Not Found'").toString();
			data.lessonStatus = page.evaluate("() => API_1484_11?.GetValue('cmi.completion_status') || 'Not Found'").toString();
			data.lessonMode = page.evaluate("() => API_1484_11?.GetValue('cmi.mode') || 'Not Found'").toString();

		} else if (version.equals("SCORM 1.2")) {
			// SCORM 1.2 API calls
			data.studentId = page.evaluate("() => API?.LMSGetValue?.('cmi.core.student_id') || 'Not Found'").toString();
			data.studentName = page.evaluate("() => API?.LMSGetValue?.('cmi.core.student_name') || 'Not Found'").toString();
			data.objectivesCount = page.evaluate("() => API?.LMSGetValue?.('cmi.objectives._count') || 'Not Found'").toString();
			data.audioPref = page.evaluate("() => API?.LMSGetValue?.('cmi.student_preference.audio') || 'Not Found'").toString();
			data.lessonLocation = page.evaluate("() => API?.LMSGetValue?.('cmi.core.lesson_location') || 'Not Found'").toString();
			data.lessonStatus = page.evaluate("() => API?.LMSGetValue?.('cmi.core.lesson_status') || 'Not Found'").toString();
			data.lessonMode = page.evaluate("() => API?.LMSGetValue?.('cmi.core.lesson_mode') || 'Not Found'").toString();

		} else {
			logger.warn("Unsupported or unknown SCORM version.");
			return;
		}

		ReportGenerator generator = new HtmlReportGenerator();
		FileWriterService writer = new FileWriterService();
		SCORMReportService reportService = new SCORMReportService(generator, writer);

        reportService.generateAndSave(data);
		logger.info("SCORM report generated successfully.");
 	}


}
// Common methods that can be used across different pages
// ...