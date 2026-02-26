package ee.subjecta.subjecta_backend.mapper;

import ee.subjecta.subjecta_backend.client.contentful.model.ContentfulEntry;
import ee.subjecta.subjecta_backend.dto.TopicDto;

import java.util.Map;

public class TopicMapper {

    public static TopicDto toDto(
            ContentfulEntry topic,
            Map<String, ContentfulEntry> referenceIndex
    ) {
        String subjectId = extractReferenceId(topic, "subject");

        return new TopicDto(
                topic.sys().id(),
                subjectId,
                (String) topic.fields().get("title"),
                (String) topic.fields().get("description"),
                (Integer) topic.fields().get("order")
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
