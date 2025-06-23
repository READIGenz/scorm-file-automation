package com.lh.core.page;

import com.microsoft.playwright.*;
import com.microsoft.playwright.FrameLocator;
import com.lh.core.utils.McqAnswerLoader;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static com.lh.core.report.SCORMReport.generateHtmlReport;



public class DEKRAScormTestingPage extends BasePage{
    private static final Logger logger = LogManager.getLogger(DEKRAScormTestingPage.class);
    private final HashMap<String, String> locatorMap = new HashMap<>();
    private final HashMap<String, String> infoMap = new HashMap<>();

    static String courseName = McqAnswerLoader.getCurrentCourseName();
    String moduleLocatorKey = "Module" + courseName;
    String locatorValue = "//div[contains(text(), '" + courseName + "')]";
    private String initialProgressText = "";

    public DEKRAScormTestingPage(){
        locatorMap.put("IDField", "//input[@id='externalForm:login']");
        locatorMap.put("PasswordField", "//input[@id='externalForm:password']");
        locatorMap.put("LoginButton", "//button[@id='externalForm:loginButton']");
        locatorMap.put("MyCourses", "//span[contains(text(), 'My courses')]");
        locatorMap.put("All courses", "//a[@id='mylearnings-all']");
        locatorMap.put("Course", "//div[contains(text(), 'Test Factory Test AVSEC')]"); // Test Factory Test AVSEC
        locatorMap.put("Back", "//a[@id='titleWBTBackLinkEnabled']");
        locatorMap.put("Next", "//button[@class='navigation-arrow right highlight']");
        locatorMap.put(moduleLocatorKey, locatorValue); // Dynamic locator for selecting the module from JSON
        locatorMap.put("WBT", "//iframe[@id='SCO']");
        locatorMap.put("TableOfContent", "//div[@id='outline-search-content']");
        locatorMap.put("Start", "//img[@class='button']");
        locatorMap.put("Resume", "//button[text()='Resume']");

        infoMap.put("IDField", "User ID Field");
        infoMap.put("PasswordField", "User Password Field");
        infoMap.put("LoginButton", "Login Button");
        infoMap.put("Course", "User is on the WBT");
        infoMap.put("Module", "User navigates to the WBT module");
        infoMap.put("WBT", "User in on the WBT");
        infoMap.put("TableOfContent", "Course TableOfContent");
        infoMap.put("Start", "Course start button");
        infoMap.put("Resume", "Resume course");
    }

    private static final Properties mcqProperties = new Properties();

