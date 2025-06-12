package com.lh.utilities;

public class Configurations {

	// DriverPaths and Run on browser parameter
	public static final String ExecutionEnvnmt = "QA"; // Valid values: QA, Stage, PROD
	public static final String BrowserName = "Chrome";


	// Application URL
	public static final String LHG_LMS_URL = "https://lms-test.lufthansa.com/?client=admin";
	// Test Data source path
	public final static String testDataResourcePath = "../src/test/java/com/TestData/";

	// Browser Stack configuration
	public static final String RunOnBrowserStack = "N";
	public static final String USERNAME = "";

	public static final String AUTOMATE_KEY = "5tW8jrFVdPxbpgUSvssc";

	public static final String URL_BS = "https://" + USERNAME + ":" + AUTOMATE_KEY
			+ "@hub-cloud.browserstack.com/wd/hub";
	public static boolean cloud = false;

	// Output Reports path
	public static final String reportPath = "./Reports/";

	// download file path
	public static String downloadPath = System.getProperty("user.dir") + "\\Downloads\\";

	// Take screenshots on run parameter settings.
	public static final String takeScreenshots = "Y";
	//screenshot
	public static final Boolean TakeScreenshotAfterEachStep = false;
	/* set value of takeScreenshotAtEnd to 'false' if takeScreenshotAfterEachStep is set to 'true' otherwise 2 screenshots will get captured at the end step*/
	public static final Boolean TakeScreenshotAtEnd = false;

	public final static String XrayConfigPath = "./src/test/java/com/lh/xray/xray_config.properties";

}
