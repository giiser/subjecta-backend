package ee.subjecta.subjecta_backend.service;

import ee.subjecta.subjecta_backend.client.ContentfulClient;
import ee.subjecta.subjecta_backend.dto.TopicDto;
import ee.subjecta.subjecta_backend.mapper.ContentType;
import ee.subjecta.subjecta_backend.mapper.ContentfulEntryHelper;
import ee.subjecta.subjecta_backend.mapper.ContentfulReferenceResolver;
import ee.subjecta.subjecta_backend.mapper.TopicMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    private final ContentfulClient client;

    public TopicService(ContentfulClient client) {
        this.client = client;
    }

    public List<TopicDto> getTopicsBySubject(String subjectId) {
        var response = client.fetchEntries();
        var index = ContentfulReferenceResolver.indexById(response.includes());

        return response.items().stream()
                .filter(e -> ContentfulEntryHelper.typeOf(e) == ContentType.TOPIC)
                .map(e -> TopicMapper.toDto(e, index))
                .filter(t -> subjectId.equals(t.subjectId()))
                .toList();
    }
}
