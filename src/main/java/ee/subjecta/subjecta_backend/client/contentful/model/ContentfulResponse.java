package ee.subjecta.subjecta_backend.client.contentful.model;

import java.util.List;
import java.util.Map;

public record ContentfulResponse(
        List<ContentfulEntry> items,
        Map<String, List<ContentfulEntry>> includes
) {
}
