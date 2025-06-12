package com.lh.core.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class McqAnswerLoader {
    private static JsonNode mcqAnswers;

    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mcqAnswers = mapper.readTree(new File("src/test/java/com/lh/testdata/mcq-answers.json"));
        } catch (IOException e) {
            System.err.println("Failed to load mcq-answers.json: " + e.getMessage());
        }
    }

    public static String getCurrentCourseName() {
        return mcqAnswers.path("currentCourse").asText();
    }

    public static List<String> getAnswers(String course, String question) {
        List<String> answers = new ArrayList<>();
        JsonNode node = mcqAnswers.path(course).path(question);
        if (node.isArray()) {
            node.forEach(jsonNode -> answers.add(jsonNode.asText()));
        }
        return answers;
    }
}
