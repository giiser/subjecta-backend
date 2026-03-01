package ee.subjecta.subjecta_backend.quiz;

import ee.subjecta.subjecta_backend.dto.QuizResultDto;
import ee.subjecta.subjecta_backend.dto.QuizSubmissionDto;
import ee.subjecta.subjecta_backend.progress.ProgressService;
import ee.subjecta.subjecta_backend.quiz.domain.Question;
import ee.subjecta.subjecta_backend.quiz.domain.Quiz;
import ee.subjecta.subjecta_backend.service.LessonService;
import ee.subjecta.subjecta_backend.user.User;
import ee.subjecta.subjecta_backend.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class QuizService {

    private final LessonService lessonService;
    private final UserRepository userRepository;
    private final UserQuizResultRepository resultRepository;
    private final ProgressService progressService;

    public QuizService(
            LessonService lessonService,
            UserRepository userRepository,
            UserQuizResultRepository resultRepository,
            ProgressService progressService) {

        this.lessonService = lessonService;
        this.userRepository = userRepository;
        this.resultRepository = resultRepository;
        this.progressService = progressService;
    }

    public QuizResultDto submitQuiz(
            String username,
            String lessonId,
            QuizSubmissionDto submission) {

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        Quiz quiz = lessonService.getQuizForLesson(lessonId);

        int total = quiz.questions().size();
        int correct = 0;

        for (Question question : quiz.questions()) {

            Integer answer =
                    submission.answers().get(question.id());

            if (answer != null &&
                    answer >= 0 &&
                    answer < question.options().size() &&
                    answer.equals(question.correctAnswerIndex())) {
                correct++;
            }
        }

        double percentage =
                total == 0 ? 0 : (correct * 100.0) / total;

        boolean passed = percentage >= 70;

        UserQuizResult result =
                resultRepository.findByUserIdAndLessonId(
                        user.getId(),
                        lessonId
                ).orElseGet(UserQuizResult::new);

        double previousScore = result.getPercentage() == 0
                ? 0
                : result.getPercentage();

        if (percentage > previousScore) {
            result.setUserId(user.getId());
            result.setLessonId(lessonId);
            result.setTotalQuestions(total);
            result.setCorrectAnswers(correct);
            result.setPercentage(percentage);
            result.setPassed(passed);
            result.setSubmittedAt(Instant.now());

            resultRepository.save(result);
        }

        if (passed) {
            progressService.markCompleted(username, lessonId);
        }

        return new QuizResultDto(
                total,
                correct,
                percentage,
                passed
        );
    }
}