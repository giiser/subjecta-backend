package ee.subjecta.subjecta_backend.quiz;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
        name = "user_quiz_results",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "lesson_id"}
        )
)
public class UserQuizResult {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private Long userId;

    @Getter
    @Setter
    private String lessonId;

    @Getter
    @Setter
    private int totalQuestions;

    @Getter
    @Setter
    private int correctAnswers;

    @Getter
    @Setter
    private double percentage;

    @Getter
    @Setter
    private boolean passed;

    @Getter
    @Setter
    private Instant submittedAt;
}
