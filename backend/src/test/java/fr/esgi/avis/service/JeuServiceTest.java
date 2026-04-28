package fr.esgi.avis.service;

import fr.esgi.avis.business.*;
import fr.esgi.avis.dto.in.JeuDtoIn;
import fr.esgi.avis.dto.out.JeuDtoOut;
import fr.esgi.avis.port.out.*;
import fr.esgi.avis.mapper.JeuMapper;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JeuService Tests")
class JeuServiceTest {

    @Mock
    private JeuPort jeuPort;

    @Mock
    private GenrePort genrePort;

    @Mock
    private EditeurPort editeurPort;

    @Mock
    private ClassificationPort classificationPort;

    @Mock
    private PlateformePort plateformePort;

    @Mock
    private AvisPort avisPort;

    @Mock
    private JeuMapper jeuMapper;

    @InjectMocks
    private JeuService jeuService;

    private JeuDtoIn jeuDtoIn;
    private Jeu jeu;
    private Genre genre;
    private Editeur editeur;
    private Classification classification;

    @BeforeEach
    void setUp() {
        genre = new Genre(1L, "RPG", List.of());
        editeur = new Editeur(1L, "Ubisoft", List.of());
        classification = new Classification(1L, "PEGI 12", List.of(), "FFFFFF");

        jeuDtoIn = new JeuDtoIn(
                "Baldur's Gate 3",
                "A fantasy RPG",
                1L,
                "image.jpg",
                59.99f,
                LocalDate.of(2023, 8, 3),
                1L,
                1L,
                List.of()
        );

        jeu = new Jeu(1L, "Baldur's Gate 3", "A fantasy RPG", genre, "image.jpg", 59.99f,
                LocalDate.of(2023, 8, 3), editeur, classification, List.of(), null);
    }

    @Test
    @DisplayName("Should create a new game successfully")
    void testCreerUnJeu() {
        when(genrePort.findById(1L)).thenReturn(Optional.of(genre));
        when(editeurPort.findById(1L)).thenReturn(Optional.of(editeur));
        when(classificationPort.findById(1L)).thenReturn(Optional.of(classification));
        when(jeuMapper.toDomain(any(JeuDtoIn.class), any(), any(), any(), anyList())).thenReturn(jeu);
        when(jeuPort.save(any(Jeu.class))).thenReturn(jeu);
        when(avisPort.findNoteMoyenneByJeuId(anyLong())).thenReturn(Optional.empty());
        when(jeuMapper.toDto(any(Jeu.class))).thenReturn(new JeuDtoOut(1L, "Baldur's Gate 3", "A fantasy RPG", "image.jpg", 59.99f, LocalDate.of(2023, 8, 3), "RPG", "Ubisoft", "PEGI 12", List.of(), null));

        JeuDtoOut result = jeuService.creerUnJeu(jeuDtoIn);

        assertThat(result).isNotNull();
        assertThat(result.nom()).isEqualTo("Baldur's Gate 3");
        verify(jeuPort, times(1)).save(any(Jeu.class));
    }

    @Test
    @DisplayName("Should throw exception when genre not found during creation")
    void testCreerUnJeuGenreNotFound() {
        when(genrePort.findById(999L)).thenReturn(Optional.empty());

        JeuDtoIn dtoWithBadGenre = new JeuDtoIn(
                "Game",
                "Description",
                999L,
                "image.jpg",
                29.99f,
                LocalDate.now(),
                1L,
                1L,
                List.of()
        );

        assertThatThrownBy(() -> jeuService.creerUnJeu(dtoWithBadGenre))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Genre non trouvé");
    }

    @Test
    @DisplayName("Should update an existing game")
    void testMettreAJourUnJeu() {
        when(jeuPort.findById(1L)).thenReturn(Optional.of(jeu));
        when(genrePort.findById(1L)).thenReturn(Optional.of(genre));
        when(editeurPort.findById(1L)).thenReturn(Optional.of(editeur));
        when(classificationPort.findById(1L)).thenReturn(Optional.of(classification));
        when(jeuMapper.toDomain(anyLong(), any(JeuDtoIn.class), any(), any(), any(), anyList())).thenReturn(jeu);
        when(jeuPort.save(any(Jeu.class))).thenReturn(jeu);
        when(avisPort.findNoteMoyenneByJeuId(anyLong())).thenReturn(Optional.empty());
        when(jeuMapper.toDto(any(Jeu.class))).thenReturn(new JeuDtoOut(1L, "Baldur's Gate 3", "A fantasy RPG", "image.jpg", 59.99f, LocalDate.of(2023, 8, 3), "RPG", "Ubisoft", "PEGI 12", List.of(), null));

        JeuDtoOut result = jeuService.mettreAJourUnJeu(1L, jeuDtoIn);

        assertThat(result).isNotNull();
        verify(jeuPort, times(1)).findById(1L);
        verify(jeuPort, times(1)).save(any(Jeu.class));
    }

