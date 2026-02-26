package ee.subjecta.subjecta_backend.client.contentful.model;

public record Sys(
        String id,
        ContentType contentType
) {
    public record ContentType(LinkSys sys) {
        public record LinkSys(String id) {}
    }
}
