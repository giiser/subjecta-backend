package ee.subjecta.subjecta_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;   // 👈 ADD THIS


@SpringBootTest
@ActiveProfiles("test")
class SubjectaBackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
