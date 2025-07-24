package com.lh.core.utils;

import com.lh.core.page.DEKRAScormTestingPage;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

public class TestAutomationReportZipHandler implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(TestAutomationReportZipHandler.class);
    private static final Path REPORTS_DIR = Paths.get(System.getProperty("user.dir"), "Reports", "LHG-LMS_");

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.info("📥 Request received for latest test report ZIP");

        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            logger.error("❌ Invalid HTTP method: " + exchange.getRequestMethod());
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        logger.info("🔍 Searching for latest test report folder in: " + REPORTS_DIR);
        File latestFolder = getLatestReportFolder();
        if (latestFolder == null) {
            String msg = "❌ No test report folders found.";
            logger.info(msg);
            exchange.sendResponseHeaders(404, msg.getBytes().length);
            exchange.getResponseBody().write(msg.getBytes());
            exchange.close();
            return;
        }

        logger.info("📁 Latest report folder found: " + latestFolder.getName());

        Path zipPath = Paths.get(latestFolder.getAbsolutePath() + ".zip");
        logger.info("🗜️ Zipping folder to: " + zipPath);

        try {
            zipFolder(latestFolder.toPath(), zipPath);
        } catch (IOException e) {
            String msg = "❌ Error while creating ZIP: " + e.getMessage();
            System.err.println(msg);
            e.printStackTrace();
            exchange.sendResponseHeaders(500, msg.getBytes().length);
            exchange.getResponseBody().write(msg.getBytes());
            exchange.close();
            return;
        }

        logger.info("✅ ZIP created successfully. Preparing to send...");

        byte[] fileBytes = Files.readAllBytes(zipPath);
        exchange.getResponseHeaders().add("Content-Type", "application/zip");
        exchange.getResponseHeaders().add("Content-Disposition", "attachment; filename=\"" + zipPath.getFileName() + "\"");

        exchange.sendResponseHeaders(200, fileBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(fileBytes);
            logger.info("✅ ZIP file sent: " + zipPath.getFileName());
        }

        // Cleanup
        boolean deleted = Files.deleteIfExists(zipPath);
        logger.info(deleted ? "🧹 Temporary ZIP deleted." : "⚠️ Failed to delete ZIP: " + zipPath);
    }

    private File getLatestReportFolder() {
        File baseDir = REPORTS_DIR.toFile();
        File[] dirs = baseDir.listFiles(file ->
                file.isDirectory() && !file.getName().equalsIgnoreCase("images"));

        if (dirs == null || dirs.length == 0) {
            logger.warn("🚫 No report directories found in: " + baseDir.getAbsolutePath());
            return null;
        }

        logger.info("📊 Found " + dirs.length + " candidate folders for report.");
        Arrays.stream(dirs).forEach(dir ->
                logger.info("🗂️ Candidate: " + dir.getName() + " | Last Modified: " + new Date(dir.lastModified())));

        return Arrays.stream(dirs)
                .max(Comparator.comparingLong(File::lastModified))
                .orElse(null);
    }

    private void zipFolder(Path sourceDir, Path zipPath) throws IOException {
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            Files.walk(sourceDir)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourceDir.relativize(path).toString());
                        try {
                            logger.info("📦 Adding to ZIP: " + path);
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException e) {
                            logger.error("❌ Error zipping file: " + path);
                            e.printStackTrace();
                        }
                    }
                    );
        }
    }
}
