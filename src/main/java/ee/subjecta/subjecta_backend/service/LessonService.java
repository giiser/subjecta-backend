package ee.subjecta.subjecta_backend.service;

import ee.subjecta.subjecta_backend.client.ContentfulClient;
import ee.subjecta.subjecta_backend.dto.LessonDto;
import ee.subjecta.subjecta_backend.mapper.ContentType;
import ee.subjecta.subjecta_backend.mapper.ContentfulEntryHelper;
import ee.subjecta.subjecta_backend.mapper.ContentfulReferenceResolver;
import ee.subjecta.subjecta_backend.mapper.LessonMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {

    private final ContentfulClient client;

    public LessonService(ContentfulClient client) {
        this.client = client;
    }

    public List<LessonDto> getLessonsByTopic(String topicId) {
        var response = client.fetchEntries();
        var index = ContentfulReferenceResolver.indexById(response.includes());

        return response.items().stream()
                .filter(e -> ContentfulEntryHelper.typeOf(e) == ContentType.LESSON)
                .map(e -> LessonMapper.toDto(e, index))
                .filter(l -> topicId.equals(l.topicId()))
                .toList();
    }
}
