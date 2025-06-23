package com.lh.core.report;

import com.lh.core.page.ScormData;
import com.microsoft.playwright.Page;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class SCORMReport {
    private static final Logger logger = LogManager.getLogger(SCORMReport.class);
    private static Page page;
    public static void generateHtmlReport(ScormData data) {
        try {
            // Step 1: Create "SCORM-Report" folder if it doesn't exist
            String reportFolderName = "SCORM-Report";
            File reportFolder = new File(reportFolderName);
            if (!reportFolder.exists()) {
                reportFolder.mkdir();
                logger.info("Created folder: " + reportFolder.getAbsolutePath());
            }

            // Step 2: Generate filename with timestamp
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE_MMM_dd_HH_mm_yyyy");
            ZonedDateTime indiaTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
            String timestamp = indiaTime.format(formatter);
            String reportFileName = "LHG-LMS-" + timestamp + "_IST.html";


            // Step 3: Full path for the new report
            File reportFile = new File(reportFolder, reportFileName);

            // Step 4: Write the HTML content
            try (PrintWriter writer = new PrintWriter(new FileWriter(reportFile))) {
                writer.println("<!DOCTYPE html>");
                writer.println("<html><head><meta charset='UTF-8'><title>SCORM Report</title>");
                writer.println("<style>");
                writer.println("body { font-family: Arial; margin: 20px; background: #f9f9f9; }");
                writer.println("table { width: 100%; border-collapse: collapse; background: #fff; }");
                writer.println("th, td { padding: 12px; border: 1px solid #ccc; }");
                writer.println("th { background-color: #007BFF; color: white; }");
                writer.println("tr:nth-child(even) { background-color: #f2f2f2; }");
                writer.println("h1 { color: #333; }");
                writer.println("</style></head><body>");
                writer.println("<h1>SCORM Data Report</h1>");
                writer.println("<table>");
                writer.println("<tr><th>Field</th><th>Value</th><th>Description</th></tr>");

                writer.printf("<tr><td>SCORM Version</td><td>%s</td><td>The version of SCORM (e.g., SCORM 1.2 or SCORM 2004) the content is using.</td></tr>%n", data.version);
                writer.printf("<tr><td>Student ID</td><td>%s</td><td>The unique identifier assigned to the student by the LMS.</td></tr>%n", data.studentId);
                writer.printf("<tr><td>Student Name</td><td>%s</td><td>The full name of the student using the course.</td></tr>%n", data.studentName);
                writer.printf("<tr><td>Objectives Count</td><td>%s</td><td>Total number of objectives (e.g., lessons or activities) defined in the course.</td></tr>%n", data.objectivesCount);
                writer.printf("<tr><td>Audio Preference</td><td>%s</td><td>The student’s preferred audio volume level (e.g., 0 = off, 100 = full volume).</td></tr>%n", data.audioPref);
                writer.printf("<tr><td>Lesson Location</td><td>%s</td><td>The last known location the student visited in the course.</td></tr>%n", data.lessonLocation);
                writer.printf("<tr><td>Lesson Status</td><td>%s</td><td>Shows if the student has completed the lesson (e.g., completed, incomplete, not attempted).</td></tr>%n", data.lessonStatus);
                writer.printf("<tr><td>Lesson Mode</td><td>%s</td><td>Indicates the mode (e.g., normal, review, browse) the course is being accessed in.</td></tr>%n", data.lessonMode);
                writer.printf("<tr><td>Session Time</td><td>%s</td><td>Total time the student has spent in the current session.</td></tr>%n", data.sessionTime);
                writer.printf("<tr><td>Suspend Data</td><td>%s</td><td>Saved state information used to resume the course (e.g., last visited slide, completed sections).</td></tr>%n", data.suspendData);
                writer.printf("<tr><td>Score</td><td>%s</td><td>Displays the score (if available).</td></tr>%n", data.rawScore);
                writer.println("</table></body></html>");
            }

            logger.info("SCORM report saved at: " + reportFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }

        // SCORM session termination logic
        try {
            Object result = page.evaluate("() => { " +
                    "try { return API.LMSFinish(''); } catch (e) { return false; } " +
                    "}");
            Boolean finished = (result instanceof Boolean) ? (Boolean) result : false;
            if (!finished) {
                logger.info("Warning: Failed to properly terminate SCORM session");
            }
        } catch (Exception e) {
            logger.error("Error terminating SCORM session: " + e.getMessage());
        }
    }
}
