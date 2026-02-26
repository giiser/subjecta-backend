package ee.subjecta.subjecta_backend.controller;

import ee.subjecta.subjecta_backend.client.ContentfulClient;
import ee.subjecta.subjecta_backend.client.contentful.model.ContentfulEntry;
import ee.subjecta.subjecta_backend.client.contentful.model.ContentfulResponse;
import ee.subjecta.subjecta_backend.dto.TopicDto;
import ee.subjecta.subjecta_backend.mapper.ContentType;
import ee.subjecta.subjecta_backend.mapper.ContentfulReferenceResolver;
import ee.subjecta.subjecta_backend.mapper.TopicMapper;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

import static ee.subjecta.subjecta_backend.mapper.ContentfulEntryHelper.typeOf;

public class TopicDebugController {

    private final ContentfulClient client;

    public TopicDebugController(ContentfulClient client) {
        this.client = client;
    }


    @GetMapping("/api/debug/topics")
    public List<TopicDto> topics() {
        ContentfulResponse response = client.fetchEntries();
        Map<String, ContentfulEntry> index =
                ContentfulReferenceResolver.indexById(response.includes());

        return response.items().stream()
                .filter(e -> ContentType.TOPIC.equals(typeOf(e)))
                .map(e -> TopicMapper.toDto(e, index))
                .toList();
    }
}
