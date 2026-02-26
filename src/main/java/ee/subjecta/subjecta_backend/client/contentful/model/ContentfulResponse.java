package ee.subjecta.subjecta_backend.client.contentful.model;

import java.util.List;

public record ContentfulResponse(
        List<ContentfulEntry> items
) {
}
