package ee.subjecta.subjecta_backend.dto;

public record TopicDto(
        String id,
        String subjectId,
        String title,
        String description,
        Integer order
) {
}
