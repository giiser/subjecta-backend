package ee.subjecta.subjecta_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

    @Bean
    public WebClient contentfulWebClient() {
        return WebClient.builder()
                .baseUrl("https://cdn.contentful.com")
                .build();
    }

}
