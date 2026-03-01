package ee.subjecta.subjecta_backend.dto;

import java.util.List;

public record QuestionDto(
        String id,
        String text,
        List<String> options
) {}
