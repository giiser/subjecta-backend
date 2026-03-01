package ee.subjecta.subjecta_backend.mapper;

import ee.subjecta.subjecta_backend.client.model.ContentfulEntry;
import ee.subjecta.subjecta_backend.dto.LessonDto;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LessonMapper {

    public LessonDto toDto(ContentfulEntry lesson) {

        String topicId = extractReferenceIdSafe(lesson, "topic");

        String title = getString(lesson, "title");
        String summary = getString(lesson, "summary");

        // RichText → convert safely to string
        Object contentObj = lesson.fields().get("content");
        String content = contentObj != null ? contentObj.toString() : null;
        Integer orderValue = (Integer) lesson.fields().get("order");
        int order = orderValue != null ? orderValue : 0;

        return new LessonDto(
                lesson.sys().id(),
                topicId,
                title,
                summary,
                content,
                order
        );
    }

    private String extractReferenceIdSafe(ContentfulEntry entry, String fieldName) {
        Object refObj = entry.fields().get(fieldName);

        if (!(refObj instanceof Map<?, ?> ref)) {
            return null;
        }

        Object sysObj = ref.get("sys");
        if (!(sysObj instanceof Map<?, ?> sys)) {
            return null;
        }

        return (String) sys.get("id");
    }

    private String getString(ContentfulEntry entry, String field) {
        Object value = entry.fields().get(field);
        return value != null ? value.toString() : null;
    }
}