package ee.subjecta.subjecta_backend.mapper;

import ee.subjecta.subjecta_backend.client.model.ContentfulEntry;
import ee.subjecta.subjecta_backend.dto.SubjectDto;

import java.util.Map;

public class ContentfulMapper {

    public static SubjectDto toSubjectDto(ContentfulEntry entry) {
        Map<String, Object> fields = entry.fields();

        return new SubjectDto(
                entry.sys().id(),
                (String) fields.get("title"),
                (String) fields.get("description"),
                (String) fields.get("themeColor")
        );
    }
}
