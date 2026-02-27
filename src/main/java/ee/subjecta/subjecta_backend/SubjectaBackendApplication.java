package ee.subjecta.subjecta_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SubjectaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubjectaBackendApplication.class, args);
	}

}
