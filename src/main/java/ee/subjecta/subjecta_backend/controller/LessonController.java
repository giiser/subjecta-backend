package ee.subjecta.subjecta_backend.controller;

import ee.subjecta.subjecta_backend.dto.LessonDto;
import ee.subjecta.subjecta_backend.lesson.dto.LessonWithAccessDto;
import ee.subjecta.subjecta_backend.progress.ProgressService;
import ee.subjecta.subjecta_backend.service.LessonService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final ProgressService progressService;

    public LessonController(LessonService lessonService, ProgressService progressService) {
        this.lessonService = lessonService;
        this.progressService = progressService;
    }

    @GetMapping
    public List<LessonWithAccessDto> lessons(
            @RequestParam String topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return lessonService.getLessons(topicId, page, size)
                .stream()
                .map(lesson -> {

                    boolean canAccess =
                            progressService.canAccessLesson(
                                    username,
                                    lesson.id()
                            );

                    LessonDto safeLesson = lesson;

                    if (!canAccess) {
                        safeLesson = new LessonDto(
                                lesson.id(),
                                lesson.topicId(),
                                lesson.title(),
                                lesson.summary(),
                                null, // hide content
                                lesson.order()
                        );
                    }

                    return new LessonWithAccessDto(
                            safeLesson,
                            !canAccess
                    );
                })
                .toList();
    }

    @GetMapping("/{lessonId}")
    public LessonWithAccessDto getLesson(
            @PathVariable String lessonId) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        LessonDto lesson =
                lessonService.getLessonById(lessonId);

        boolean canAccess =
                progressService.canAccessLesson(
                        username,
                        lessonId
                );

        return new LessonWithAccessDto(
                lesson,
                !canAccess
        );
    }
}
