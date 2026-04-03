package fr.esgi.avis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.avis.dto.in.AvatarDtoIn;
import fr.esgi.avis.dto.out.AvatarDtoOut;
import fr.esgi.avis.port.in.AvatarUseCase;
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

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AvatarController Tests")
class AvatarControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AvatarUseCase avatarUseCase;

    @InjectMocks
    private AvatarController avatarController;

    private ObjectMapper objectMapper;

    private AvatarDtoIn avatarDtoIn;
    private AvatarDtoOut avatarDtoOut;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(avatarController).build();
        objectMapper = TestObjectMappers.objectMapper();

        avatarDtoIn = new AvatarDtoIn("Knight Avatar", 1L);
        avatarDtoOut = new AvatarDtoOut(1L, "Knight Avatar", 1L, "gamer123");
    }

    @Test
    @DisplayName("Should create an avatar and return CREATED status")
    void testCreerUnAvatar() throws Exception {
        when(avatarUseCase.creerUnAvatar(any(AvatarDtoIn.class))).thenReturn(avatarDtoOut);

        mockMvc.perform(post("/api/avatars")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(avatarDtoIn)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Knight Avatar"))
                .andExpect(jsonPath("$.joueurPseudo").value("gamer123"));

        verify(avatarUseCase, times(1)).creerUnAvatar(any(AvatarDtoIn.class));
    }

    @Test
    @DisplayName("Should update an avatar and return OK status")
    void testMettreAJour() throws Exception {
        when(avatarUseCase.mettreAJour(eq(1L), any(AvatarDtoIn.class))).thenReturn(avatarDtoOut);

        mockMvc.perform(put("/api/avatars/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(avatarDtoIn)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Knight Avatar"));

        verify(avatarUseCase, times(1)).mettreAJour(eq(1L), any(AvatarDtoIn.class));
    }

    @Test
    @DisplayName("Should retrieve avatar by id and return OK status")
    void testRecupererUnAvatarParId() throws Exception {
        when(avatarUseCase.recupererUnAvatarParId(1L)).thenReturn(avatarDtoOut);

        mockMvc.perform(get("/api/avatars/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Knight Avatar"));

        verify(avatarUseCase, times(1)).recupererUnAvatarParId(1L);
    }

    @Test
    @DisplayName("Should retrieve all avatars and return OK status")
    void testRecupererTousLesAvatars() throws Exception {
        AvatarDtoOut avatar2 = new AvatarDtoOut(2L, "Dragon Avatar", 2L, "player456");
        when(avatarUseCase.recupererTousLesAvatars()).thenReturn(List.of(avatarDtoOut, avatar2));

        mockMvc.perform(get("/api/avatars")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nom").value("Knight Avatar"))
                .andExpect(jsonPath("$[1].nom").value("Dragon Avatar"));

        verify(avatarUseCase, times(1)).recupererTousLesAvatars();
    }

    @Test
    @DisplayName("Should retrieve avatars by player id and return OK status")
    void testRecupererAvatarsParJoueur() throws Exception {
        when(avatarUseCase.recupererAvatarsParJoueur(1L)).thenReturn(List.of(avatarDtoOut));

        mockMvc.perform(get("/api/avatars/joueur/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].joueurPseudo").value("gamer123"));

        verify(avatarUseCase, times(1)).recupererAvatarsParJoueur(1L);
    }

    @Test
    @DisplayName("Should delete avatar and return NO_CONTENT status")
    void testSupprimerUnAvatar() throws Exception {
        doNothing().when(avatarUseCase).supprimerUnAvatar(1L);

        mockMvc.perform(delete("/api/avatars/1")
                .contentType("application/json"))
                .andExpect(status().isNoContent());

        verify(avatarUseCase, times(1)).supprimerUnAvatar(1L);
    }
}
