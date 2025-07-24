package com.lh.core.utils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class SCORMReportHandler implements HttpHandler {
    private static final Logger logger = LogManager.getLogger(SCORMReportHandler.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.info("📥 Request received: " + exchange.getRequestURI());

        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            logger.error("❌ Invalid method: " + exchange.getRequestMethod());
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        // Path to SCORM-Report directory
        Path reportDir = Paths.get(System.getProperty("user.dir"), "SCORM-Report");
        logger.info("📂 Looking for latest report in: " + reportDir.toAbsolutePath());

        if (!Files.exists(reportDir) || !Files.isDirectory(reportDir)) {
            logger.error("❌ Report directory not found");
            sendError(exchange, 404, "SCORM report directory not found.");
            return;
        }

        // Find the latest *.html file based on filename timestamp
        try (Stream<Path> files = Files.list(reportDir)) {
            Optional<Path> latestReport = files
                    .filter(path -> path.toString().endsWith(".html"))
                    .max(Comparator.comparing(path -> path.toFile().lastModified()));

            if (latestReport.isEmpty()) {
                logger.error("❌ No HTML reports found.");
                sendError(exchange, 404, "No SCORM reports available.");
                return;
            }

            Path latestReportPath = latestReport.get();
            byte[] fileBytes = Files.readAllBytes(latestReportPath);

            exchange.getResponseHeaders().add("Content-Type", "text/html");
            exchange.getResponseHeaders().add("Content-Disposition",
                    "attachment; filename=\"" + latestReportPath.getFileName().toString() + "\"");

            exchange.sendResponseHeaders(200, fileBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(fileBytes);
            }

            logger.info("✅ Sent latest SCORM report: " + latestReportPath.getFileName());

        } catch (IOException e) {
            e.printStackTrace();
            sendError(exchange, 500, "Error while retrieving report.");
        }
    }

    private void sendError(HttpExchange exchange, int code, String message) throws IOException {
        exchange.sendResponseHeaders(code, message.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(message.getBytes());
        }
    }
}