package fr.esgi.avis.controller;

import fr.esgi.avis.dto.in.EditeurDtoIn;
import fr.esgi.avis.dto.out.EditeurDtoOut;
import fr.esgi.avis.port.in.EditeurUseCase;
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
@DisplayName("EditeurController Tests")
class EditeurControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EditeurUseCase editeurUseCase;

    @InjectMocks
    private EditeurController editeurController;

    private ObjectMapper objectMapper;

    private EditeurDtoIn editeurDtoIn;
    private EditeurDtoOut editeurDtoOut;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(editeurController).build();
        objectMapper = TestObjectMappers.objectMapper();

        editeurDtoIn = new EditeurDtoIn(null, "Ubisoft");
        editeurDtoOut = new EditeurDtoOut(1L, "Ubisoft");
    }

    @Test
    @DisplayName("Should create a publisher and return CREATED status")
    void testCreerUnEditeur() throws Exception {
        when(editeurUseCase.creerUnEditeur(any(EditeurDtoIn.class))).thenReturn(editeurDtoOut);

        mockMvc.perform(post("/api/editeurs")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(editeurDtoIn)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Ubisoft"));

        verify(editeurUseCase, times(1)).creerUnEditeur(any(EditeurDtoIn.class));
    }

    @Test
    @DisplayName("Should update a publisher and return OK status")
    void testMettreAJour() throws Exception {
        when(editeurUseCase.mettreAJour(eq(1L), any(EditeurDtoIn.class))).thenReturn(editeurDtoOut);

        mockMvc.perform(put("/api/editeurs/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(editeurDtoIn)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Ubisoft"));

        verify(editeurUseCase, times(1)).mettreAJour(eq(1L), any(EditeurDtoIn.class));
    }

    @Test
    @DisplayName("Should retrieve publisher by id and return OK status")
    void testRecupererUnEditeurParId() throws Exception {
        when(editeurUseCase.recupererUnEditeurParId(1L)).thenReturn(editeurDtoOut);

        mockMvc.perform(get("/api/editeurs/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Ubisoft"));

        verify(editeurUseCase, times(1)).recupererUnEditeurParId(1L);
    }

    @Test
    @DisplayName("Should retrieve all publishers and return OK status")
    void testRecupererTousLesEditeurs() throws Exception {
        EditeurDtoOut editeur2 = new EditeurDtoOut(2L, "EA Sports");
        when(editeurUseCase.recupererTousLesEditeurs()).thenReturn(List.of(editeurDtoOut, editeur2));

        mockMvc.perform(get("/api/editeurs")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nom").value("Ubisoft"))
                .andExpect(jsonPath("$[1].nom").value("EA Sports"));

        verify(editeurUseCase, times(1)).recupererTousLesEditeurs();
    }

    @Test
    @DisplayName("Should delete publisher and return NO_CONTENT status")
    void testSupprimerUnEditeur() throws Exception {
        doNothing().when(editeurUseCase).supprimerUnEditeur(1L);

        mockMvc.perform(delete("/api/editeurs/1")
                .contentType("application/json"))
                .andExpect(status().isNoContent());

        verify(editeurUseCase, times(1)).supprimerUnEditeur(1L);
    }
}
