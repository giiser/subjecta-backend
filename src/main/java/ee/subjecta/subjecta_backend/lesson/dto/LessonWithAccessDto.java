package ee.subjecta.subjecta_backend.lesson.dto;

import ee.subjecta.subjecta_backend.dto.LessonDto;

public record LessonWithAccessDto(
        LessonDto lesson,
        boolean locked
) {}
