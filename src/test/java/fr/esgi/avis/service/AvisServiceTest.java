package fr.esgi.avis.service;

import fr.esgi.avis.business.*;
import fr.esgi.avis.dto.in.AvisDtoIn;
import fr.esgi.avis.dto.out.AvisDtoOut;
import fr.esgi.avis.port.out.AvisPort;
import fr.esgi.avis.port.out.JeuPort;
import fr.esgi.avis.port.out.JoueurPort;
import fr.esgi.avis.port.out.ModerateurPort;
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
@DisplayName("AvisService Tests")
class AvisServiceTest {

    @Mock
    private AvisPort avisPort;

    @Mock
    private JeuPort jeuPort;

    @Mock
    private JoueurPort joueurPort;

    @Mock
    private ModerateurPort moderateurPort;

    @InjectMocks
    private AvisService avisService;

    private Avis avis;
    private Jeu jeu;
    private Joueur joueur;
    private Moderateur moderateur;
    private AvisDtoIn avisDtoIn;

    @BeforeEach
    void setUp() {
        Genre genre = new Genre(1L, "RPG", List.of());
        Editeur editeur = new Editeur(1L, "Larian", List.of());
        Classification classification = new Classification(1L, "PEGI 16", List.of(), "FFFFFF");

        jeu = new Jeu(1L, "Baldur's Gate 3", "Fantasy RPG", genre,
                "bg3.jpg", 59.99f, LocalDate.of(2023, 8, 3),
                editeur, classification, List.of());

        joueur = new Joueur(List.of(), LocalDate.of(1995, 5, 15), null);
        joueur.setId(1L);
        joueur.setPseudo("gamer123");

        moderateur = new Moderateur("0612345678");
        moderateur.setId(1L);
        moderateur.setPseudo("mod123");

        avis = new Avis(1L, "Great game!", jeu, joueur, 9.5f, moderateur, LocalDate.now());
        avisDtoIn = new AvisDtoIn(null, "Great game!", 1L, 1L, 9.5f, null, LocalDate.now());
    }

    @Test
    @DisplayName("Should create a review successfully")
    void testCreerUnAvis() {
        when(jeuPort.findById(1L)).thenReturn(Optional.of(jeu));
        when(joueurPort.findById(1L)).thenReturn(Optional.of(joueur));
        when(avisPort.save(any(Avis.class))).thenReturn(avis);

        AvisDtoOut result = avisService.creerUnAvis(avisDtoIn);

        assertThat(result).isNotNull();
        assertThat(result.description()).isEqualTo("Great game!");
        assertThat(result.note()).isEqualTo(9.5f);
        verify(avisPort, times(1)).save(any(Avis.class));
    }

    @Test
    @DisplayName("Should throw exception when game not found during creation")
    void testCreerUnAvisJeuNotFound() {
        when(jeuPort.findById(999L)).thenReturn(Optional.empty());

        AvisDtoIn dtoWithBadJeu = new AvisDtoIn(null, "Great game!", 999L, 1L, 9.5f, null, LocalDate.now());
        assertThatThrownBy(() -> avisService.creerUnAvis(dtoWithBadJeu))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Jeu non trouvé");
    }

    @Test
    @DisplayName("Should update a review successfully")
    void testMettreAJourUnAvis() {
        when(avisPort.findById(1L)).thenReturn(Optional.of(avis));
        when(jeuPort.findById(1L)).thenReturn(Optional.of(jeu));
        when(joueurPort.findById(1L)).thenReturn(Optional.of(joueur));
        when(avisPort.save(any(Avis.class))).thenReturn(avis);

        AvisDtoOut result = avisService.mettreAJourUnAvis(1L, avisDtoIn);

        assertThat(result).isNotNull();
        verify(avisPort, times(1)).findById(1L);
        verify(avisPort, times(1)).save(any(Avis.class));
    }

    @Test
    @DisplayName("Should throw exception when review not found for update")
    void testMettreAJourUnAvisNotFound() {
        when(avisPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> avisService.mettreAJourUnAvis(999L, avisDtoIn))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Avis non trouvé");
    }

    @Test
    @DisplayName("Should retrieve review by id")
    void testRecupererUnAvisParId() {
        when(avisPort.findById(1L)).thenReturn(Optional.of(avis));

        AvisDtoOut result = avisService.recupererUnAvisParId(1L);

        assertThat(result).isNotNull();
        assertThat(result.description()).isEqualTo("Great game!");
        verify(avisPort, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when review not found by id")
    void testRecupererUnAvisParIdNotFound() {
        when(avisPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> avisService.recupererUnAvisParId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Avis non trouvé");
    }

    @Test
    @DisplayName("Should retrieve all reviews")
    void testRecupererTousLesAvis() {
        Avis avis2 = new Avis(2L, "Awesome!", jeu, joueur, 9.0f, null, LocalDate.now());
        when(avisPort.findAll()).thenReturn(List.of(avis, avis2));

        List<AvisDtoOut> result = avisService.recupererTousLesAvis();

        assertThat(result).hasSize(2);
        assertThat(result).extracting("description").contains("Great game!", "Awesome!");
        verify(avisPort, times(1)).findAll();
    }

    @Test
    @DisplayName("Should retrieve reviews by game id")
    void testRecupererTousLesAvisParJeu() {
        when(avisPort.findAllByJeuId(1L)).thenReturn(List.of(avis));

        List<AvisDtoOut> result = avisService.recupererTousLesAvisParJeu(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).description()).isEqualTo("Great game!");
        verify(avisPort, times(1)).findAllByJeuId(1L);
    }

    @Test
    @DisplayName("Should retrieve reviews by player id")
    void testRecupererTousLesAvisParJoueur() {
        when(avisPort.findAllByJoueurId(1L)).thenReturn(List.of(avis));

        List<AvisDtoOut> result = avisService.recupererTousLesAvisParJoueur(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).joueurPseudo()).isEqualTo("gamer123");
        verify(avisPort, times(1)).findAllByJoueurId(1L);
    }

    @Test
    @DisplayName("Should delete a review")
    void testSupprimerUnAvis() {
        avisService.supprimerUnAvis(1L);

        verify(avisPort, times(1)).deleteById(1L);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme