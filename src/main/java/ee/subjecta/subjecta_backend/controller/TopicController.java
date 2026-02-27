package ee.subjecta.subjecta_backend.controller;

import ee.subjecta.subjecta_backend.dto.TopicDto;
import ee.subjecta.subjecta_backend.service.TopicService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TopicController {

    private final TopicService service;

    public TopicController(TopicService service) {
        this.service = service;
    }

    @GetMapping("/subjects/{subjectId}/topics")
    public List<TopicDto> topics(@PathVariable String subjectId) {
        return service.getTopicsBySubject(subjectId);
    }
}
