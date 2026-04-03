package fr.esgi.avis.controller;

import fr.esgi.avis.dto.in.LoginDtoIn;
import fr.esgi.avis.dto.out.TokenDtoOut;
import fr.esgi.avis.port.in.AuthUseCase;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthUseCase authUseCase;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = TestObjectMappers.objectMapper();
    }

    @Test
    @DisplayName("Should login and return OK with token")
    void testLogin() throws Exception {
        LoginDtoIn loginDto = new LoginDtoIn("gamer@example.com", "password123");
        TokenDtoOut tokenDto = new TokenDtoOut("eyJ.joueur.token", "JOUEUR");

        when(authUseCase.login(any(LoginDtoIn.class))).thenReturn(tokenDto);

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("eyJ.joueur.token"))
                .andExpect(jsonPath("$.role").value("JOUEUR"));

        verify(authUseCase, times(1)).login(any(LoginDtoIn.class));
    }

    @Test
    @DisplayName("Should return OK with MODERATEUR token")
    void testLoginModerateur() throws Exception {
        LoginDtoIn loginDto = new LoginDtoIn("mod@example.com", "modpassword");
        TokenDtoOut tokenDto = new TokenDtoOut("eyJ.mod.token", "MODERATEUR");

        when(authUseCase.login(any(LoginDtoIn.class))).thenReturn(tokenDto);

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("eyJ.mod.token"))
                .andExpect(jsonPath("$.role").value("MODERATEUR"));

        verify(authUseCase, times(1)).login(any(LoginDtoIn.class));
    }
}
