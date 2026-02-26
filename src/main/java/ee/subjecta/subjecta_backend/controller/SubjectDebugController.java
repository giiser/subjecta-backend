package ee.subjecta.subjecta_backend.controller;

import ee.subjecta.subjecta_backend.client.ContentfulClient;
import ee.subjecta.subjecta_backend.dto.SubjectDto;
import ee.subjecta.subjecta_backend.mapper.ContentfulMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SubjectDebugController {
    private final ContentfulClient client;

    public SubjectDebugController(ContentfulClient client) {
        this.client = client;
    }

    @GetMapping("/api/debug/subjects")
    public List<SubjectDto> subjects() {
        return client.fetchEntries().items().stream()
                .map(ContentfulMapper::toSubjectDto)
                .toList();
    }
}
