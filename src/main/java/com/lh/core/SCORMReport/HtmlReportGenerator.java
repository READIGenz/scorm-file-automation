package com.lh.core.SCORMReport;

import com.lh.core.SCORMReport.model.ScormData;

public class HtmlReportGenerator implements ReportGenerator {
    @Override
    public String generate(ScormData data) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>SCORM Report</title>")
                .append("<style>")
                .append("body { font-family: Arial; margin: 20px; background: #f9f9f9; }")
                .append("table { width: 100%; border-collapse: collapse; background: #fff; }")
                .append("th, td { padding: 12px; border: 1px solid #ccc; }")
                .append("th { background-color: #007BFF; color: white; }")
                .append("tr:nth-child(even) { background-color: #f2f2f2; }")
                .append("</style></head><body>")
                .append("<h1>SCORM Data Report</h1><table>")
                .append("<tr><th>Field</th><th>Value</th><th>Description</th></tr>");

        html.append(formatRow("SCORM Version", data.version, "The version of SCORM (e.g., SCORM 1.2 or SCORM 2004) the content is using."))
                .append(formatRow("Student ID", data.studentId, "The unique identifier assigned to the student by the LMS."))
                .append(formatRow("Student Name", data.studentName, "The full name of the student using the course."))
                .append(formatRow("Objectives Count", data.objectivesCount, "Total number of objectives (e.g., lessons or activities) defined in the course."))
                .append(formatRow("Audio Preference", data.audioPref, "The student’s preferred audio volume level."))
                .append(formatRow("Lesson Location", data.lessonLocation, "The last known location the student visited."))
                .append(formatRow("Lesson Status", data.lessonStatus, "Shows if the student has completed the lesson."))
                .append(formatRow("Lesson Mode", data.lessonMode, "Indicates the mode the course is being accessed in."))
                .append(formatRow("External Links", data.externalLinks, "Any external URLs or links detected during SCORM session."))
                .append("</table></body></html>");

        return html.toString();
    }

    private String formatRow(String field, String value, String description) {
        return String.format("<tr><td>%s</td><td>%s</td><td>%s</td></tr>", field, value, description);
    }
}
