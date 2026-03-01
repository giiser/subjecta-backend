package ee.subjecta.subjecta_backend.dto;

import java.util.Map;

public record QuizSubmissionDto(
        Map<String, Integer> answers
        // key = questionId
        // value = selected option index
) {}
