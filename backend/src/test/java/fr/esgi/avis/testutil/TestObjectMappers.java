package fr.esgi.avis.testutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Centralise la configuration Jackson pour les tests "standalone" MockMvc.
 *
 * Sans Spring Boot auto-config, il faut enregistrer le module JavaTime pour LocalDate.
 */
public final class TestObjectMappers {

    private TestObjectMappers() {
    }

    public static ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}