    static {
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream("src/test/java/com/lh/testdata/mcq-answers.json"), StandardCharsets.UTF_8)) {
            mcqProperties.load(reader);
        } catch (IOException e) {
            logger.error("Failed to load MCQ properties", e);
        }
    }

    public void openSCORM() throws InterruptedException {
        clicks(moduleLocatorKey);
    }


    public void verifyAndEnterText(String data, String locator) throws InterruptedException {
        boolean status = false;
        status = waitForElement(locatorMap.get(locator), 3000);
        if (status == true) {
            enterText(locatorMap.get(locator), data);
        } else {
            logAssert_Fail("Fails to enter text in " + infoMap.get(locator) + ". Locator is: " + locatorMap.get(locator));
        }
    }

    public void clicks(String locator) throws InterruptedException {
        try {
            waitForElement(locatorMap.get(locator), 60000);
            waitAndClick(locatorMap.get(locator));
            logger.info("Successfully clicked: " + locator);
        } catch (Exception e) {
            logAssert_Fail("Fails to select " + infoMap.get(locator) + ". Locator is: " + locatorMap.get(locator));
        }
    }

    public void clickOnFrame() throws InterruptedException {
        try {
            FrameLocator frame = page.frameLocator("//iframe[@id='SCO']");
            page.waitForTimeout(2000);
            page.frameLocator("//iframe[@id='SCO']").locator("//div[normalize-space()='Training beginnen']").isVisible();
            page.frameLocator("//iframe[@id='SCO']").locator("//div[normalize-space()='Training beginnen']").click();
            page.waitForLoadState();
            Locator menuIcon = frame.locator("//i[@class='fa fa-bars']");
            menuIcon.isVisible();
            menuIcon.click();
            page.waitForTimeout(2000);

        } catch (Exception e) {
            logAssert_Fail("Error" + e.getMessage());
        }
    }

    private FrameLocator getFrameLocator() {
        return page.frameLocator("//iframe[@id='SCO']");
    }

    private Locator getNextButtonLocator(FrameLocator frame) {
        return frame.locator("//button[@class='navigation-arrow right highlight']");
    }

    private Locator getVideoElementLocator(FrameLocator frame) {
        return frame.locator("//video[@preload='auto']");
    }

    private void setVideoPlaybackSpeed(Locator videoElement, double speed) {
        if (videoElement != null && videoElement.isVisible()) {
            videoElement.evaluate("video => video.playbackRate = " + speed);
            logger.info("Playback speed increased to" + speed);
        }
    }

    private void startTheSCORM(FrameLocator frame) {
        frame.locator("//div[@class='kapitel-item-titel']").nth(0).isVisible();
        frame.locator("//div[@class='kapitel-item-titel']").nth(0).click();
        frame.locator("//div[@class='seiten-nav-container active']//div[1]//div[1]//div[3]").click();
    }

    private boolean waitForVisibility(Locator locator, int timeoutSec) {
        long start = System.currentTimeMillis();
        while ((System.currentTimeMillis() - start) < timeoutSec * 1000) {
            try {
                if (locator.isVisible()) {
                    return true;
                }
            } catch (PlaywrightException ignored) {}
            page.waitForTimeout(1000);
        }
        return false;
    }

    public void completeTheCoursePartially() {
        FrameLocator frame = getFrameLocator();
        Locator nextButton = getNextButtonLocator(frame);
        Locator videoElement = getVideoElementLocator(frame);

        startTheSCORM(frame);
        page.waitForTimeout(2000);
        setVideoPlaybackSpeed(videoElement, 16.0);

        if (!waitForVisibility(nextButton, 30)) {
            logger.info("Next button not found in initial wait.");
            return;
        }

        try {
            nextButton.click();
            logger.info("Clicked Next button once.");
            page.waitForLoadState();
            setVideoPlaybackSpeed(videoElement, 16.0);
        } catch (PlaywrightException e) {
            logger.error("Click error: " + e.getMessage());
            return;
        }

        if (waitForVisibility(nextButton, 30)) {
            logger.info("Next button became visible again. Exiting function.");
        } else {
            logger.error("Next button did not reappear in time.");
        }
    }

    public void navigateThroughSCORM() {
        FrameLocator frame = getFrameLocator();
        Locator nextButton = getNextButtonLocator(frame);
        Locator videoElement = getVideoElementLocator(frame);
        Locator completionMessage = frame.locator("//div[contains(text(),'Sie haben die Unterweisung')]");
        int totalClicks = 0;

        startTheSCORM(frame);
        page.waitForLoadState();
        setVideoPlaybackSpeed(videoElement, 16.0);

        while (true) {
            if (completionMessage.isVisible()) {
                logger.info("Training completed message found.");
                break;
            }

            if (!waitForVisibility(nextButton, 30)) {
                logger.info("Next button not found in time.");
                break;
            }

            try {
                nextButton.click();
                page.waitForTimeout(2000);
                setVideoPlaybackSpeed(videoElement, 16.0);
                totalClicks++;
                logger.info("Clicked Next button. Count: " + totalClicks);
                page.waitForTimeout(1500);
            } catch (PlaywrightException e) {
                logger.error("Click error: " + e.getMessage());
                break;
            }
            clickAllVisibleVideoButtons(frame);
            answerMcqsIfPresent(frame);
        }

        logger.info("Flow complete. Total Next clicks: " + totalClicks);
        page.waitForTimeout(2000);
        scormTesting();
    }

    public static void answerMcqsIfPresent(FrameLocator frame) {
        String course = courseName; // Still dynamic

        for (int i = 1; i <= 20; i++) {
            List<String> answers = McqAnswerLoader.getAnswers(course, String.valueOf(i));

            if (!answers.isEmpty()) {
                // Try both locator strategies for the first answer to detect visibility
                String answerText = answers.get(0);

                Locator containsLabel = frame.locator("//label[contains(text(),'" + answerText + "')]");
                Locator normalizedLabel = frame.locator("//label[normalize-space()='" + answerText + "']");

                if (containsLabel.isVisible() || normalizedLabel.isVisible()) {
                    logger.info("Question " + i + " found. Selecting answers...");

                    for (String answer : answers) {
                        try {
                            // Create both locators for each answer
                            Locator option1 = frame.locator("//label[contains(text(),'" + answer + "')]");
                            Locator option2 = frame.locator("//label[normalize-space()='" + answer + "']");

                            if (option1.isVisible()) {
                                option1.click();
                                logger.info("Clicked (contains): " + answer);
                            } else if (option2.isVisible()) {
                                option2.click();
                                logger.info("Clicked (normalized): " + answer);
                            } else {
                                logger.info("Answer not visible: " + answer);
                            }
                        } catch (Exception e) {
                            logger.error("Failed to click answer: " + answer);
                        }
                    }

                    // Submit answer and click next
                    pageSafeClick(frame, "//div[@class='frage-antwort-button wbt-button']");
                    clickNextButton(page);
                }
            }
        }
    }

    public static void clickNextButton(Page page) {
        if(page.frameLocator("//iframe[@id='SCO']").locator("//div[@class='message-box-next-question-button wbt-button']").isVisible()) {
            page.frameLocator("//iframe[@id='SCO']").locator("//div[@class='message-box-next-question-button wbt-button']").click();
        }
         else if (page.frameLocator("//iframe[@id='SCO']").locator("//div[@class='frage-next-button wbt-button']").isVisible()){
            page.frameLocator("//iframe[@id='SCO']").locator("//div[@class='frage-next-button wbt-button']").click();
        }
         else {
            logger.info("No matching button was found.");
        }
    }

    public static void clickAllVisibleVideoButtons(FrameLocator frame) {
        FrameLocator frameLocator = page.frameLocator("//iframe[@id='SCO']");
        Locator videoElement = frameLocator.locator("//video[@preload='auto']");

        int index = 1;

        while (true) {
            String currentXpath = "(//div[@class='interaction-button-info'])[" + index + "]";
            Locator currentButton = frame.locator(currentXpath);

            if (currentButton.count() == 0) {
                logger.info("No more buttons at index " + index + ". Exiting.");
                break;
            }

            try {
                // Wait until the current button becomes visible (in case it's still loading)
                int maxWaitMs = 20000;
                int waited = 0;
                int pollInterval = 1000;

                while (!currentButton.isVisible() && waited < maxWaitMs) {
                    Thread.sleep(pollInterval);
                    waited += pollInterval;
                }

                if (!currentButton.isVisible()) {
                    logger.info("Button at index " + index + " never became visible. Skipping.");
                    index++;
                    continue;
                }

                logger.info("Clicking video button at index: " + index);

                currentButton.click();
                page.waitForTimeout(2000);
                if (videoElement.isVisible()){
                    videoElement.evaluate("video => video.playbackRate = 16.0"); // speeds are 2.0, 4.0, 8.0, 16.0
                } else {
                    logger.info("Video element not visible");
                }

                // ✅ Immediately check until the button disappears, then break early
                long disappearStart = System.currentTimeMillis();
                while ((System.currentTimeMillis() - disappearStart) < 20000) { // Max 3 mins wait
                    try {
                        // If the element is gone or invisible, break immediately
                        if (currentButton.count() == 0 || !currentButton.isVisible()) {
                            logger.info("Button at index " + index + " has disappeared. Proceeding.");
                            break;
                        }
                    } catch (PlaywrightException ignored) {
                        break; // element is likely detached
                    }
                    page.waitForTimeout(pollInterval);
                }

            } catch (Exception e) {
                logger.error("Error at index " + index + ": " + e.getMessage());
            }

            index++;
        }
    }

    private static void pageSafeClick(FrameLocator frame, String selector) {
        try {
            if (frame.locator(selector).isVisible()) {
                frame.locator(selector).click();
                logger.info("Clicked: " + selector);
            }
        } catch (Exception e) {
            logger.error("Failed to click: " + selector);
        }
    }

    public void tableOfContentVisibility() throws InterruptedException {
        try {
            FrameLocator frame = page.frameLocator("//iframe[@id='SCO']");
            Locator menuIcon = frame.locator("//i[@class='fa fa-bars']");
            menuIcon.isVisible();
            page.waitForTimeout(2000);

            frame.locator("//div[@id='content-menu-process-info-page']").isVisible();
            page.waitForTimeout(2000);

            takeScreenshot();
            logger.info("Table of Contents is visible");

            Locator progressElement = frame.locator("//div[@id='content-menu-process-info-overall']");
            progressElement.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            initialProgressText = progressElement.innerText();
            logger.info("Initial Progress Text: " + initialProgressText);

        } catch (Exception e) {
            logger.error("Exception in tableOfContentVisibility: " + e.getMessage());
            logAssert_Fail("Error in visibility" + e.getMessage());
        }
    }

    public void validateProgressBarUpdate() throws InterruptedException {
        try {
            FrameLocator frame = page.frameLocator("//iframe[@id='SCO']");

            Locator progressElement = frame.locator("//div[@id='content-menu-process-info-overall']");
            progressElement.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            String progressText = progressElement.innerText();
            logger.info("Current Progress Text: " + progressText);

            page.waitForTimeout(2000);

            // Compare progress
            if (progressText.equals(initialProgressText)) {
                throw new RuntimeException("Progress did not update");
            } else {
                logger.info("Progress updated successfully.");
            }

        } catch (Exception e) {
            logger.error("FAIL: Progress update check failed. " + e.getMessage());
            logAssert_Fail("Progress bar not updated" + e.getMessage());
        }
    }


    public void scormTesting() {
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

            data.version = page.evaluate("() => typeof API_1484_11 !== 'undefined' ? 'SCORM 2004' : (typeof API !== 'undefined' ? 'SCORM 1.2' : 'Unknown')").toString();
            data.studentId = page.evaluate("() => API?.LMSGetValue?.('cmi.core.student_id') || 'Not Found'").toString();
            data.studentName = page.evaluate("() => API?.LMSGetValue?.('cmi.core.student_name') || 'Not Found'").toString();
            data.objectivesCount = page.evaluate("() => API?.LMSGetValue?.('cmi.objectives._count') || 'Not Found'").toString();
            data.audioPref = page.evaluate("() => API?.LMSGetValue?.('cmi.student_preference.audio') || 'Not Found'").toString();
            data.lessonLocation = page.evaluate("() => API?.LMSGetValue?.('cmi.core.lesson_location') || 'Not Found'").toString();
            data.lessonStatus = page.evaluate("() => API?.LMSGetValue?.('cmi.core.lesson_status') || 'Not Found'").toString();
            data.lessonMode = page.evaluate("() => API?.LMSGetValue?.('cmi.core.lesson_mode') || 'Not Found'").toString();
            data.sessionTime = page.evaluate("() => API?.LMSGetValue?.('cmi.core.session_time') || 'Not Found'").toString();
            data.suspendData = page.evaluate("() => API?.LMSGetValue?.('cmi.suspend_data') || 'Not Found'").toString();
            data.rawScore = page.evaluate("() => API?.LMSGetValue?.('cmi.core.score.raw') || 'Not Found'").toString();

        generateHtmlReport(data);
        logger.info("SCORM report generated successfully.");
    }


    public void enterCredentials() throws IOException, InterruptedException {
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        props.load(fis);

        String loginId = props.getProperty("LoginID");
        String encodedPassword = props.getProperty("Password");

        // Decode Base64 password
        byte[] decodedBytes = Base64.getDecoder().decode(encodedPassword);
        String password = new String(decodedBytes);

        logger.info("Launching LMS with credentials");
        clicks("IDField");
        enterText("//input[@id='externalForm:login']", loginId);
        clicks("PasswordField");
        enterText("//input[@id='externalForm:password']", password);


    }
}