    @Test
    @DisplayName("Should throw exception when game not found during update")
    void testMettreAJourUnJeuNotFound() {
        when(jeuPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jeuService.mettreAJourUnJeu(999L, jeuDtoIn))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Jeu non trouvé");
    }

    @Test
    @DisplayName("Should retrieve game by id")
    void testRecupererUnJeuParId() {
        when(jeuPort.findById(1L)).thenReturn(Optional.of(jeu));
        when(avisPort.findNoteMoyenneByJeuId(anyLong())).thenReturn(Optional.empty());
        when(jeuMapper.toDto(any(Jeu.class))).thenReturn(new JeuDtoOut(1L, "Baldur's Gate 3", "A fantasy RPG", "image.jpg", 59.99f, LocalDate.of(2023, 8, 3), "RPG", "Ubisoft", "PEGI 12", List.of(), null));

        JeuDtoOut result = jeuService.recupererUnJeuParId(1L);

        assertThat(result).isNotNull();
        assertThat(result.nom()).isEqualTo("Baldur's Gate 3");
        verify(jeuPort, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when game not found by id")
    void testRecupererUnJeuParIdNotFound() {
        when(jeuPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jeuService.recupererUnJeuParId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Jeu non trouvé");
    }

    @Test
    @DisplayName("Should retrieve all games")
    void testRecupererTousLesJeux() {
        Jeu jeu2 = new Jeu(2L, "Elden Ring", "Action RPG", genre, "image2.jpg", 59.99f,
                LocalDate.of(2022, 2, 25), editeur, classification, List.of(), null);

        when(jeuPort.findAll()).thenReturn(List.of(jeu, jeu2));
        when(avisPort.findNoteMoyenneByJeuId(anyLong())).thenReturn(Optional.empty());
        when(jeuMapper.toDto(jeu)).thenReturn(new JeuDtoOut(1L, "Baldur's Gate 3", "A fantasy RPG", "image.jpg", 59.99f, LocalDate.of(2023, 8, 3), "RPG", "Ubisoft", "PEGI 12", List.of(), null));
        when(jeuMapper.toDto(jeu2)).thenReturn(new JeuDtoOut(2L, "Elden Ring", "Action RPG", "image2.jpg", 59.99f, LocalDate.of(2022, 2, 25), "RPG", "Ubisoft", "PEGI 12", List.of(), null));

        List<JeuDtoOut> result = jeuService.recupererTousLesJeux();

        assertThat(result).hasSize(2);
        assertThat(result).extracting("nom").contains("Baldur's Gate 3", "Elden Ring");
        verify(jeuPort, times(1)).findAll();
    }

    @Test
    @DisplayName("Should retrieve games by genre id")
    void testRecupererDesJeuxDUnGenre() {
        when(jeuPort.findAllByGenreId(1L)).thenReturn(List.of(jeu));
        when(avisPort.findNoteMoyenneByJeuId(anyLong())).thenReturn(Optional.empty());
        when(jeuMapper.toDto(jeu)).thenReturn(new JeuDtoOut(1L, "Baldur's Gate 3", "A fantasy RPG", "image.jpg", 59.99f, LocalDate.of(2023, 8, 3), "RPG", "Ubisoft", "PEGI 12", List.of(), null));

        List<JeuDtoOut> result = jeuService.recupererDesJeuxDUnGenre(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).nom()).isEqualTo("Baldur's Gate 3");
        verify(jeuPort, times(1)).findAllByGenreId(1L);
    }

    @Test
    @DisplayName("Should retrieve games by publisher id")
    void testRecupererDesJeuxDUnEditeur() {
        when(jeuPort.findAllByEditeurId(1L)).thenReturn(List.of(jeu));
        when(avisPort.findNoteMoyenneByJeuId(anyLong())).thenReturn(Optional.empty());
        when(jeuMapper.toDto(jeu)).thenReturn(new JeuDtoOut(1L, "Baldur's Gate 3", "A fantasy RPG", "image.jpg", 59.99f, LocalDate.of(2023, 8, 3), "RPG", "Ubisoft", "PEGI 12", List.of(), null));

        List<JeuDtoOut> result = jeuService.recupererDesJeuxDUnEditeur(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).nom()).isEqualTo("Baldur's Gate 3");
        verify(jeuPort, times(1)).findAllByEditeurId(1L);
    }

    @Test
    @DisplayName("Should delete a game")
    void testSupprimerUnJeu() {
        jeuService.supprimerUnJeu(1L);

        verify(jeuPort, times(1)).deleteById(1L);
    }
}
