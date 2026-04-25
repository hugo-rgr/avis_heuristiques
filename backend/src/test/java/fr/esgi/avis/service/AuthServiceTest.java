package fr.esgi.avis.service;

import fr.esgi.avis.business.Joueur;
import fr.esgi.avis.business.Moderateur;
import fr.esgi.avis.dto.in.LoginDtoIn;
import fr.esgi.avis.dto.out.TokenDtoOut;
import fr.esgi.avis.port.out.JoueurPort;
import fr.esgi.avis.port.out.ModerateurPort;
import fr.esgi.avis.port.out.TokenPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
class AuthServiceTest {

    @Mock
    private JoueurPort joueurPort;

    @Mock
    private ModerateurPort moderateurPort;

    @Mock
    private TokenPort tokenPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private Joueur joueur;
    private Moderateur moderateur;

    @BeforeEach
    void setUp() {
        joueur = new Joueur(List.of(), LocalDate.of(1995, 5, 15), null);
        joueur.setId(1L);
        joueur.setPseudo("gamer123");
        joueur.setEmail("gamer@example.com");
        joueur.setMotDePasse("$2a$10$hashedpassword");

        moderateur = new Moderateur("0612345678");
        moderateur.setId(2L);
        moderateur.setPseudo("mod123");
        moderateur.setEmail("mod@example.com");
        moderateur.setMotDePasse("$2a$10$hashedmodpassword");
    }

    @Test
    @DisplayName("Should login a joueur and return a JOUEUR token")
    void testLoginJoueur() {
        when(joueurPort.findByEmail("gamer@example.com")).thenReturn(Optional.of(joueur));
        when(passwordEncoder.matches("password123", joueur.getMotDePasse())).thenReturn(true);
        when(tokenPort.generateToken(1L, "gamer@example.com", "JOUEUR")).thenReturn("joueur.jwt.token");

        TokenDtoOut result = authService.login(new LoginDtoIn("gamer@example.com", "password123"));

        assertThat(result).isNotNull();
        assertThat(result.token()).isEqualTo("joueur.jwt.token");
        assertThat(result.role()).isEqualTo("JOUEUR");
        verify(tokenPort, times(1)).generateToken(1L, "gamer@example.com", "JOUEUR");
    }

    @Test
    @DisplayName("Should login a moderateur and return a MODERATEUR token")
    void testLoginModerateur() {
        when(joueurPort.findByEmail("mod@example.com")).thenReturn(Optional.empty());
        when(moderateurPort.findByEmail("mod@example.com")).thenReturn(Optional.of(moderateur));
        when(passwordEncoder.matches("modpassword", moderateur.getMotDePasse())).thenReturn(true);
        when(tokenPort.generateToken(2L, "mod@example.com", "MODERATEUR")).thenReturn("mod.jwt.token");

        TokenDtoOut result = authService.login(new LoginDtoIn("mod@example.com", "modpassword"));

        assertThat(result).isNotNull();
        assertThat(result.token()).isEqualTo("mod.jwt.token");
        assertThat(result.role()).isEqualTo("MODERATEUR");
        verify(tokenPort, times(1)).generateToken(2L, "mod@example.com", "MODERATEUR");
    }

    @Test
    @DisplayName("Should throw exception when joueur password is incorrect")
    void testLoginWrongPassword() {
        when(joueurPort.findByEmail("gamer@example.com")).thenReturn(Optional.of(joueur));
        when(passwordEncoder.matches("wrongpassword", joueur.getMotDePasse())).thenReturn(false);

        assertThatThrownBy(() -> authService.login(new LoginDtoIn("gamer@example.com", "wrongpassword")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Mot de passe incorrect");
    }

    @Test
    @DisplayName("Should throw exception when email not found in either repository")
    void testLoginEmailNotFound() {
        when(joueurPort.findByEmail("unknown@example.com")).thenReturn(Optional.empty());
        when(moderateurPort.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(new LoginDtoIn("unknown@example.com", "password")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Utilisateur non trouvé");
    }
}
