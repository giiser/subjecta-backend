package ee.subjecta.subjecta_backend.quiz;

import ee.subjecta.subjecta_backend.dto.QuestionDto;
import ee.subjecta.subjecta_backend.dto.QuizDto;
import ee.subjecta.subjecta_backend.dto.QuizResultDto;
import ee.subjecta.subjecta_backend.dto.QuizSubmissionDto;
import ee.subjecta.subjecta_backend.quiz.domain.Quiz;
import ee.subjecta.subjecta_backend.service.LessonService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quizzes")
public class QuizController {

    private final QuizService quizService;
    private final LessonService lessonService;

    public QuizController(QuizService quizService, LessonService lessonService) {
        this.quizService = quizService;
        this.lessonService = lessonService;
    }

    @PostMapping("/{lessonId}/submit")
    public QuizResultDto submit(
            @PathVariable String lessonId,
            @RequestBody QuizSubmissionDto submission) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return quizService.submitQuiz(
                username,
                lessonId,
                submission
        );
    }

    @GetMapping("/{lessonId}")
    public QuizDto getQuiz(@PathVariable String lessonId) {

        Quiz quiz = lessonService.getQuizForLesson(lessonId);

        List<QuestionDto> questions = quiz.questions().stream()
                .map(q -> new QuestionDto(
                        q.id(),
                        q.text(),
                        q.options()
                ))
                .toList();

        return new QuizDto(lessonId, questions);
    }
}