package ee.subjecta.subjecta_backend.client.contentful.model;

import java.util.Map;

public record ContentfulEntry(
        Sys sys,
        Map<String, Object> fields
) {
    public record Sys(String id){}
}
