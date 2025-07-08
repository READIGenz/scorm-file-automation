package com.lh.reportsfreemaker;

import com.github.mkolisnyk.cucumber.reporting.CucumberDetailedResults;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Detailed report of the tests. This will contain detailed steps of each test
 * cases(both failed and passed).
 */
public class DetailedReport implements Report {
	private final static Logger LOGGER = Logger.getLogger(DetailedReport.class);

	private static final String SCREEN_SHOT_WIDTH = "300px";
	private final String targetDirectoryPath;
	private final String outputName;
	private final String templatePath;
	private final String sourceName;
	private final String screenShotLocation;

	/**
	 * @param targetDirectoryPath Target directory path where the report file will
	 *                            be generated. This field is mandatory
	 * @param outputName          File name of the report to be generated. This
	 *                            field is mandatory
	 * @param templatePath        custom template file to be used for report
	 *                            generation. This field is not mandatory
	 * @param sourceName          Input file that should be used to generate the
	 *                            report. This field is mandatory
	 * @param screenShotLocation  The location of the screenshots taken during the
	 *                            testing. This field is mandatory
	 */
	public DetailedReport(String targetDirectoryPath, String outputName, String templatePath, String sourceName,
			String screenShotLocation) {

		Validate.notEmpty(targetDirectoryPath, "Target directory path should not be null. Please provide a valid path");
		Validate.notEmpty(outputName, "Output name of the report is empty. Please provide a valid name");
		Validate.notEmpty(sourceName, "Source name for the report is empty. Please provide a valid path");
		Validate.notEmpty(screenShotLocation,
				"Screenshots location for the report is empty. Please provide a valid path");

		this.targetDirectoryPath = targetDirectoryPath;
		this.outputName = outputName;
		this.templatePath = templatePath;
		this.sourceName = sourceName;
		this.screenShotLocation = screenShotLocation;
	}

	@Override
	public String generateReport() throws Exception {
		LOGGER.info("Generating DetailedReport started" + toString());

		CucumberDetailedResults resultsExtended = new CucumberDetailedResults();
		resultsExtended.setOutputDirectory(this.targetDirectoryPath);
		resultsExtended.setOutputName(this.outputName);
		resultsExtended.setSourceFile(this.sourceName);
		resultsExtended.setScreenShotLocation(this.screenShotLocation);
		resultsExtended.setScreenShotWidth(SCREEN_SHOT_WIDTH);
		resultsExtended.setTemplatesLocation(this.templatePath);
		resultsExtended.execute();

		LOGGER.info("Generating DetailedReport completed " + resultsExtended.getOutputHtmlFile().getCanonicalPath());
		return resultsExtended.getOutputHtmlFile().getPath();
	}

	@Override
	public String toString() {
		return "DetailedReport{" + "targetDirectoryPath='" + targetDirectoryPath + '\'' + ", outputName='" + outputName
				+ '\'' + ", templatePath='" + templatePath + '\'' + ", sourceName='" + sourceName + '\''
				+ ", screenShotLocation='" + screenShotLocation + '\'' + '}';
	}
}
