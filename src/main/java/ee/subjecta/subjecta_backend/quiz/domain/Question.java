package ee.subjecta.subjecta_backend.quiz.domain;

import java.util.List;

public record Question(
        String id,
        String text,
        List<String> options,
        int correctAnswerIndex
) {}
