package fr.esgi.avis.service;

import fr.esgi.avis.business.Avis;
import fr.esgi.avis.business.Joueur;
import fr.esgi.avis.dto.in.JoueurDtoIn;
import fr.esgi.avis.dto.out.AvisDtoOut;
import fr.esgi.avis.dto.out.JoueurDtoOut;
import fr.esgi.avis.port.out.AvisPort;
import fr.esgi.avis.port.out.JeuPort;
import fr.esgi.avis.port.out.JoueurPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JoueurService Tests")
class JoueurServiceTest {

    @Mock
    private JoueurPort joueurPort;

    @Mock
    private AvisPort avisPort;

    @Mock
    private JeuPort jeuPort;

    @InjectMocks
    private JoueurService joueurService;

    private Joueur joueur;
    private JoueurDtoIn joueurDtoIn;

    @BeforeEach
    void setUp() {
        joueur = new Joueur(List.of(), LocalDate.of(1995, 5, 15), null);
        joueur.setId(1L);
        joueur.setPseudo("gamer123");
        joueur.setEmail("gamer@example.com");
        joueur.setMotDePasse("password123");

        joueurDtoIn = new JoueurDtoIn("gamer123", "gamer@example.com", "password123", LocalDate.of(1995, 5, 15));
    }

    @Test
    @DisplayName("Should connect player with correct credentials")
    void testSeConnecter() {
        when(joueurPort.findByEmail("gamer@example.com")).thenReturn(Optional.of(joueur));

        JoueurDtoOut result = joueurService.seConnecter("gamer@example.com", "password123");

        assertThat(result).isNotNull();
        assertThat(result.pseudo()).isEqualTo("gamer123");
        verify(joueurPort, times(1)).findByEmail("gamer@example.com");
    }

    @Test
    @DisplayName("Should throw exception when email not found")
    void testSeConnecterEmailNotFound() {
        when(joueurPort.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> joueurService.seConnecter("notfound@example.com", "password123"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Joueur non trouvé");
    }

    @Test
    @DisplayName("Should throw exception when password is incorrect")
    void testSeConnecterWrongPassword() {
        when(joueurPort.findByEmail("gamer@example.com")).thenReturn(Optional.of(joueur));

        assertThatThrownBy(() -> joueurService.seConnecter("gamer@example.com", "wrongpassword"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Mot de passe incorrect");
    }

    @Test
    @DisplayName("Should register a new player successfully")
    void testSInscrire() {
        when(joueurPort.existsByEmail("newgamer@example.com")).thenReturn(false);
        Joueur newJoueur = new Joueur(List.of(), LocalDate.of(2000, 1, 1), null);
        newJoueur.setId(2L);
        newJoueur.setPseudo("newgamer");
        newJoueur.setEmail("newgamer@example.com");
        newJoueur.setMotDePasse("newpassword");

        when(joueurPort.save(any(Joueur.class))).thenReturn(newJoueur);

        JoueurDtoIn newDtoIn = new JoueurDtoIn("newgamer", "newgamer@example.com", "newpassword", LocalDate.of(2000, 1, 1));
        JoueurDtoOut result = joueurService.sInscrire(newDtoIn);

        assertThat(result).isNotNull();
        assertThat(result.pseudo()).isEqualTo("newgamer");
        verify(joueurPort, times(1)).existsByEmail("newgamer@example.com");
        verify(joueurPort, times(1)).save(any(Joueur.class));
    }

    @Test
    @DisplayName("Should throw exception when email already used")
    void testSInscrireEmailAlreadyExists() {
        when(joueurPort.existsByEmail("gamer@example.com")).thenReturn(true);

        assertThatThrownBy(() -> joueurService.sInscrire(joueurDtoIn))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email déjà utilisé");
    }

    @Test
    @DisplayName("Should retrieve player by id")
    void testTrouverParId() {
        when(joueurPort.findById(1L)).thenReturn(Optional.of(joueur));

        JoueurDtoOut result = joueurService.trouverParId(1L);

        assertThat(result).isNotNull();
        assertThat(result.pseudo()).isEqualTo("gamer123");
        verify(joueurPort, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when player not found by id")
    void testTrouverParIdNotFound() {
        when(joueurPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> joueurService.trouverParId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Joueur non trouvé");
    }

    @Test
    @DisplayName("Should retrieve player's reviews")
    void testListerAvisDuJoueur() {
        Avis avis1 = new Avis(1L, "Great game!", null, joueur, 9.5f, null, LocalDate.now());
        Avis avis2 = new Avis(2L, "Awesome!", null, joueur, 9.0f, null, LocalDate.now());

        when(avisPort.findAllByJoueurId(1L)).thenReturn(List.of(avis1, avis2));

        List<AvisDtoOut> result = joueurService.listerAvisDuJoueur(1L);

        assertThat(result).hasSize(2);
        verify(avisPort, times(1)).findAllByJoueurId(1L);
    }
}
