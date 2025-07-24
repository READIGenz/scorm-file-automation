package com.lh.steps;

import com.lh.core.page.BasePage;
import com.lh.core.utils.DriverManager;
import com.lh.utilities.Configurations;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.AfterStep;
import cucumber.api.java.Before;

public class GenericHooks extends BasePage {

	@Before
	public void before(Scenario scenario) {
		System.out.println("Scenario name: " + scenario.getName());
		setScenario(scenario);
	}

	@After
	public void after(Scenario scenario) {
		if(Configurations.TakeScreenshotAtEnd && !isScreenshotCapturedAfterFailure) {
			takeScreenshot();
		}
		DriverManager.tearDown();
	}

	@AfterStep
	public void afterStep(Scenario scenario) {
		if (Configurations.TakeScreenshotAfterEachStep && !isScreenshotCapturedAfterFailure) {
			takeScreenshot();
		}
	}
}