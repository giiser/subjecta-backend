package ee.subjecta.subjecta_backend.controller;

import ee.subjecta.subjecta_backend.client.ContentfulClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentfulTestController {

    private final ContentfulClient contentfulClient;

    public ContentfulTestController(ContentfulClient contentfulClient) {
        this.contentfulClient = contentfulClient;
    }

    @GetMapping("/api/debug/contentful")
    public String fetchContentful() {
        return contentfulClient.fetchEntries();
    }
}
