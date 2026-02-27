package ee.subjecta.subjecta_backend.service;

import ee.subjecta.subjecta_backend.client.ContentfulClient;
import ee.subjecta.subjecta_backend.dto.SubjectDto;
import ee.subjecta.subjecta_backend.mapper.ContentType;
import ee.subjecta.subjecta_backend.mapper.ContentfulEntryHelper;
import ee.subjecta.subjecta_backend.mapper.ContentfulMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private final ContentfulClient client;

    public SubjectService(ContentfulClient client) {
        this.client = client;
    }

    public List<SubjectDto> getSubjects() {
        return client.fetchEntries().items().stream()
                .filter(e -> ContentfulEntryHelper.typeOf(e) == ContentType.SUBJECT)
                .map(ContentfulMapper::toSubjectDto)
                .toList();
    }
}
