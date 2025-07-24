package com.lh.runner;

import com.lh.core.config.PropertiesHandler;
import com.lh.reportsfreemaker.ReportBuilder;
import com.lh.xray.Log;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@RunWith(Cucumber.class)

@CucumberOptions(monochrome = true,
        features = "src/test/java/com/lh/features/",
        dryRun = false,
        glue = {"com.lh.steps"},
//        tags = "@SCORMTesting",
//        tags = "@DEKRA",
        tags = "@IMCExpressSCORM",
        plugin = {"json:target/cucumber.json"})
public class JunitRunner {

    public final static String PATH_TO_CUCUMBER_REPORT = "target/cucumber.json";
    public static String testStart = "";
    public static String folderNameReport = "";
    //test
    public static String folderNameReport1 = "";

    @BeforeClass
    public static void setupBeforeClass() {

        setLogger();

        ZonedDateTime startDateTime = ZonedDateTime.now();
        testStart = startDateTime.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        Log.info("Test Start Time: " + testStart);

    }

    @AfterClass
    public static void teardown() throws Exception {
        try {
            ReportBuilder.generateReport();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void setLogger() {

        System.setProperty("log-directory", PropertiesHandler.getLogsFolder());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        System.setProperty("currenttime", dateFormat.format(new Date()));
        Log.info("Log configuration done. Log Dir:" + PropertiesHandler.getLogsFolder());

    }
}