package ee.subjecta.subjecta_backend.service;

import ee.subjecta.subjecta_backend.client.ContentfulClient;
import ee.subjecta.subjecta_backend.client.contentful.ContentfulHttpClient;
import ee.subjecta.subjecta_backend.client.contentful.ContentfulReferenceHelper;
import ee.subjecta.subjecta_backend.client.model.ContentfulEntry;
import ee.subjecta.subjecta_backend.dto.LessonDto;
import ee.subjecta.subjecta_backend.exception.NotFoundException;
import ee.subjecta.subjecta_backend.mapper.ContentType;
import ee.subjecta.subjecta_backend.mapper.ContentfulEntryHelper;
import ee.subjecta.subjecta_backend.mapper.LessonMapper;
import ee.subjecta.subjecta_backend.quiz.domain.Question;
import ee.subjecta.subjecta_backend.quiz.domain.Quiz;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;


@Service
public class LessonService {

    private final ContentfulHttpClient contentfulClient;
    private final LessonMapper lessonMapper;

    public LessonService(ContentfulHttpClient contentfulClient,
                         LessonMapper lessonMapper) {
        this.contentfulClient = contentfulClient;
        this.lessonMapper = lessonMapper;
    }

    public List<LessonDto> getLessons(String topicId, int page, int size) {

        var response = contentfulClient.entries(
                "lesson",
                Map.of(
                        "fields.topic.sys.id", topicId,
                        "skip", String.valueOf(page * size),
                        "limit", String.valueOf(size)
                )
        );

        if (response.items().isEmpty()) {
            throw new NotFoundException("No lessons found for topic " + topicId);
        }

        return response.items().stream()
                .map(entry -> lessonMapper.toDto(entry))
                .toList();
    }

    public List<LessonDto> getLessonsByTopic(String topicId) {

        var response = contentfulClient.entries(
                "lesson",
                Map.of(
                        "fields.topic.sys.id", topicId,
                        "limit", "1000"
                )
        );

        return response.items().stream()
                .filter(e -> ContentfulEntryHelper.typeOf(e) == ContentType.LESSON)
                .map(entry -> lessonMapper.toDto(entry))
                .sorted(Comparator.comparingInt(LessonDto::order))
                .toList();
    }

    private Quiz mapQuiz(String lessonId, ContentfulEntry quizEntry) {

        List<Map<String, Object>> questionsRaw =
                (List<Map<String, Object>>) quizEntry
                        .fields()
                        .get("questions");

        List<Question> questions = questionsRaw.stream()
                .map(q -> new Question(
                        (String) q.get("id"),
                        (String) q.get("text"),
                        (List<String>) q.get("options"),
                        (Integer) q.get("correctAnswerIndex")
                ))
                .toList();

        return new Quiz(lessonId, questions);
    }
    public Quiz getQuizForLesson(String lessonId) {

        var response = contentfulClient.entries(
                "quiz",
                Map.of(
                        "fields.lesson.sys.id", lessonId,
                        "limit", "1000"
                )
        );

        if (response.items().isEmpty()) {
            throw new RuntimeException("Quiz not found for lesson " + lessonId);
        }

        List<Question> questions = response.items().stream()
                .sorted(Comparator.comparingInt(e ->
                        (Integer) e.fields().getOrDefault("order", 0)
                ))
                .map(entry -> new Question(
                        entry.sys().id(),
                        (String) entry.fields().get("question"),
                        (List<String>) entry.fields().get("options"),
                        (Integer) entry.fields().get("correctIndex")
                ))
                .toList();

        return new Quiz(lessonId, questions);
    }
//    public Quiz getQuizForLesson(String lessonId) {
//
//        var response = contentfulClient.entries(
//                "lesson",
//                Map.of(
//                        "sys.id", lessonId,
//                        "include", "2"
//                )
//        );
//
//        ContentfulEntry lessonEntry = response.items().stream()
//                .findFirst()
//                .orElseThrow(() ->
//                        new RuntimeException("Lesson not found"));
//
//        // Resolve quiz reference
//        String quizId = ContentfulReferenceHelper.extractReferenceId(lessonEntry, "quiz");
//
//        ContentfulEntry quizEntry = response.includes()
//                .getOrDefault("Entry", List.of())
//                .stream()
//                .filter(e -> e.sys().id().equals(quizId))
//                .findFirst()
//                .orElseThrow(() ->
//                        new RuntimeException("Quiz not found"));
//
//        return mapQuiz(lessonId, quizEntry);
//    }

    public LessonDto getLessonById(String lessonId) {

        var response = contentfulClient.entries(
                "lesson",
                Map.of(
                        "sys.id", lessonId,
                        "limit", "1"
                )
        );

        return response.items().stream()
                .filter(e -> ContentfulEntryHelper.typeOf(e) == ContentType.LESSON)
                .findFirst()
                .map(entry -> lessonMapper.toDto(entry))
                .orElseThrow(() ->
                        new NotFoundException("Lesson not found"));
    }
}
