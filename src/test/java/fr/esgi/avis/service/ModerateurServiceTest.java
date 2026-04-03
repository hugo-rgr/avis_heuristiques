package fr.esgi.avis.service;

import fr.esgi.avis.business.*;
import fr.esgi.avis.dto.in.JeuDtoIn;
import fr.esgi.avis.dto.out.JeuDtoOut;
import fr.esgi.avis.dto.out.ModerateurDtoOut;
import fr.esgi.avis.mapper.JeuMapper;
import fr.esgi.avis.mapper.ModerateurMapper;
import fr.esgi.avis.port.out.*;
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
@DisplayName("ModerateurService Tests")
class ModerateurServiceTest {

    @Mock
    private ModerateurPort moderateurPort;

    @Mock
    private JeuPort jeuPort;

    @Mock
    private AvisPort avisPort;

    @Mock
    private GenrePort genrePort;

    @Mock
    private EditeurPort editeurPort;

    @Mock
    private ClassificationPort classificationPort;

    @Mock
    private PlateformePort plateformePort;

    @Mock
    private ModerateurMapper moderateurMapper;

    @Mock
    private JeuMapper jeuMapper;

    @InjectMocks
    private ModerateurService moderateurService;

    private Moderateur moderateur;
    private Avis avis;

    @BeforeEach
    void setUp() {
        moderateur = new Moderateur("0612345678");
        moderateur.setId(1L);
        moderateur.setPseudo("mod123");
        moderateur.setEmail("mod@example.com");
        moderateur.setMotDePasse("modpassword");

        avis = new Avis(1L, "Bad content", null, null, 2.0f, null, LocalDate.now());
    }

    @Test
    @DisplayName("Should connect moderator with correct credentials")
    void testSeConnecter() {
        when(moderateurPort.findByEmail("mod@example.com")).thenReturn(Optional.of(moderateur));
        when(moderateurMapper.toDto(any(Moderateur.class))).thenReturn(new ModerateurDtoOut(1L, "mod123", "mod@example.com", "0612345678"));

        ModerateurDtoOut result = moderateurService.seConnecter("mod@example.com", "modpassword");

        assertThat(result).isNotNull();
        assertThat(result.pseudo()).isEqualTo("mod123");
        verify(moderateurPort, times(1)).findByEmail("mod@example.com");
    }

    @Test
    @DisplayName("Should throw exception when moderator email not found")
    void testSeConnecterEmailNotFound() {
        when(moderateurPort.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> moderateurService.seConnecter("notfound@example.com", "password"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Modérateur non trouvé");
    }

    @Test
    @DisplayName("Should throw exception when moderator password is incorrect")
    void testSeConnecterWrongPassword() {
        when(moderateurPort.findByEmail("mod@example.com")).thenReturn(Optional.of(moderateur));

        assertThatThrownBy(() -> moderateurService.seConnecter("mod@example.com", "wrongpassword"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Mot de passe incorrect");
    }

    @Test
    @DisplayName("Should find moderator by id")
    void testTrouverParId() {
        when(moderateurPort.findById(1L)).thenReturn(Optional.of(moderateur));
        when(moderateurMapper.toDto(any(Moderateur.class))).thenReturn(new ModerateurDtoOut(1L, "mod123", "mod@example.com", "0612345678"));

        ModerateurDtoOut result = moderateurService.trouverParId(1L);

        assertThat(result).isNotNull();
        assertThat(result.pseudo()).isEqualTo("mod123");
        verify(moderateurPort, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when moderator not found by id")
    void testTrouverParIdNotFound() {
        when(moderateurPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> moderateurService.trouverParId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Modérateur non trouvé");
    }

    @Test
    @DisplayName("Should add a new game")
    void testAjouterJeu() {
        Genre genre = new Genre(1L, "RPG", List.of());
        Editeur editeur = new Editeur(1L, "Ubisoft", List.of());
        Classification classification = new Classification(1L, "PEGI 12", List.of(), "FFFFFF");

        JeuDtoIn jeuDto = new JeuDtoIn(
                1L,
                "New Game",
                "Description",
                1L,
                "image.jpg",
                59.99f,
                LocalDate.now(),
                1L,
                1L,
                List.of()
        );

        Jeu jeu = new Jeu(1L, "New Game", "Description", genre, "image.jpg", 59.99f,
                LocalDate.now(), editeur, classification, List.of());

        when(moderateurPort.findById(1L)).thenReturn(Optional.of(moderateur));
        when(genrePort.findById(1L)).thenReturn(Optional.of(genre));
        when(editeurPort.findById(1L)).thenReturn(Optional.of(editeur));
        when(classificationPort.findById(1L)).thenReturn(Optional.of(classification));
        when(jeuMapper.toDomain(any(JeuDtoIn.class), any(), any(), any(), anyList())).thenReturn(jeu);
        when(jeuPort.save(any(Jeu.class))).thenReturn(jeu);
        when(jeuMapper.toDto(any(Jeu.class))).thenReturn(new JeuDtoOut(1L, "New Game", "Description", "image.jpg", 59.99f, LocalDate.now(), "RPG", "Ubisoft", "PEGI 12", List.of()));

        JeuDtoOut result = moderateurService.ajouterJeu(1L, jeuDto);

        assertThat(result).isNotNull();
        assertThat(result.nom()).isEqualTo("New Game");
        verify(moderateurPort, times(1)).findById(1L);
        verify(jeuPort, times(1)).save(any(Jeu.class));
    }

    @Test
    @DisplayName("Should throw exception when moderator not found for adding game")
    void testAjouterJeuModerateurNotFound() {
        when(moderateurPort.findById(999L)).thenReturn(Optional.empty());

        JeuDtoIn jeuDto = new JeuDtoIn(
                null,
                "Game",
                "Description",
                1L,
                "image.jpg",
                29.99f,
                LocalDate.now(),
                1L,
                1L,
                List.of()
        );

        assertThatThrownBy(() -> moderateurService.ajouterJeu(999L, jeuDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Modérateur non trouvé");
    }

    @Test
    @DisplayName("Should delete a review")
    void testSupprimerAvis() {
        when(moderateurPort.findById(1L)).thenReturn(Optional.of(moderateur));
        when(avisPort.findById(1L)).thenReturn(Optional.of(avis));

        moderateurService.supprimerAvis(1L, 1L);

        verify(moderateurPort, times(1)).findById(1L);
        verify(avisPort, times(1)).findById(1L);
        verify(avisPort, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when moderator not found for deleting review")
    void testSupprimerAvisModerateurNotFound() {
        when(moderateurPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> moderateurService.supprimerAvis(999L, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Modérateur non trouvé");
    }

    @Test
    @DisplayName("Should throw exception when review not found for deletion")
    void testSupprimerAvisReviewNotFound() {
        when(moderateurPort.findById(1L)).thenReturn(Optional.of(moderateur));
        when(avisPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> moderateurService.supprimerAvis(1L, 999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Avis non trouvé");
    }
}
