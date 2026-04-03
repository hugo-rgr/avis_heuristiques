package fr.esgi.avis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.avis.dto.in.PlatefomeDtoIn;
import fr.esgi.avis.dto.out.PlatefomeDtoOut;
import fr.esgi.avis.port.in.PlateformeUseCase;
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

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PlateformeController Tests")
class PlateformeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PlateformeUseCase plateformeUseCase;

    @InjectMocks
    private PlateformeController plateformeController;

    private ObjectMapper objectMapper;

    private PlatefomeDtoIn platefomeDtoIn;
    private PlatefomeDtoOut platefomeDtoOut;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(plateformeController).build();
        objectMapper = TestObjectMappers.objectMapper();

        platefomeDtoIn = new PlatefomeDtoIn("PlayStation 5", LocalDate.of(2020, 11, 12));
        platefomeDtoOut = new PlatefomeDtoOut(1L, "PlayStation 5", LocalDate.of(2020, 11, 12), List.of());
    }

    @Test
    @DisplayName("Should create a platform and return CREATED status")
    void testCreerUnePlateforme() throws Exception {
        when(plateformeUseCase.creerUnePlateforme(any(PlatefomeDtoIn.class))).thenReturn(platefomeDtoOut);

        mockMvc.perform(post("/api/plateformes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(platefomeDtoIn)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("PlayStation 5"));

        verify(plateformeUseCase, times(1)).creerUnePlateforme(any(PlatefomeDtoIn.class));
    }

    @Test
    @DisplayName("Should update a platform and return OK status")
    void testMettreAJour() throws Exception {
        when(plateformeUseCase.mettreAJour(eq(1L), any(PlatefomeDtoIn.class))).thenReturn(platefomeDtoOut);

        mockMvc.perform(put("/api/plateformes/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(platefomeDtoIn)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("PlayStation 5"));

        verify(plateformeUseCase, times(1)).mettreAJour(eq(1L), any(PlatefomeDtoIn.class));
    }

    @Test
    @DisplayName("Should retrieve platform by id and return OK status")
    void testRecupererUnePlateformeParId() throws Exception {
        when(plateformeUseCase.recupererUnePlateformeParId(1L)).thenReturn(platefomeDtoOut);

        mockMvc.perform(get("/api/plateformes/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("PlayStation 5"));

        verify(plateformeUseCase, times(1)).recupererUnePlateformeParId(1L);
    }

    @Test
    @DisplayName("Should retrieve all platforms and return OK status")
    void testRecupererToutesLesPlateformes() throws Exception {
        PlatefomeDtoOut plateforme2 = new PlatefomeDtoOut(2L, "Xbox Series X", LocalDate.of(2020, 11, 10), List.of());
        when(plateformeUseCase.recupererToutesLesPlateformes()).thenReturn(List.of(platefomeDtoOut, plateforme2));

        mockMvc.perform(get("/api/plateformes")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nom").value("PlayStation 5"))
                .andExpect(jsonPath("$[1].nom").value("Xbox Series X"));

        verify(plateformeUseCase, times(1)).recupererToutesLesPlateformes();
    }

    @Test
    @DisplayName("Should delete platform and return NO_CONTENT status")
    void testSupprimerUnePlateforme() throws Exception {
        doNothing().when(plateformeUseCase).supprimerUnePlateforme(1L);

        mockMvc.perform(delete("/api/plateformes/1")
                        .contentType("application/json"))
                .andExpect(status().isNoContent());

        verify(plateformeUseCase, times(1)).supprimerUnePlateforme(1L);
    }
}
