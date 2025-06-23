package com.lh.steps;

import com.lh.core.page.BasePage;
import com.lh.core.page.DEKRAScormTestingPage;
import com.lh.utilities.Configurations;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.io.IOException;

public class DEKRAScormTestingSteps extends BasePage{

    DEKRAScormTestingPage scormTestingPage = new DEKRAScormTestingPage();

    @Given("The user launches LHG-LMS url")
    public void userLaunchesLMSurl() throws IOException, InterruptedException {
//        launchBrowser(Configurations.LHG_LMS_URL);
        try {
            launchBrowser(Configurations.LHG_LMS_URL);
        } catch (Exception e) {
            logAssert_Fail("LMS URL launch failed due to: " + e.getMessage());
        }
    }

    @And("The user enters the credentials")
    public void theUserEntersTheCredentials() throws IOException, InterruptedException {
        scormTestingPage.enterCredentials();
    }

    @And("The user enters {string} in {string}")
    public void theUserEnters(String IDField, String locator) throws InterruptedException {
        try {
            scormTestingPage.verifyAndEnterText(IDField, locator);
        } catch (Exception e) {
            logAssert_Fail("Error entering the text" + e.getMessage());
        }
    }

    @And("The user clicks on the {string}")
    public void theUserClicksOnTheButton(String locator) throws InterruptedException {
        try {
            scormTestingPage.clicks(locator);
            page.waitForLoadState();
        } catch (Exception e){
            logAssert_Fail("Error clicking on the button!" + e.getMessage() );
        }
    }

    @When("The user opens the SCORM")
    public void theUserOpensTheSCORM() throws InterruptedException {
        try{
            scormTestingPage.openSCORM();
        } catch (Exception e){
            logAssert_Fail("Error in launching the SCORM" + e.getMessage());
        }
    }

    @And("The user starts the course")
    public void theUserStartsTheCourse() throws InterruptedException {
        try{
            scormTestingPage.clickOnFrame();
        } catch (Exception e){
            logAssert_Fail("Error in starting the SCORM" + e.getMessage());
        }
    }

    @Then("The user verifies that the table of contents and progress bar is visible")
    public void theUserVerifiesThatTheTableOfContentsIsVisible() throws InterruptedException {
        try {
            scormTestingPage.tableOfContentVisibility();
        } catch (Exception e){
            logAssert_Fail("Table of Contents is not visible" + e.getMessage());
        }
    }

    @Then("The user completes the course partially")
    public void theUserCompletesTheCoursePartially() throws InterruptedException {
        try {
            scormTestingPage.completeTheCoursePartially();
        } catch (Exception e) {
            logAssert_Fail("Error in partially completing the course" + e.getMessage());
        }
    }

    @Then("The user verifies progress has been updated")
    public void theUserVerifiesProgressHasBeenUpdated() throws InterruptedException {
        try {
            scormTestingPage.validateProgressBarUpdate();
        } catch (Exception e) {
            logAssert_Fail("Progress bar not updated" + e.getMessage());
        }
    }

    @Then("The user completes the course")
    public void theUserCompletedTheCourse() {
        scormTestingPage.navigateThroughSCORM();
    }


}
