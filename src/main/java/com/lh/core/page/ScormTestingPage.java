package com.lh.core.page;

import com.microsoft.playwright.*;
import com.microsoft.playwright.FrameLocator;
import com.lh.core.utils.McqAnswerLoader;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static com.lh.core.library.SCORMReport.generateHtmlReport;

public class ScormTestingPage extends BasePage{
    private final HashMap<String, String> locatorMap = new HashMap<>();
    private final HashMap<String, String> infoMap = new HashMap<>();

    static String courseName = McqAnswerLoader.getCurrentCourseName();
    String moduleLocatorKey = "Module" + courseName;
    String locatorValue = "//div[contains(text(), '" + courseName + "')]";
    private String initialProgressText = "";

    public ScormTestingPage(){
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
                new FileInputStream("src/test/java/com/lh/testdata/mcq-answers.properties"), StandardCharsets.UTF_8)) {
            mcqProperties.load(reader);
        } catch (IOException e) {
            System.err.println("Failed to load MCQ properties: " + e.getMessage());
        }
    }

    public void openSCORM() throws InterruptedException {
        clicks(moduleLocatorKey);
    }


    public void verifyAndEnterText(String locator, String data) throws InterruptedException {
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
            System.out.println("Successfully clicked: " + locator);
        } catch (Exception e) {
            logAssert_Fail("Fails to select " + infoMap.get(locator) + ". Locator is: " + locatorMap.get(locator));
        }
    }

    public void frameClick() throws InterruptedException {
        try {
            page.waitForTimeout(2000);
            page.frameLocator("//iframe[@id='SCO']").locator("//div[normalize-space()='Training beginnen']").isVisible();
            page.frameLocator("//iframe[@id='SCO']").locator("//div[normalize-space()='Training beginnen']").click();
//            page.waitForTimeout(2000);
            page.waitForLoadState();
        } catch (Exception e) {
            logAssert_Fail("Error" + e.getMessage());
        }
    }

    public void partialCourseCompletion() {
        FrameLocator frame = page.frameLocator("//iframe[@id='SCO']");
        Locator nextButton = frame.locator("//button[@class='navigation-arrow right highlight']");

        page.frameLocator("//iframe[@id='SCO']").locator("//i[@class='fa fa-bars']").isVisible();
        page.frameLocator("//iframe[@id='SCO']").locator("(//div[@class='kapitel-item-titel'])[1]").isVisible();
        page.frameLocator("//iframe[@id='SCO']").locator("(//div[@class='kapitel-item-titel'])[1]").click();
        page.frameLocator("//iframe[@id='SCO']").locator("//div[@class='seiten-nav-container active']//div[1]//div[1]//div[3]").isVisible();
        page.frameLocator("//iframe[@id='SCO']").locator("//div[@class='seiten-nav-container active']//div[1]//div[1]//div[3]").click();

        FrameLocator frameLocator = page.frameLocator("//iframe[@id='SCO']");
        Locator videoElement = frameLocator.locator("//video[@preload='auto']");

        // Set the playback rate to 2x (fast forward)
        videoElement.evaluate("video => video.playbackRate = 16.0");


        int maxWaitSecs = 30;
        int pollInterval = 1000;

        // Step 1: Wait for the Next button to appear
        boolean firstAppearance = false;
        long start = System.currentTimeMillis();

        while ((System.currentTimeMillis() - start) < maxWaitSecs * 1000) {
            try {
                if (nextButton.isVisible()) {
                    firstAppearance = true;
                    break;
                }
            } catch (PlaywrightException ignored) {}
            page.waitForTimeout(pollInterval);
        }

        if (!firstAppearance) {
            System.out.println("Next button not found in initial wait.");
            return;
        }

        // Step 2: Click the Next button once
        try {
            nextButton.click();
            System.out.println("Clicked Next button once.");
            videoElement.evaluate("video => video.playbackRate = 16.0");
//            page.waitForTimeout(1500); // optional wait after click
        } catch (PlaywrightException e) {
            System.out.println("Click error: " + e.getMessage());
            return;
        }

        // Step 3: Wait for the button to become visible again
        boolean secondAppearance = false;
        start = System.currentTimeMillis();

        while ((System.currentTimeMillis() - start) < maxWaitSecs * 1000) {
            try {
                if (nextButton.isVisible()) {
                    secondAppearance = true;
                    break;
                }
            } catch (PlaywrightException ignored) {}
            page.waitForTimeout(pollInterval);
        }

        if (secondAppearance) {
            System.out.println("Next button became visible again. Exiting function.");
        } else {
            System.out.println("Next button did not reappear in time.");
        }
    }

    public void courseNavigation(){
        FrameLocator frame = page.frameLocator("//iframe[@id='SCO']");

        page.frameLocator("//iframe[@id='SCO']").locator("//i[@class='fa fa-bars']").isVisible();
        page.frameLocator("//iframe[@id='SCO']").locator("//i[@class='fa fa-bars']").click();
        page.frameLocator("//iframe[@id='SCO']").locator("(//div[@class='kapitel-item-titel'])[1]").isVisible();
        page.frameLocator("//iframe[@id='SCO']").locator("(//div[@class='kapitel-item-titel'])[1]").click();
        page.frameLocator("//iframe[@id='SCO']").locator("//div[@class='seiten-nav-container active']//div[1]//div[1]//div[3]").isVisible();
        page.frameLocator("//iframe[@id='SCO']").locator("//div[@class='seiten-nav-container active']//div[1]//div[1]//div[3]").click();

        FrameLocator frameLocator = page.frameLocator("//iframe[@id='SCO']");
        Locator videoElement = frameLocator.locator("//video[@preload='auto']");

        // Set the playback rate to 2x (fast forward)
        videoElement.evaluate("video => video.playbackRate = 16.0");

        Locator nextButton = frameLocator.locator("//button[@class='navigation-arrow right highlight']");

        int maxWaitSecs = 30;
        int pollInterval = 1000;
        int totalClicks = 0;

        Locator completionMessage = frame.locator("//div[contains(text(),'Sie haben die Unterweisung')]");

        while (true) {
            // ======== Check for completion message before proceeding ========
            try {
                if (completionMessage.isVisible()) {
                    System.out.println("Training completed message found.");
                    break;
                }
            } catch (PlaywrightException ignored) {}

            boolean appeared = false;
            long start = System.currentTimeMillis();

            // Wait for Next button to appear
            while ((System.currentTimeMillis() - start) < maxWaitSecs * 1000) {
                try {
                    if (nextButton.isVisible()) {
                        appeared = true;
                        break;
                    }
                } catch (PlaywrightException ignored) {}
                page.waitForTimeout(pollInterval);
            }

            if (!appeared) {
                System.out.println("Next button not found in time.");
                break;
            }

            // Click Next
            try {
                nextButton.click();
                page.waitForTimeout(2000);
                if (videoElement.isVisible()){
                    videoElement.evaluate("video => video.playbackRate = 16.0"); // speeds are 2.0, 4.0, 8.0, 16.0
                } else {
                    System.out.println("Video element not visible");
                }

                totalClicks++;
                System.out.println("Clicked Next button. Count: " + totalClicks);
                page.waitForTimeout(1500); // optional wait for content load
            } catch (PlaywrightException e) {
                System.out.println("Click error: " + e.getMessage());
                break;
            }

            // ======== Check for changed UI for video them if present ========
            clickAllVisibleVideoButtons(page.frameLocator("//iframe[@id='SCO']"));
            // ======== Check for MCQs and answer them if present ========
            answerMcqsIfPresent(frame);
        }

        System.out.println("Flow complete. Total Next clicks: " + totalClicks);
        page.waitForTimeout(2000);

        scormTestingPOC();

    }

    private static void answerMcqsIfPresent(FrameLocator frame) {
        String course = courseName; // Still dynamic

        for (int i = 1; i <= 20; i++) {
            List<String> answers = McqAnswerLoader.getAnswers(course, String.valueOf(i));

            if (!answers.isEmpty()) {
                // Try both locator strategies for the first answer to detect visibility
                String answerText = answers.get(0);

                Locator containsLabel = frame.locator("//label[contains(text(),'" + answerText + "')]");
                Locator normalizedLabel = frame.locator("//label[normalize-space()='" + answerText + "']");

                if (containsLabel.isVisible() || normalizedLabel.isVisible()) {
                    System.out.println("Question " + i + " found. Selecting answers...");

                    for (String answer : answers) {
                        try {
                            // Create both locators for each answer
                            Locator option1 = frame.locator("//label[contains(text(),'" + answer + "')]");
                            Locator option2 = frame.locator("//label[normalize-space()='" + answer + "']");

                            if (option1.isVisible()) {
                                option1.click();
                                System.out.println("Clicked (contains): " + answer);
                            } else if (option2.isVisible()) {
                                option2.click();
                                System.out.println("Clicked (normalized): " + answer);
                            } else {
                                System.out.println("Answer not visible: " + answer);
                            }
                        } catch (Exception e) {
                            System.err.println("Failed to click answer: " + answer);
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
            System.out.println("No matching button was found.");
        }
    }

    private static void clickAllVisibleVideoButtons(FrameLocator frame) {
        FrameLocator frameLocator = page.frameLocator("//iframe[@id='SCO']");
        Locator videoElement = frameLocator.locator("//video[@preload='auto']");

        int index = 1;

        while (true) {
            String currentXpath = "(//div[@class='interaction-button-info'])[" + index + "]";
            Locator currentButton = frame.locator(currentXpath);

            if (currentButton.count() == 0) {
                System.out.println("No more buttons at index " + index + ". Exiting.");
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
                    System.out.println("Button at index " + index + " never became visible. Skipping.");
                    index++;
                    continue;
                }

                System.out.println("Clicking video button at index: " + index);

                currentButton.click();
                page.waitForTimeout(2000);
                if (videoElement.isVisible()){
                    videoElement.evaluate("video => video.playbackRate = 16.0"); // speeds are 2.0, 4.0, 8.0, 16.0
                } else {
                    System.out.println("Video element not visible");
                }

                // ✅ Immediately check until the button disappears, then break early
                long disappearStart = System.currentTimeMillis();
                while ((System.currentTimeMillis() - disappearStart) < 20000) { // Max 3 mins wait
                    try {
                        // If the element is gone or invisible, break immediately
                        if (currentButton.count() == 0 || !currentButton.isVisible()) {
                            System.out.println("Button at index " + index + " has disappeared. Proceeding.");
                            break;
                        }
                    } catch (PlaywrightException ignored) {
                        break; // element is likely detached
                    }
                    page.waitForTimeout(pollInterval);
                }

            } catch (Exception e) {
                System.out.println("Error at index " + index + ": " + e.getMessage());
            }

            index++;
        }
    }

    private static void pageSafeClick(FrameLocator frame, String selector) {
        try {
            if (frame.locator(selector).isVisible()) {
                frame.locator(selector).click();
                System.out.println("Clicked: " + selector);
            }
        } catch (Exception e) {
            System.out.println("Failed to click: " + selector);
        }
    }

    public void tableOfContentVisibility() throws InterruptedException {
        try {
            FrameLocator frame = page.frameLocator("//iframe[@id='SCO']");
            Locator menuIcon = frame.locator("//i[@class='fa fa-bars']");
            menuIcon.isVisible();
            menuIcon.click();
            page.waitForTimeout(2000);

            frame.locator("//div[@id='content-menu-process-info-page']").isVisible();
            page.waitForTimeout(2000);

            takeScreenshot();
            System.out.println("Table of Contents is visible");

            Locator progressElement = frame.locator("//div[@id='content-menu-process-info-overall']");
            progressElement.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            initialProgressText = progressElement.innerText();
            System.out.println("Initial Progress Text: " + initialProgressText);

        } catch (Exception e) {
            System.out.println("Exception in tableOfContentVisibility: " + e.getMessage());
            logAssert_Fail("Error in visibility" + e.getMessage());
        }
    }

    public void progressBarUpdation() throws InterruptedException {
        try {
            FrameLocator frame = page.frameLocator("//iframe[@id='SCO']");
            Locator menuIcon = frame.locator("//i[@class='fa fa-bars']");
            menuIcon.isVisible();
            menuIcon.click();
            page.waitForTimeout(2000);

            Locator progressElement = frame.locator("//div[@id='content-menu-process-info-overall']");
            progressElement.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            String progressText = progressElement.innerText();
            System.out.println("Current Progress Text: " + progressText);

            page.waitForTimeout(2000);

            // Compare progress
            if (progressText.equals(initialProgressText)) {
                throw new RuntimeException("Progress did not update");
            } else {
                System.out.println("Progress updated successfully.");
            }

        } catch (Exception e) {
            System.out.println("FAIL: Progress update check failed. " + e.getMessage());
            logAssert_Fail("Progress bar not updated" + e.getMessage());
        }
    }


    public void scormTestingPOC() {
        // Wait for the iframe and get its locator
        page.waitForSelector("//iframe[@id='SCO']", new Page.WaitForSelectorOptions().setTimeout(2000));

        ElementHandle iframeElement = page.querySelector("#SCO");
        if (iframeElement == null) {
            System.out.println("Iframe element not found!");
            return;
        }

        Frame scormFrame = iframeElement.contentFrame();

        if (scormFrame == null) {
            System.out.println("SCORM frame not loaded yet!");
            return;
        }

        System.out.println("Frame URL: " + scormFrame.url());  // ✅ Now it's safe
        page.waitForTimeout(10000); // give time to load the API

        System.out.println("SCORM session initialized successfully");

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
        System.out.println("SCORM report generated successfully.");
    }


}
