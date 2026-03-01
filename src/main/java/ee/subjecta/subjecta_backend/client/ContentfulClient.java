package ee.subjecta.subjecta_backend.client;

import ee.subjecta.subjecta_backend.client.model.ContentfulResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ContentfulClient {

    private final WebClient webClient;
    private final String spaceId;
    private final String accessToken;

    public ContentfulClient(
            WebClient contentfulWebClient,
            @Value("${contentful.space-id}") String spaceId,
            @Value("${contentful.access-token}") String accessToken
    ) {
        this.webClient = contentfulWebClient;
        this.spaceId = spaceId;
        this.accessToken = accessToken;
    }

    public ContentfulResponse fetchEntries() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/spaces/{spaceId}/entries")
                        .queryParam("access_token", accessToken)
                        .build(spaceId))
                .retrieve()
                .bodyToMono(ContentfulResponse.class)
                .block();
    }
}