package com.lh.steps;

import com.lh.core.page.BasePage;
import com.lh.core.page.IMCExpressScormTestingPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

public class IMCExpressScormTestingSteps extends BasePage {

    IMCExpressScormTestingPage imcExpressScormTestingPage = new IMCExpressScormTestingPage();

    @Then("The user verifies the presence of external links")
    public void theUserVerifiesThePresenceOfExternalLinks() throws InterruptedException {
        imcExpressScormTestingPage.verifyNoExternalResources();
    }

//    @Then("User verifies SCORM API report")
//    public void userVerifiesSCORMAPIReport() {
////        scormTesting();
//    }

    @And("The user starts the IMCExpress SCORM course")
    public void theUserStartsTheIMCExpressSCORMCourse() {
        imcExpressScormTestingPage.userStartsIMCExpressCourse();
    }

    @And("The user navigates through the course")
    public void theUserNavigatesThroughTheCourse() throws InterruptedException {
        imcExpressScormTestingPage.courseNavigation();
    }


}
