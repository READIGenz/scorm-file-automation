package com.lh.core.SCORMReport;

import com.lh.core.SCORMReport.model.ScormData;

public class SCORMReportService {
    private final ReportGenerator reportGenerator;
    private final FileWriterService fileWriterService;

    public SCORMReportService(ReportGenerator generator, FileWriterService writerService) {
        this.reportGenerator = generator;
        this.fileWriterService = writerService;
    }

    public void generateAndSave(ScormData data) {
        try {
            String content = reportGenerator.generate(data);
            fileWriterService.writeReportToFile("SCORM-Report", "LHG-LMS", content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
