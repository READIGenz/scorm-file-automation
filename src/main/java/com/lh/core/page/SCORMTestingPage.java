package com.lh.core.page;

import com.lh.core.utils.McqAnswerLoader;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Properties;

public class SCORMTestingPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(SCORMTestingPage.class);

    // Static course name and dynamic locator for module
    private static final String COURSE_NAME = McqAnswerLoader.getCurrentCourseName();
    private static final String MODULE_LOCATOR = "//div[contains(text(), '" + COURSE_NAME + "')]";

    // Constants for locators
    // LOGIN PAGE
    private static final String ID_FIELD_LOCATOR = "//input[@id='externalForm:login']";
    private static final String PASSWORD_FIELD_LOCATOR = "//input[@id='externalForm:password']";
    private static final String LOGIN_BTN_LOCATOR = "//button[@id='externalForm:loginButton']";

    // SCORM NAVIGATION
    private static final String MY_COURSES_LOCATOR = "//span[contains(text(), 'My courses')]";
    private static final String ALL_COURSES_LOCATOR = "//a[@id='mylearnings-all']";
    private static final String COURSE_LOCATOR = "//div[contains(text(), 'Test Factory Test AVSEC')]";

    public void clicks(String locator) throws InterruptedException {
        try {
            waitForElement(locator, 60000);
            waitAndClick(locator);
            logger.info("Successfully clicked: " + locator);
        } catch (Exception e) {
            logAssert_Fail("Fails to select " + locator);
        }
    }

    public void loginAsLearner() throws InterruptedException {
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            props.load(fis);

            String loginId = props.getProperty("LoginID");
            String encodedPassword = props.getProperty("Password");

            // Decode Base64 password
            byte[] decodedBytes = Base64.getDecoder().decode(encodedPassword);
            String password = new String(decodedBytes);

            logger.info("Launching LMS with credentials");
            clicks(ID_FIELD_LOCATOR);
            enterText(ID_FIELD_LOCATOR, loginId);
            clicks(PASSWORD_FIELD_LOCATOR);
            enterText(PASSWORD_FIELD_LOCATOR, password);
            clicks(LOGIN_BTN_LOCATOR);
        } catch (Exception e) {
            logAssert_Fail("Error in login due to " + e.getMessage());
        }
    }

    public void navigatesTillSCORM() throws InterruptedException {
        try {
            waitAndClick(MY_COURSES_LOCATOR);
            waitAndClick(ALL_COURSES_LOCATOR);
            waitAndClick(COURSE_LOCATOR);
        } catch (Exception e) {
            logAssert_Fail("Error in navigation due to " + e.getMessage());
        }

    }

    public void openSCORM() throws InterruptedException {
        waitAndClick(MODULE_LOCATOR);
        logger.info("Clicked on SCORM module for course: " + COURSE_NAME);
    }
}