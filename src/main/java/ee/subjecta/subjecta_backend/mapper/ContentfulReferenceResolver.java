package ee.subjecta.subjecta_backend.mapper;

import ee.subjecta.subjecta_backend.client.model.ContentfulEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentfulReferenceResolver {
    public static Map<String, ContentfulEntry> indexById(
            Map<String, List<ContentfulEntry>> includes
    ) {
        Map<String, ContentfulEntry> index = new HashMap<>();

        if (includes == null || !includes.containsKey("Entry")) {
            return index;
        }

        for (ContentfulEntry entry : includes.get("Entry")) {
            index.put(entry.sys().id(), entry);
        }

        return index;
    }
}
