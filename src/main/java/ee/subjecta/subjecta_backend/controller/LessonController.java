package ee.subjecta.subjecta_backend.controller;

import ee.subjecta.subjecta_backend.dto.LessonDto;
import ee.subjecta.subjecta_backend.service.LessonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LessonController {

    private final LessonService service;

    public LessonController(LessonService service) {
        this.service = service;
    }

    @GetMapping("/topics/{topicId}/lessons")
    public List<LessonDto> lessons(@PathVariable String topicId) {
        return service.getLessonsByTopic(topicId);
    }
}
