package com.lh.core.utilities;

public class Configurations {

	// Application URL
	public static final String LHG_LMS_URL = "https://lms-test.lufthansa.com/?client=admin";
	// Output Reports path
	public static final String reportPath = "./Reports/";

	//Screenshot
	public static final Boolean TakeScreenshotAfterEachStep = false;
	/* set value of takeScreenshotAtEnd to 'false' if takeScreenshotAfterEachStep is set to 'true' otherwise 2 screenshots will get captured at the end step*/
	public static final Boolean TakeScreenshotAtEnd = false;

	public final static String XrayConfigPath = "./src/test/java/com/lh/xray/xray_config.properties";

}
