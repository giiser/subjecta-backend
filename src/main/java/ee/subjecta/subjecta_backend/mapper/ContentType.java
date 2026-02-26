package ee.subjecta.subjecta_backend.mapper;

public enum ContentType {
    SUBJECT,
    TOPIC,
    LESSON,
    QUIZ;

    public static ContentType from(String value) {
        return ContentType.valueOf(value.toUpperCase());
    }
}
