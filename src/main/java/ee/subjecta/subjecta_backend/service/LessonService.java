package ee.subjecta.subjecta_backend.service;

import ee.subjecta.subjecta_backend.client.ContentfulClient;
import ee.subjecta.subjecta_backend.client.contentful.ContentfulHttpClient;
import ee.subjecta.subjecta_backend.dto.LessonDto;
import ee.subjecta.subjecta_backend.exception.NotFoundException;
import ee.subjecta.subjecta_backend.mapper.ContentType;
import ee.subjecta.subjecta_backend.mapper.ContentfulEntryHelper;
import ee.subjecta.subjecta_backend.mapper.ContentfulReferenceResolver;
import ee.subjecta.subjecta_backend.mapper.LessonMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LessonService {

    private final ContentfulHttpClient contentfulClient;
    private final LessonMapper lessonMapper;

    public LessonService(ContentfulHttpClient contentfulClient,
                         LessonMapper lessonMapper) {
        this.contentfulClient = contentfulClient;
        this.lessonMapper = lessonMapper;
    }

    public List<LessonDto> getLessons(String topicId, int page, int size) {

        var response = contentfulClient.entries(
                "lesson",
                Map.of(
                        "fields.topic.sys.id", topicId,
                        "skip", String.valueOf(page * size),
                        "limit", String.valueOf(size)
                )
        );

        if (response.items().isEmpty()) {
            throw new NotFoundException("No lessons found for topic " + topicId);
        }

        return response.items().stream()
                .map(entry -> lessonMapper.toDto(entry))
                .toList();
    }
}
