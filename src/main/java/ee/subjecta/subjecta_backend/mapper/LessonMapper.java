package ee.subjecta.subjecta_backend.mapper;

import ee.subjecta.subjecta_backend.client.contentful.model.ContentfulEntry;
import ee.subjecta.subjecta_backend.dto.LessonDto;

import java.util.Map;

public class LessonMapper {

    public static LessonDto toDto(
            ContentfulEntry lesson,
            Map<String, ContentfulEntry> referenceIndex
    ) {
        String topicId = extractReferenceId(lesson, "topic");

        return new LessonDto(
                lesson.sys().id(),
                topicId,
                (String) lesson.fields().get("title"),
                (String) lesson.fields().get("summary"),
                (String) lesson.fields().get("content")
        );
    }

    private static String extractReferenceId(
            ContentfulEntry entry,
            String fieldName
    ) {
        Map<String, Object> ref =
                (Map<String, Object>) entry.fields().get(fieldName);

        Map<String, Object> sys =
                (Map<String, Object>) ref.get("sys");

        return (String) sys.get("id");
    }
}
