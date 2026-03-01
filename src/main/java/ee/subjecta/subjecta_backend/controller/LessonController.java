package ee.subjecta.subjecta_backend.controller;

import ee.subjecta.subjecta_backend.dto.LessonDto;
import ee.subjecta.subjecta_backend.service.LessonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public List<LessonDto> lessons(
            @RequestParam String topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return lessonService.getLessons(topicId, page, size);
    }
}
