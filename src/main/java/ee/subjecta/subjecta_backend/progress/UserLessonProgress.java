package ee.subjecta.subjecta_backend.progress;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
        name = "user_lesson_progress",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "lesson_id"}
        )
)
public class UserLessonProgress {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Getter
    @Setter
    @Column(name = "lesson_id", nullable = false)
    private String lessonId;

    @Getter
    @Setter
    private boolean completed = false;

    @Getter
    @Setter
    private Instant completedAt;

}