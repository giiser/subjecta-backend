package ee.subjecta.subjecta_backend.progress;

import ee.subjecta.subjecta_backend.dto.LessonDto;
import ee.subjecta.subjecta_backend.dto.TopicDto;
import ee.subjecta.subjecta_backend.progress.dto.SubjectProgressDto;
import ee.subjecta.subjecta_backend.progress.dto.TopicProgressDto;
import ee.subjecta.subjecta_backend.service.LessonService;
import ee.subjecta.subjecta_backend.service.TopicService;
import ee.subjecta.subjecta_backend.user.User;
import ee.subjecta.subjecta_backend.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
public class ProgressService {

    private final UserLessonProgressRepository repository;
    private final UserRepository userRepository;
    private final LessonService lessonService;
    private final TopicService topicService;

    public ProgressService(UserLessonProgressRepository repository,
                           UserRepository userRepository,
                           LessonService lessonService,
                           TopicService topicService) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.lessonService = lessonService;
        this.topicService = topicService;
    }

    public void markCompleted(String username, String lessonId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        UserLessonProgress progress =
                repository.findByUserIdAndLessonId(user.getId(), lessonId)
                        .orElseGet(() -> {
                            UserLessonProgress p = new UserLessonProgress();
                            p.setUserId(user.getId());
                            p.setLessonId(lessonId);
                            return p;
                        });

        progress.setCompleted(true);
        progress.setCompletedAt(Instant.now());

        repository.save(progress);
    }

    public List<String> getCompletedLessons(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return repository.findByUserId(user.getId())
                .stream()
                .filter(UserLessonProgress::isCompleted)
                .map(UserLessonProgress::getLessonId)
                .toList();
    }

    public TopicProgressDto getTopicProgress(
            String username,
            String topicId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<LessonDto> lessons =
                lessonService.getLessonsByTopic(topicId);

        List<String> completed =
                repository.findByUserId(user.getId())
                        .stream()
                        .filter(UserLessonProgress::isCompleted)
                        .map(UserLessonProgress::getLessonId)
                        .toList();

        int total = lessons.size();

        int completedCount = (int) lessons.stream()
                .map(LessonDto::id)
                .filter(completed::contains)
                .count();

        double percentage = total == 0
                ? 0
                : (completedCount * 100.0) / total;

        return new TopicProgressDto(
                topicId,
                total,
                completedCount,
                percentage
        );
    }

    public SubjectProgressDto getSubjectProgress(
            String username,
            String subjectId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<TopicDto> topics =
                topicService.getTopicsBySubject(subjectId);

        List<LessonDto> allLessons = topics.stream()
                .flatMap(t ->
                        lessonService
                                .getLessonsByTopic(t.id())
                                .stream())
                .toList();

        List<String> completed =
                repository.findByUserId(user.getId())
                        .stream()
                        .filter(UserLessonProgress::isCompleted)
                        .map(UserLessonProgress::getLessonId)
                        .toList();

        int total = allLessons.size();

        int completedCount = (int) allLessons.stream()
                .map(LessonDto::id)
                .filter(completed::contains)
                .count();

        double percentage = total == 0
                ? 0
                : (completedCount * 100.0) / total;

        return new SubjectProgressDto(
                subjectId,
                total,
                completedCount,
                percentage
        );
    }

    public boolean canAccessLesson(
            String username,
            String lessonId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        LessonDto lesson = lessonService.getLessonById(lessonId);

        List<LessonDto> lessons =
                lessonService.getLessonsByTopic(lesson.topicId());

        lessons = lessons.stream()
                .sorted(Comparator.comparingInt(LessonDto::order))
                .toList();

        if (lesson.order() == 0) {
            return true;
        }

        LessonDto previous = lessons.stream()
                .filter(l -> l.order() == lesson.order() - 1)
                .findFirst()
                .orElse(null);

        if (previous == null) {
            return true;
        }

        return repository.findByUserIdAndLessonId(
                        user.getId(),
                        previous.id()
                ).map(UserLessonProgress::isCompleted)
                .orElse(false);
    }
}
