package ee.subjecta.subjecta_backend.dto;

public record LessonDto(
        String id,
        String topicId,
        String title,
        String summary,
        String content
) {
}
