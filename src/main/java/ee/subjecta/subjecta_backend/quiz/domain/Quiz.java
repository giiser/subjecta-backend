package ee.subjecta.subjecta_backend.quiz.domain;

import java.util.List;

public record Quiz(
        String lessonId,
        List<Question> questions
) {}