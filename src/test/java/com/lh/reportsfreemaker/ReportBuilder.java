package com.lh.reportsfreemaker;

import com.lh.runner.JunitRunner;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Entry class for the report generator
 */
public class ReportBuilder {

	public static void generateReport() throws Exception {

		TimeUnit.SECONDS.sleep(5);

		Date currentDate = new Date();
//		te.setExecutionDate(currentDate.toString());
		JunitRunner.folderNameReport = "LHG-LMS" + currentDate.toString().replace(":", "_").replace(" ", "_");
		JunitRunner.folderNameReport1 = "LHG-LMS_";

		ReportGenerator rg = new ReportGenerator();
		rg.generateReport(JunitRunner.PATH_TO_CUCUMBER_REPORT, JunitRunner.folderNameReport1);
		rg.generateReport(JunitRunner.PATH_TO_CUCUMBER_REPORT, JunitRunner.folderNameReport);

	}
}
