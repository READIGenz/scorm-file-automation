package com.lh.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SimpleHttpJsonServer {

    private static final Logger logger = LogManager.getLogger(SimpleHttpJsonServer.class);

    public static void main(String[] args) throws Exception {
        logger.info("🚀 HTTP Server is starting...");
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        logger.info("🛠️ Initializing server contexts...");

        server.createContext("/", new StaticDirHandler("src/main/resources/static"));
        server.createContext("/submit", SimpleHttpJsonServer::handleSubmit);
        server.createContext("/download/latest-report", new SCORMReportHandler());
        server.createContext("/download/latest-zip", new TestAutomationReportZipHandler());

        server.setExecutor(null);
        logger.info("✅ Server started at: http://localhost:8080");
        server.start();
    }

    public static class StaticDirHandler implements HttpHandler {
        private final String baseDir;

        public StaticDirHandler(String baseDir) {
            this.baseDir = baseDir;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String uriPath = exchange.getRequestURI().getPath();
            if (uriPath.equals("/") || uriPath.isEmpty()) {
                uriPath = "/index.html";
            }

            File file = new File(baseDir, uriPath);
            if (!file.exists() || file.isDirectory()) {
                String response = "404 (Not Found)\n";
                exchange.sendResponseHeaders(404, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
                return;
            }

            String contentType = guessContentType(file.getName());
            exchange.getResponseHeaders().set("Content-Type", contentType);

            byte[] bytes = Files.readAllBytes(file.toPath());
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

        private String guessContentType(String fileName) {
            if (fileName.endsWith(".html")) return "text/html";
            if (fileName.endsWith(".css")) return "text/css";
            if (fileName.endsWith(".js")) return "application/javascript";
            return "application/octet-stream";
        }
    }

    private static void handleSubmit(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)
            );
            StringBuilder formData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                formData.append(line);
            }

            Map<String, String> parsed = parseForm(formData.toString());
            String scormName = parsed.get("scormName");
            String scormAnswers = parsed.get("scormAnswers");
            String vendorName = parsed.get("vendorName");

            if (scormName == null || scormName.trim().isEmpty()) {
                throw new IllegalArgumentException("❌ SCORM Name is required.");
            }

            logger.info("📦 Received SCORM: {}");
            logger.info("🏷️ Vendor Tag: @{}");

            Map<String, Object> finalData = new LinkedHashMap<>();
            Map<String, List<String>> courseAnswers = parseAnswersFormatted(scormAnswers);
            finalData.put("currentCourse", scormName);
            finalData.put(scormName, courseAnswers);

            Path filePath = Paths.get("src/test/java/com/lh/testdata/mcq-answers.json");
            Files.createDirectories(filePath.getParent());
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), finalData);

            // Run JUnit tests with vendor tag
            String tag = "@" + vendorName.trim();
            ProcessBuilder pb = new ProcessBuilder("mvn.cmd", "test", "-Dcucumber.filter.tags=" + tag);
            pb.directory(new File("."));
            pb.redirectErrorStream(true);
            logger.info("🚀 Running tests with tag: " + tag);

            Process process = pb.start();
            BufferedReader processReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)
            );

            while ((line = processReader.readLine()) != null) {
                logger.info("[mvn]" + line);
            }

            int exitCode = process.waitFor();
            boolean success = (exitCode == 0);

            Map<String, String> responseMap = new LinkedHashMap<>();
            responseMap.put("message", success ? "✅ Tests passed!" : "⚠️ Tests failed. Check reports.");

            String jsonResponse = new ObjectMapper().writeValueAsString(responseMap);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
            exchange.getResponseBody().write(jsonResponse.getBytes());
            exchange.getResponseBody().close();

        } catch (Exception ex) {
            logger.error("❌ Error occurred during /submit: ", ex);

            String errorResponse = "{\"error\": \"" + ex.getMessage().replace("\"", "'") + "\"}";
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(500, errorResponse.getBytes().length);
            exchange.getResponseBody().write(errorResponse.getBytes());
            exchange.getResponseBody().close();
        }
    }

    private static Map<String, String> parseForm(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            map.put(URLDecoder.decode(kv[0], "UTF-8"),
                    kv.length > 1 ? URLDecoder.decode(kv[1], "UTF-8") : "");
        }
        return map;
    }

    private static Map<String, List<String>> parseAnswersFormatted(String input) {
        Map<String, List<String>> questionMap = new LinkedHashMap<>();
        String[] blocks = input.split("\\n\\n");
        int questionNumber = 1;

        for (String block : blocks) {
            String[] lines = block.trim().split("\\n");
            List<String> answers = new ArrayList<>();

            for (String line : lines) {
                line = line.trim();
                if (!line.startsWith("ANS=") && !line.isEmpty()) {
                    if (!line.matches("^\\d+\\.\\s*.*")) continue;
                    answers.add(line.replaceFirst("^\\d+\\.\\s*", ""));
                }
            }

            if (!answers.isEmpty()) {
                questionMap.put(String.valueOf(questionNumber), answers);
                questionNumber++;
            }
        }

        return questionMap;
    }
}
