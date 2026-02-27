package ee.subjecta.subjecta_backend.client.model;

import java.util.Map;

public record ContentfulEntry(
        Sys sys,
        Map<String, Object> fields
) {

}
