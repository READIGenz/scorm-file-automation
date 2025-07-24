package com.lh.core.page;

import com.microsoft.playwright.*;
import com.microsoft.playwright.FrameLocator;
import com.lh.core.utils.McqAnswerLoader;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;


public class DEKRAScormTestingPage extends BasePage{
    private static final Logger logger = LogManager.getLogger(DEKRAScormTestingPage.class);
    private String initialProgressText = "";

    // OPENING SCORM DYNAMICALLY FROM THE JSON FILE
    static String courseName = McqAnswerLoader.getCurrentCourseName();
    private static final String locatorValue = "//div[contains(text(), '" + courseName + "')]";

    // USER STARTS THE COURSE
    private static final String SCO_IFRAME_LOCATOR = "//iframe[@id='SCO']";
    private static final String TRAINING_BEGINNEN_LOCATOR = "//div[normalize-space()='Training beginnen']";
    private static final String MENU_ICON_LOCATOR = "//i[@class='fa fa-bars']";

    // TABLE OF CONTENTS AND PROGRESS BAR
    private static final String CONTENT_MENU_PAGE_LOCATOR = "//div[@id='content-menu-process-info-page']";
    private static final String OVERALL_PROGRESS_LOCATOR = "//div[@id='content-menu-process-info-overall']";

    // SCORM NAVIGATION
    private static final String FIRST_CHAPTER_TITLE_LOCATOR = "//div[@class='kapitel-item-titel']";
    private static final String ACTIVE_PAGE_ELEMENT_LOCATOR = "//div[@class='seiten-nav-container active']//div[1]//div[1]//div[3]";
    private static final String BACK_BUTTON_LOCATOR = "//a[@id='titleWBTBackLinkEnabled']";
    private static final String NEXT_BUTTON_LOCATOR = "//button[@class='navigation-arrow right highlight']";
    private static final String VIDEO_ELEMENT_LOCATOR = "//video[@preload='auto']";
    private static final String PREVIOUS_BUTTON_LOCATOR = "//button[contains(@class, 'navigation-arrow left')]";
    private static final String COMPLETION_MESSAGE_LOCATOR = "//div[contains(text(),'Sie haben die Unterweisung')]";

    // DRAG AND DROP QUESTION TYPE LOCATORS
    private static final String RICHTIG_LOCATOR = "//div[@id='column-1']";
    private static final String FALSCH_LOCATOR = "//div[@id='column-2']";
    private static final String CHECK_BUTTON_LOCATOR = "//div[@class='drag-n-drop-check-button wbt-button']";
    private static final String NEXT_QUESTION_BUTTON_LOCATOR = "//div[contains(@class, 'message-box-next-question-button') and contains(text(), 'Nächste Frage')]";
    private static final String ANSWER_OPTION_LOCATOR_TEMPLATE = "//div[contains(text(),\"%s\")]";

    // ADDITIONAL VIDEO UI LOCATORS
    private static final String INTERACTION_BUTTON_LOCATOR_TEMPLATE = "(//div[@class='interaction-button-info'])[%d]";

    // MCQ TYPE QUESTIONS LOCATORS
    private static final String CONTAINS_LABEL_LOCATOR_TEMPLATE = "//label[contains(text(),'%s')]";
    private static final String NORMALIZED_LABEL_LOCATOR_TEMPLATE = "//label[normalize-space()='%s']";
    private static final String SUBMIT_BUTTON_LOCATOR = "//div[@class='frage-antwort-button wbt-button']";
    private static final String NEXT_QUESTION_BUTTON = "//div[@class='message-box-next-question-button wbt-button']";
    private static final String QUESTION_NEXT_BUTTON = "//div[@class='frage-next-button wbt-button']";


