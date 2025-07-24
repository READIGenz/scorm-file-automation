package com.lh.steps;

import com.lh.core.page.BasePage;
import com.lh.core.page.SCORMTestingPage;
import com.lh.core.utils.DriverManager;
import com.lh.utilities.Configurations;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

import java.io.IOException;

public class SCORMTestingSteps extends BasePage {

    SCORMTestingPage scormTestingPage = new SCORMTestingPage();


    @Given("The user launches the LMS on {string}")
    public void theUserLaunchesTheLMSOn(String browserName) throws InterruptedException {
        try {
            DriverManager.setUp(browserName);
            System.out.println("Browser initialized: " + (DriverManager.getBrowser() != null));
            launchBrowser(Configurations.LHG_LMS_URL);
        } catch (Exception e) {
            logAssert_Fail("LMS URL launch failed due to: " + e.getMessage());
        }
    }

    @And("The user performs login as a learner")
    public void theUserPerformsLoginAsLearner() throws IOException, InterruptedException {
        try {
            scormTestingPage.loginAsLearner();
        } catch (Exception e){
            logAssert_Fail("Error in login due to: " + e.getMessage());
        }

    }

    @And("The user navigates to the SCORM")
    public void theUserNavigatesToTheSCORM() throws InterruptedException {
        try {
            scormTestingPage.navigatesTillSCORM();
        } catch (Exception e) {
            logAssert_Fail("Error due to " + e.getMessage());
        }
    }

//    @And("The user navigates to the SCORM")
//    public void theUserNavigatesToTheSCORM() {
//    }
//
//    @And("The user enters {string} in {string}")
//    public void theUserEnters(String IDField, String locator) throws InterruptedException {
//        try {
//            scormTestingPage.verifyAndEnterText(IDField, locator);
//        } catch (Exception e) {
//            logAssert_Fail("Error entering the text" + e.getMessage());
//        }
//    }

//    @And("The user clicks on the {string}")
//    public void theUserClicksOnTheButton(String locator) throws InterruptedException {
//        try {
//            scormTestingPage.clicks(locator);
//            page.waitForLoadState();
//        } catch (Exception e){
//            logAssert_Fail("Error clicking on the button!" + e.getMessage() );
//        }
//    }

    @When("The user opens the SCORM")
    public void theUserOpensTheSCORM() throws InterruptedException {
        try{
            scormTestingPage.openSCORM();
//            SCORMLaunchStatus();
        } catch (Exception e){
            logAssert_Fail("Error in launching the SCORM" + e.getMessage());
        }
    }



}
