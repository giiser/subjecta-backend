package ee.subjecta.subjecta_backend.progress;

import ee.subjecta.subjecta_backend.progress.dto.SubjectProgressDto;
import ee.subjecta.subjecta_backend.progress.dto.TopicProgressDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @PostMapping("/lessons/{lessonId}")
    public Map<String, String> completeLesson(
            @PathVariable String lessonId) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        progressService.markCompleted(username, lessonId);

        return Map.of("status", "lesson_completed");
    }

    @GetMapping("/lessons")
    public List<String> getCompletedLessons() {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return progressService.getCompletedLessons(username);
    }

    @GetMapping("/topics/{topicId}")
    public TopicProgressDto topicProgress(
            @PathVariable String topicId) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return progressService
                .getTopicProgress(username, topicId);
    }

    @GetMapping("/subjects/{subjectId}")
    public SubjectProgressDto subjectProgress(
            @PathVariable String subjectId) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return progressService
                .getSubjectProgress(username, subjectId);
    }
}