    private static final Properties mcqProperties = new Properties();
    static {
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream("src/test/java/com/lh/testdata/mcq-answers.json"), StandardCharsets.UTF_8)) {
            mcqProperties.load(reader);
        } catch (IOException e) {
            logger.error("Failed to load MCQ properties", e);
        }
    }

    public void clicks(String locator) throws InterruptedException {
        try {
            waitForElement(locator, 60000);
            waitAndClick(locator);
            logger.info("Successfully clicked: " + locator);
        } catch (Exception e) {
            logAssert_Fail("Fails to select " + locator);
        }
    }

    public void userStartsTheCourse() throws InterruptedException {
        try {
            page.waitForTimeout(5000);
            page.frameLocator(SCO_IFRAME_LOCATOR).locator(TRAINING_BEGINNEN_LOCATOR).isVisible();
            page.frameLocator(SCO_IFRAME_LOCATOR).locator(TRAINING_BEGINNEN_LOCATOR).click();

            page.waitForLoadState();
            page.frameLocator(SCO_IFRAME_LOCATOR).locator(MENU_ICON_LOCATOR).click();
        } catch (Exception e) {
            logAssert_Fail("Error in userStartsTheCourse: " + e.getMessage());
        }
    }

    public void tableOfContentAndProgressVisibility() throws InterruptedException {
        try {
            page.waitForTimeout(2000);
            page.frameLocator(SCO_IFRAME_LOCATOR).locator(CONTENT_MENU_PAGE_LOCATOR).click();
            page.waitForTimeout(2000);

            takeScreenshot();
            logger.info("Table of Contents is visible");

            Locator progressElement = page.frameLocator(SCO_IFRAME_LOCATOR).locator(OVERALL_PROGRESS_LOCATOR);
            progressElement.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            initialProgressText = progressElement.innerText();
            logger.info("Initial Progress Text: " + initialProgressText);

        } catch (Exception e) {
            logger.error("Exception in tableOfContentAndProgressVisibility: " + e.getMessage());
            logAssert_Fail("Error in tableOfContentAndProgressVisibility: " + e.getMessage());
        }
    }

    public void completeTheCoursePartially() throws InterruptedException {
        FrameLocator frame = page.frameLocator(SCO_IFRAME_LOCATOR);
        Locator nextButton = frame.locator(NEXT_BUTTON_LOCATOR);
        Locator videoElement = frame.locator(VIDEO_ELEMENT_LOCATOR);

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
            page.waitForTimeout(2000);
            setVideoPlaybackSpeed(videoElement, 16.0);
        } catch (PlaywrightException e) {
            logger.error("Click error: " + e.getMessage());
            logAssert_Fail("Click error: " + e.getMessage());
            return;
        }

        if (waitForVisibility(nextButton, 30)) {
            logger.info("Next button became visible again. Exiting function.");
            clicks(BACK_BUTTON_LOCATOR);
        } else {
            logger.error("Next button did not reappear in time.");
        }
    }

    private void setVideoPlaybackSpeed(Locator videoElement, double speed) {
        if (videoElement != null && videoElement.isVisible()) {
            videoElement.evaluate("video => video.playbackRate = " + speed);
            logger.info("Playback speed increased to" + speed);
        }
    }

    private void startTheSCORM(FrameLocator frame) throws InterruptedException {
        try {
            page.waitForLoadState();
            Locator firstChapter = frame.locator(FIRST_CHAPTER_TITLE_LOCATOR).nth(0);
            if (firstChapter.isVisible()) {
                firstChapter.click();
                frame.locator(ACTIVE_PAGE_ELEMENT_LOCATOR).click();
            } else {
                logAssert_Fail("First chapter title is not visible");
            }
        } catch (InterruptedException e) {
            logAssert_Fail("First chapter title is not visible");
        }
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

    public void navigateThroughSCORM() throws IOException, InterruptedException {
        FrameLocator frame = page.frameLocator(SCO_IFRAME_LOCATOR);
        Locator nextButton = frame.locator(NEXT_BUTTON_LOCATOR);
        Locator videoElement = frame.locator(VIDEO_ELEMENT_LOCATOR);
        Locator completionMessage = frame.locator(COMPLETION_MESSAGE_LOCATOR);

        int totalClicks = 0;

        startTheSCORM(frame);
        page.waitForTimeout(2000);
        setVideoPlaybackSpeed(videoElement, 16.0);

        while (true) {
            if (completionMessage.isVisible()) {
                logger.info("Training completed message found.");
                break;
            }

            if (!waitForVisibility(nextButton, 60)) {
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
                logAssert_Fail("Click error: " + e.getMessage());
            }
            dragAndDrop(frame);
            clickAllVisibleVideoButtons(frame);
            answerMcqsIfPresent(frame);
        }

        logger.info("Flow complete. Total Next clicks: " + totalClicks);
        page.waitForTimeout(2000);
        scormTesting();
    }

    public static void dragAndDrop(FrameLocator frameLocator) throws IOException {
        Locator richtig = frameLocator.locator(RICHTIG_LOCATOR);
        Locator falsch = frameLocator.locator(FALSCH_LOCATOR);

        // Check visibility before proceeding
        if (!richtig.isVisible() || !falsch.isVisible()) {
            logger.warn("Drop zones not visible. Skipping drag-and-drop.");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("src/test/java/com/lh/testdata/mcq-answers.json"));
        String currentCourse = root.get("currentCourse").asText();

        for (int i = 1; i <= 20; i++) {
            String questionKey = currentCourse + "_Q" + i;
            JsonNode answerSet = root.get(questionKey);

            if (answerSet == null) continue;

            JsonNode richtigAnswers = answerSet.get("richtig");
            JsonNode falschAnswers = answerSet.get("falsch");

            if (richtigAnswers != null && !richtigAnswers.isEmpty()) {
                String previewText = richtigAnswers.get(0).asText();
                Locator previewOption = frameLocator.locator(String.format(ANSWER_OPTION_LOCATOR_TEMPLATE, previewText));

                if (!previewOption.isVisible()) continue;

                logger.info("Drag-and-drop question " + i + " found. Performing actions...");

                for (JsonNode answerNode : richtigAnswers) {
                    String text = answerNode.asText();
                    Locator option = frameLocator.locator(String.format(ANSWER_OPTION_LOCATOR_TEMPLATE, text));
                    if (option.isVisible()) {
                        option.dragTo(richtig);
                        logger.info("Dragged to Richtig: " + text);
                    } else {
                        logger.warn("Richtig option not found or not visible: " + text);
                    }
                }

                if (falschAnswers != null) {
                    for (JsonNode answerNode : falschAnswers) {
                        String text = answerNode.asText();
                        Locator option = frameLocator.locator(String.format(ANSWER_OPTION_LOCATOR_TEMPLATE, text));
                        if (option.isVisible()) {
                            option.dragTo(falsch);
                            logger.info("Dragged to Falsch: " + text);
                        } else {
                            logger.warn("Falsch option not found or not visible: " + text);
                        }
                    }
                }

                Locator checkButton = frameLocator.locator(CHECK_BUTTON_LOCATOR);
                if (checkButton.isVisible()) {
                    checkButton.click();
                    logger.info("Clicked check button for question " + i);
                }

                Locator nextQuestion = frameLocator.locator(NEXT_QUESTION_BUTTON_LOCATOR);
                if (nextQuestion.isVisible()) {
                    nextQuestion.click();
                    logger.info("Next Question button clicked");
                }
            }
        }
    }

    public static void answerMcqsIfPresent(FrameLocator frame) throws InterruptedException {
        String course = courseName;

        for (int i = 1; i <= 20; i++) {
            List<String> answers = McqAnswerLoader.getAnswers(course, String.valueOf(i));

            if (answers.isEmpty()) continue;

            String answerText = answers.get(0);

            Locator containsLabel = frame.locator(String.format(CONTAINS_LABEL_LOCATOR_TEMPLATE, answerText));
            Locator normalizedLabel = frame.locator(String.format(NORMALIZED_LABEL_LOCATOR_TEMPLATE, answerText));

            if (containsLabel.isVisible() || normalizedLabel.isVisible()) {
                logger.info("Question " + i + " found. Selecting answers...");

                for (String answer : answers) {
                    try {
                        Locator option1 = frame.locator(String.format(CONTAINS_LABEL_LOCATOR_TEMPLATE, answer));
                        Locator option2 = frame.locator(String.format(NORMALIZED_LABEL_LOCATOR_TEMPLATE, answer));

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
                        logger.error("Failed to click answer: " + answer + ". Error: " + e.getMessage());
                    }
                }
                // Submit answer
                pageSafeClick(frame);
                clickNextButton(page);
            }
        }
    }

    private static void pageSafeClick(FrameLocator frame) throws InterruptedException {
        try {
            if (frame.locator(SUBMIT_BUTTON_LOCATOR).isVisible()) {
                frame.locator(SUBMIT_BUTTON_LOCATOR).click();
                logger.info("Clicked: " + SUBMIT_BUTTON_LOCATOR);
            }
        } catch (Exception e) {
            logger.error("Failed to click: " + SUBMIT_BUTTON_LOCATOR);
            logAssert_Fail("Failed to click: " + e.getMessage()) ;
        }
    }

    public static void clickNextButton(Page page) throws InterruptedException {
        FrameLocator frame = page.frameLocator(SCO_IFRAME_LOCATOR);

        Locator nextQuestionBtn = frame.locator(NEXT_QUESTION_BUTTON);
        Locator questionNextBtn = frame.locator(QUESTION_NEXT_BUTTON);

        try {
            if (nextQuestionBtn.isVisible()) {
                nextQuestionBtn.click();
                logger.info("Clicked 'Next Question' button.");
            } else if (questionNextBtn.isVisible()) {
                questionNextBtn.click();
                logger.info("Clicked 'Frage Next' button.");
            } else {
                logger.info("No matching next button was found.");
            }
        } catch (Exception e) {
            logger.error("Failed to click next button: " + e.getMessage());
            logAssert_Fail("Next button click failed: " + e.getMessage());
        }
    }
    public static void clickAllVisibleVideoButtons(FrameLocator frame) {
        FrameLocator frameLocator = page.frameLocator(SCO_IFRAME_LOCATOR);
        Locator videoElement = frameLocator.locator(VIDEO_ELEMENT_LOCATOR);

        int index = 1;

        while (true) {
            String currentXpath = String.format(INTERACTION_BUTTON_LOCATOR_TEMPLATE, index);
            Locator currentButton = frame.locator(currentXpath);

            if (currentButton.count() == 0) {
                logger.info("No more buttons at index " + index + ". Exiting.");
                break;
            }

            try {
                // Wait for the button to be visible (max 10s)
                currentButton.waitFor(new Locator.WaitForOptions()
                        .setTimeout(10000)
                        .setState(WaitForSelectorState.VISIBLE));

                logger.info("Clicking video button at index: " + index);
                currentButton.click();
                page.waitForTimeout(2000);

                if (videoElement.isVisible()) {
                    videoElement.evaluate("video => video.playbackRate = 16.0");
                } else {
                    logger.info("Video element not visible");
                }

                // Wait until the button disappears (max 10s)
                currentButton.waitFor(new Locator.WaitForOptions()
                        .setTimeout(10000)
                        .setState(WaitForSelectorState.HIDDEN));
                logger.info("Button at index " + index + " has disappeared. Proceeding.");

            } catch (PlaywrightException e) {
                logger.warn("Button at index " + index + " is not clickable or didn't disappear in time.");
            } catch (Exception e) {
                logger.error("Unexpected error at index " + index + ": " + e.getMessage());
            }

            index++;
        }
    }

    public void validateProgressBarUpdate() throws InterruptedException {
        try {
            Locator progressElement = page.frameLocator(SCO_IFRAME_LOCATOR).locator(OVERALL_PROGRESS_LOCATOR);
            progressElement.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            String progressText = progressElement.innerText();
            logger.info("Current Progress Text: " + progressText);

            page.waitForTimeout(2000);

            // Compare progress
            if (progressText.equals(initialProgressText)) {
                logAssert_Fail("Progress did not update!");
            } else {
                logger.info("Progress updated successfully.");
            }

        } catch (Exception e) {
            logger.error("FAIL: Progress update check failed. " + e.getMessage());
            logAssert_Fail("Progress bar not updated" + e.getMessage());
        }
    }
}