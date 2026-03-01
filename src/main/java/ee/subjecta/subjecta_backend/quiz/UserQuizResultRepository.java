package ee.subjecta.subjecta_backend.quiz;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserQuizResultRepository
        extends JpaRepository<UserQuizResult, Long> {

    Optional<UserQuizResult>
    findByUserIdAndLessonId(Long userId, String lessonId);

    List<UserQuizResult>
    findByUserId(Long userId);

    List<UserQuizResult>
    findByUserIdAndPassedTrue(Long userId);
}