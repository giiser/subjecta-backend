package ee.subjecta.subjecta_backend.dto;

public record QuizResultDto(
        int totalQuestions,
        int correctAnswers,
        double percentage,
        boolean passed
) {}