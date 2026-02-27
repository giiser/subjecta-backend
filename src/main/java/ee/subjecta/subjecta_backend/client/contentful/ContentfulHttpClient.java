package ee.subjecta.subjecta_backend.client.contentful;

import ee.subjecta.subjecta_backend.client.model.ContentfulResponse;
import ee.subjecta.subjecta_backend.exception.CmsException;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.InvalidUrlException;
import org.springframework.web.util.UriComponentsBuilder;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.util.Map;

@Component
public class ContentfulHttpClient implements ContentfulClient {

    private final RestTemplate restTemplate;
    private final ContentfulProperties properties;
    private final CacheManager cacheManager;
    private final MeterRegistry meterRegistry;

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(properties.getAccessToken());
        return headers;
    }

    public ContentfulHttpClient(RestTemplate restTemplate,
                                ContentfulProperties properties,
                                CacheManager cacheManager,
                                MeterRegistry meterRegistry) {
        this.restTemplate = restTemplate;
        this.properties = properties;
        this.cacheManager = cacheManager;
        this.meterRegistry = meterRegistry;
    }

    public void evictCache() {
        var cache = cacheManager.getCache("contentfulEntries");
        if (cache != null) {
            cache.clear();
            System.out.println("Contentful cache cleared");
        }
    }

    @Override
    @Cacheable(value = "contentfulEntries",
            key = "#contentType + '-' + #queryParams.hashCode()")
    public ContentfulResponse entries(String contentType,
                                      Map<String, String> queryParams) {

        Timer.Sample sample = Timer.start(meterRegistry);

        try {

            var uri = UriComponentsBuilder
                    .fromUriString(properties.getBaseUrl())
                    .path("/spaces/{spaceId}/environments/{env}/entries")
                    .queryParam("content_type", contentType)
                    .buildAndExpand(
                            properties.getSpaceId(),
                            properties.getEnvironment()
                    )
                    .toUri();

            var uriBuilder = UriComponentsBuilder.fromUri(uri);
            queryParams.forEach(uriBuilder::queryParam);

            var response = restTemplate.exchange(
                    uriBuilder.build(true).toUri(),
                    HttpMethod.GET,
                    new HttpEntity<>(headers()),
                    ContentfulResponse.class
            );

            meterRegistry.counter("cms.requests.success",
                    "contentType", contentType).increment();

            return response.getBody();

        } catch (Exception ex) {

            meterRegistry.counter("cms.requests.failure",
                    "contentType", contentType).increment();

            throw new CmsException("Failed to fetch entries from Contentful", ex);

        } finally {

            sample.stop(
                    Timer.builder("cms.requests.duration")
                            .tag("contentType", contentType)
                            .register(meterRegistry)
            );
        }
    }
}
