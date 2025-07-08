package com.lh.reportsfreemaker;

import com.github.mkolisnyk.cucumber.reporting.CucumberResultsOverview;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Overview report which will contain the following details
 * <ul>
 * <li>Two pie charts representing the overall status</li>
 * <li>Overall summart table</li>
 * <li>Features Status</li>
 * <li>Scenario Status</li>
 * </ul>
 */
public class OverviewReport implements Report {
	private final static Logger LOGGER = Logger.getLogger(OverviewReport.class);
	private final String targetDirectoryPath;
	private final String outputName;
	private final String templatePath;
	private final String sourceName;

	/**
	 * Constructs the Result overview report with specified parameters.
	 *
	 * @param targetDirectoryPath Target directory path where the report file will
	 *                            be generated. This field is mandatory
	 * @param outputName          File name of the report to be generated. This
	 *                            field is mandatory
	 * @param templatePath        custom template file to be used for report
	 *                            generation. This field is not mandatory
	 * @param sourceName          Input file that should be used to generate the
	 *                            report. This field is mandatory
	 */

	public OverviewReport(String targetDirectoryPath, String outputName, String templatePath, String sourceName) {

		Validate.notEmpty(targetDirectoryPath, "Target directory path should not be null. Please provide a valid path");
		Validate.notEmpty(outputName, "Output name of the report is empty. Please provide a valid name");
		Validate.notEmpty(sourceName, "Source name for the report is empty. Please provide a valid path");

		this.targetDirectoryPath = targetDirectoryPath;
		this.outputName = outputName;
		this.templatePath = templatePath;
		this.sourceName = sourceName;
	}

	@Override
	public String generateReport() throws Exception {
		LOGGER.info("Generating OverviewReport started" + toString());

		CucumberResultsOverview results = new CucumberResultsOverview();

		results.setOutputDirectory(this.targetDirectoryPath);
		results.setOutputName(this.outputName);
		results.setSourceFile(this.sourceName);

		File file1 = new File("./custom_templates/consolidated_template.ftlh");
		String str1 = FileUtils.readFileToString(file1, StandardCharsets.UTF_8);

		String html1 = str1.replace("_place_holder_project_name_", "LHG-LMS");
		PrintWriter writer1 = new PrintWriter("./custom_templates/consolidated.ftlh", "UTF-8");
		writer1.println(html1);
		writer1.close();

		File file2 = new File("./custom_templates/overview_test_coverage.ftlh");
		String str2 = FileUtils.readFileToString(file2, StandardCharsets.UTF_8);
//		System.out.println(str2);
//		String totalStory = Integer.toString(tc.getstoryTotal() - tc.getstoryWithFunctionalTestTotal());
//		String totalCoverdStory = Integer.toString(tc.getstoryWithFunctionalTestTotal());
//		String totalTests = Integer.toString(tc.getTestTotal() - tc.getAutoTestTotal());
//		String totalAutoTests = Integer.toString(tc.getAutoTestTotal());
//		String testEnvironment = te.getTestEnvironment();
//		String testComponent = te.getTestComponent();
//		String testExecutionDate = te.getExecutionDate();
//		String testReleaseName = te.getReleaseName();

		String totalStory = "0";
		String totalCoverdStory = "1";
		String totalTests = "0";
		String totalAutoTests = "1";
		String testEnvironment = "stage";
		String testComponent = "LHG-LMS";
		String testExecutionDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
		String testReleaseName = "LHG-LMS";

		String html2 = str2.replace("_place_holder_release_name_", testReleaseName)
				.replace("_place_holder_test_execution_date_", testExecutionDate)
				.replace("_place_holder_component_", testComponent)
				.replace("_place_holder_environment_", testEnvironment).replace("___stories___", totalStory)
				.replace("___covered_stories___", totalCoverdStory).replace("___tests___", totalTests)
				.replace("___autotests___", totalAutoTests);
		PrintWriter writer2 = new PrintWriter("./custom_templates/overview.ftlh", "UTF-8");
		writer2.println(html2);
		writer2.close();

		if (this.templatePath != null) {
			results.setTemplatesLocation(this.templatePath);
		}

		results.execute();
		File outputHtmlFile = results.getOutputHtmlFile();
		LOGGER.info("Generating DetailedReport completed " + outputHtmlFile.getCanonicalPath());

		return outputHtmlFile.getPath();
	}

	@Override
	public String toString() {
		return "OverviewReport{" + "targetDirectoryPath='" + targetDirectoryPath + '\'' + ", outputName='" + outputName
				+ '\'' + ", templatePath='" + templatePath + '\'' + ", sourceName='" + sourceName + '\'' + '}';
	}
}
