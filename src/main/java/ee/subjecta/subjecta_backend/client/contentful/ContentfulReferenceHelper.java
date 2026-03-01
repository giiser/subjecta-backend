package ee.subjecta.subjecta_backend.client.contentful;

import ee.subjecta.subjecta_backend.client.model.ContentfulEntry;

import java.util.Map;

public final class ContentfulReferenceHelper {

    private ContentfulReferenceHelper() {}

    public static String extractReferenceId(
            ContentfulEntry entry,
            String fieldName) {

        Object refObj = entry.fields().get(fieldName);

        if (!(refObj instanceof Map<?, ?> ref)) {
            return null;
        }

        Object sysObj = ref.get("sys");
        if (!(sysObj instanceof Map<?, ?> sys)) {
            return null;
        }

        return (String) sys.get("id");
    }
}
