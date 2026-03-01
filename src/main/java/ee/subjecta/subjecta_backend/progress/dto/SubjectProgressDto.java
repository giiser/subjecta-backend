package ee.subjecta.subjecta_backend.progress.dto;

public record SubjectProgressDto(
        String subjectId,
        int totalLessons,
        int completedLessons,
        double percentage
) {}
