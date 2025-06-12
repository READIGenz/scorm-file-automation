package com.lh.reportsfreemaker;

import com.github.mkolisnyk.cucumber.reporting.CucumberConsolidatedReport;
import com.github.mkolisnyk.cucumber.reporting.types.consolidated.ConsolidatedItemInfo;
import com.github.mkolisnyk.cucumber.reporting.types.consolidated.ConsolidatedReportBatch;
import com.github.mkolisnyk.cucumber.reporting.types.consolidated.ConsolidatedReportModel;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Arrays;

/**
 * Report with two or more reports into one .
 */
public class OverviewDetailedCombinedReport implements Report {
	private final static Logger LOGGER = Logger.getLogger(OverviewDetailedCombinedReport.class);

	private static final String PDF_PAGE_SIZE = "A4 landscape";
	private static final String[] FORMATS = { "html" };

	private final String targetDirectoryPath;
	private final String outputName;
	private final String templatePath;
	private final String sourceName;
	private final ConsolidatedItemInfo[] itemInfos;

	/**
	 * Constructs the report t with specified parameters.
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
	public OverviewDetailedCombinedReport(String targetDirectoryPath, String outputName, String templatePath,
			String sourceName, ConsolidatedItemInfo... itemInfos) {

		Validate.notEmpty(targetDirectoryPath, "Target directory path should not be null. Please provide a valid path");
		Validate.notEmpty(outputName, "Output name of the report is empty. Please provide a valid name");
		Validate.notEmpty(sourceName, "Source name for the report is empty. Please provide a valid path");
		Validate.notNull(itemInfos, "itemInfos is empty. Please provide a value");
		Validate.notEmpty(itemInfos, "There should be at-least one item in itemInfos. ");

		this.targetDirectoryPath = targetDirectoryPath;
		this.outputName = outputName;
		this.templatePath = templatePath;
		this.sourceName = sourceName;
		this.itemInfos = itemInfos;

	}

	@Override
	public String generateReport() throws Exception {
		LOGGER.info("Generating OverviewDetailedCombinedReport started" + toString());

		CucumberConsolidatedReport resultCucumberConsolidatedReport = new CucumberConsolidatedReport();
		resultCucumberConsolidatedReport.setOutputDirectory(targetDirectoryPath);
		resultCucumberConsolidatedReport.setOutputName(outputName);
		resultCucumberConsolidatedReport.setPdfPageSize(PDF_PAGE_SIZE);
		resultCucumberConsolidatedReport.setSourceFile(sourceName);
		if (this.templatePath != null) {
			resultCucumberConsolidatedReport.setTemplatesLocation(templatePath);
		}

		ConsolidatedReportModel reportModel = new ConsolidatedReportModel(this.itemInfos, "", "Overall Results", false);
		ConsolidatedReportModel[] reportModels = { reportModel };
		ConsolidatedReportBatch reportBatch = new ConsolidatedReportBatch(reportModels);
		resultCucumberConsolidatedReport.execute(reportBatch, true, FORMATS);

		File outputHtmlFile = resultCucumberConsolidatedReport.getOutputHtmlFile();
		LOGGER.info("Generating OverviewDetailedCombinedReport completed " + outputHtmlFile.getCanonicalPath());

		return outputHtmlFile.getPath();
	}

	@Override
	public String toString() {
		return "OverviewDetailedCombinedReport{" + "targetDirectoryPath='" + targetDirectoryPath + '\''
				+ ", outputName='" + outputName + '\'' + ", templatePath='" + templatePath + '\'' + ", sourceName='"
				+ sourceName + '\'' + ", itemInfos=" + Arrays.toString(itemInfos) + '}';
	}
}
