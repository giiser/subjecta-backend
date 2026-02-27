package ee.subjecta.subjecta_backend.controller;

import ee.subjecta.subjecta_backend.client.contentful.ContentfulHttpClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private final ContentfulHttpClient client;

    public WebhookController(ContentfulHttpClient client) {
        this.client = client;
    }

    @PostMapping("/contentful")
    public ResponseEntity<Void> handleContentfulWebhook() {

        client.evictCache();

        return ResponseEntity.ok().build();
    }
}