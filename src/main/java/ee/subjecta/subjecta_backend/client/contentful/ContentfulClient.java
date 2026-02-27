package ee.subjecta.subjecta_backend.client.contentful;

import ee.subjecta.subjecta_backend.client.model.ContentfulResponse;

import java.util.Map;

public interface ContentfulClient {

    ContentfulResponse entries(
            String contentType,
            Map<String, String> queryParams
    );
}