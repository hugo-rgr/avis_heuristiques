package fr.esgi.avis.controller;

import fr.esgi.avis.dto.in.ClassificationDtoIn;
import fr.esgi.avis.dto.out.ClassificationDtoOut;
import fr.esgi.avis.port.in.ClassificationUseCase;
import fr.esgi.avis.testutil.TestObjectMappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClassificationController Tests")
class ClassificationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClassificationUseCase classificationUseCase;

    @InjectMocks
    private ClassificationController classificationController;

    private ObjectMapper objectMapper;

    private ClassificationDtoIn classificationDtoIn;
    private ClassificationDtoOut classificationDtoOut;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(classificationController).build();
        objectMapper = TestObjectMappers.objectMapper();

        classificationDtoIn = new ClassificationDtoIn(null, "PEGI 12", "FFFFFF");
        classificationDtoOut = new ClassificationDtoOut(1L, "PEGI 12", "FFFFFF");
    }

    @Test
    @DisplayName("Should create a classification and return CREATED status")
    void testCreerUneClassification() throws Exception {
        when(classificationUseCase.creerUneClassification(any(ClassificationDtoIn.class))).thenReturn(classificationDtoOut);

        mockMvc.perform(post("/api/classifications")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(classificationDtoIn)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("PEGI 12"));

        verify(classificationUseCase, times(1)).creerUneClassification(any(ClassificationDtoIn.class));
    }

    @Test
    @DisplayName("Should update a classification and return OK status")
    void testMettreAJourUneClassification() throws Exception {
        when(classificationUseCase.mettreAJourUneClassification(eq(1L), any(ClassificationDtoIn.class))).thenReturn(classificationDtoOut);

        mockMvc.perform(put("/api/classifications/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(classificationDtoIn)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("PEGI 12"));

        verify(classificationUseCase, times(1)).mettreAJourUneClassification(eq(1L), any(ClassificationDtoIn.class));
    }

    @Test
    @DisplayName("Should retrieve classification by id and return OK status")
    void testRecupererUneClassificationParId() throws Exception {
        when(classificationUseCase.recupererUneClassificationParId(1L)).thenReturn(classificationDtoOut);

        mockMvc.perform(get("/api/classifications/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("PEGI 12"));

        verify(classificationUseCase, times(1)).recupererUneClassificationParId(1L);
    }

    @Test
    @DisplayName("Should retrieve all classifications and return OK status")
    void testRecupererToutesLesClassifications() throws Exception {
        ClassificationDtoOut classification2 = new ClassificationDtoOut(2L, "PEGI 16", "CCCCCC");
        when(classificationUseCase.recupererToutesLesClassifications()).thenReturn(List.of(classificationDtoOut, classification2));

        mockMvc.perform(get("/api/classifications")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nom").value("PEGI 12"))
                .andExpect(jsonPath("$[1].nom").value("PEGI 16"));

        verify(classificationUseCase, times(1)).recupererToutesLesClassifications();
    }

    @Test
    @DisplayName("Should delete classification and return NO_CONTENT status")
    void testSupprimerUneClassification() throws Exception {
        doNothing().when(classificationUseCase).supprimerUneClassification(1L);

        mockMvc.perform(delete("/api/classifications/1")
                .contentType("application/json"))
                .andExpect(status().isNoContent());

        verify(classificationUseCase, times(1)).supprimerUneClassification(1L);
    }
}
