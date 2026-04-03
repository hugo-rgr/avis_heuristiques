package fr.esgi.avis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("AvisApplication Integration Tests")
class AvisApplicationTests {

    @Test
    @DisplayName("Application context should load successfully")
    void contextLoads() {
        // If the Spring context fails to start, this test will fail.
    }
}
