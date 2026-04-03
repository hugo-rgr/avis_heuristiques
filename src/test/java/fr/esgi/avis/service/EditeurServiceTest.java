package fr.esgi.avis.service;

import fr.esgi.avis.business.Editeur;
import fr.esgi.avis.dto.in.EditeurDtoIn;
import fr.esgi.avis.dto.out.EditeurDtoOut;
import fr.esgi.avis.mapper.EditeurMapper;
import fr.esgi.avis.port.out.EditeurPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EditeurService Tests")
class EditeurServiceTest {

    @Mock
    private EditeurPort editeurPort;

    @Mock
    private EditeurMapper editeurMapper;

    @InjectMocks
    private EditeurService editeurService;

    private Editeur editeur;
    private EditeurDtoIn editeurDtoIn;

    @BeforeEach
    void setUp() {
        editeurDtoIn = new EditeurDtoIn(null, "Ubisoft");
        editeur = new Editeur(1L, "Ubisoft", List.of());
    }

    @Test
    @DisplayName("Should create a new publisher")
    void testCreerUnEditeur() {
        when(editeurMapper.toDomain(any(EditeurDtoIn.class))).thenReturn(editeur);
        when(editeurPort.save(any(Editeur.class))).thenReturn(editeur);
        when(editeurMapper.toDto(any(Editeur.class))).thenReturn(new EditeurDtoOut(1L, "Ubisoft"));

        EditeurDtoOut result = editeurService.creerUnEditeur(editeurDtoIn);

        assertThat(result).isNotNull();
        assertThat(result.nom()).isEqualTo("Ubisoft");
        verify(editeurPort, times(1)).save(any(Editeur.class));
    }

    @Test
    @DisplayName("Should update an existing publisher")
    void testMettreAJour() {
        EditeurDtoIn updateDto = new EditeurDtoIn(1L, "Rockstar Games");
        Editeur updatedEditeur = new Editeur(1L, "Rockstar Games", List.of());

        when(editeurPort.findById(1L)).thenReturn(Optional.of(editeur));
        when(editeurPort.save(any(Editeur.class))).thenReturn(updatedEditeur);
        when(editeurMapper.toDto(any(Editeur.class))).thenReturn(new EditeurDtoOut(1L, "Rockstar Games"));

        EditeurDtoOut result = editeurService.mettreAJour(1L, updateDto);

        assertThat(result).isNotNull();
        assertThat(result.nom()).isEqualTo("Rockstar Games");
        verify(editeurPort, times(1)).findById(1L);
        verify(editeurPort, times(1)).save(any(Editeur.class));
    }

    @Test
    @DisplayName("Should throw exception when publisher not found for update")
    void testMettreAJourNotFound() {
        when(editeurPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> editeurService.mettreAJour(999L, editeurDtoIn))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Editeur non trouvé");
    }

    @Test
    @DisplayName("Should retrieve publisher by id")
    void testRecupererUnEditeurParId() {
        when(editeurPort.findById(1L)).thenReturn(Optional.of(editeur));
        when(editeurMapper.toDto(any(Editeur.class))).thenReturn(new EditeurDtoOut(1L, "Ubisoft"));

        EditeurDtoOut result = editeurService.recupererUnEditeurParId(1L);

        assertThat(result).isNotNull();
        assertThat(result.nom()).isEqualTo("Ubisoft");
        verify(editeurPort, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when publisher not found by id")
    void testRecupererUnEditeurParIdNotFound() {
        when(editeurPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> editeurService.recupererUnEditeurParId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Editeur non trouvé");
    }

    @Test
    @DisplayName("Should retrieve all publishers")
    void testRecupererTousLesEditeurs() {
        Editeur editeur2 = new Editeur(2L, "EA Sports", List.of());
        when(editeurPort.findAll()).thenReturn(List.of(editeur, editeur2));
        when(editeurMapper.toDto(editeur)).thenReturn(new EditeurDtoOut(1L, "Ubisoft"));
        when(editeurMapper.toDto(editeur2)).thenReturn(new EditeurDtoOut(2L, "EA Sports"));

        List<EditeurDtoOut> result = editeurService.recupererTousLesEditeurs();

        assertThat(result).hasSize(2);
        assertThat(result).extracting("nom").contains("Ubisoft", "EA Sports");
        verify(editeurPort, times(1)).findAll();
    }

    @Test
    @DisplayName("Should delete a publisher")
    void testSupprimerUnEditeur() {
        editeurService.supprimerUnEditeur(1L);

        verify(editeurPort, times(1)).deleteById(1L);
    }
}
