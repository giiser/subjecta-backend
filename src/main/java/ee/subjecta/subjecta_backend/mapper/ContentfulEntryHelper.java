package ee.subjecta.subjecta_backend.mapper;

import ee.subjecta.subjecta_backend.client.contentful.model.ContentfulEntry;

public final class ContentfulEntryHelper {

    private ContentfulEntryHelper() {
        // utility class
    }

    public static ContentType typeOf(ContentfulEntry entry) {
        return ContentType.from(
                entry.sys()
                        .contentType()
                        .sys()
                        .id()
        );
    }
}
