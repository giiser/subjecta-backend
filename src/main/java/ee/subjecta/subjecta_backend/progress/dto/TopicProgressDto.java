package ee.subjecta.subjecta_backend.progress.dto;

public record TopicProgressDto(
        String topicId,
        int totalLessons,
        int completedLessons,
        double percentage
) {}
