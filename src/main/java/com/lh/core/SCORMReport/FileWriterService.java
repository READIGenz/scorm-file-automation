package com.lh.core.SCORMReport;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FileWriterService {
    private static final Logger logger = Logger.getLogger(FileWriterService.class);

    public File writeReportToFile(String folderName, String prefix, String content) throws Exception {
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdir();
            logger.info("Created folder: " + folder.getAbsolutePath());
        }

        String timestamp = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"))
                .format(DateTimeFormatter.ofPattern("EEE_MMM_dd_HH_mm_yyyy"));
        String fileName = prefix + "-" + timestamp + "_IST.html";

        File reportFile = new File(folder, fileName);

        try (PrintWriter writer = new PrintWriter(new FileWriter(reportFile))) {
            writer.write(content);
        }

        logger.info("Report saved at: " + reportFile.getAbsolutePath());
        return reportFile;
    }
}
