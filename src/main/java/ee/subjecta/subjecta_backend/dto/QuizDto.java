package ee.subjecta.subjecta_backend.dto;

import java.util.List;

public record QuizDto(
        String lessonId,
        List<QuestionDto> questions
) {}
