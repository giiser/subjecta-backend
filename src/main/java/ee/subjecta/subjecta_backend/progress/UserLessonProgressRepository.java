package ee.subjecta.subjecta_backend.progress;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserLessonProgressRepository
        extends JpaRepository<UserLessonProgress, Long> {

    Optional<UserLessonProgress>
    findByUserIdAndLessonId(Long userId, String lessonId);

    List<UserLessonProgress>
    findByUserId(Long userId);
}