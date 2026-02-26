package ee.subjecta.subjecta_backend.dto;

import java.util.List;

public record QuizDto(
        String id,
        String lessonId,
        String question,
        List<String> options
) {
}
