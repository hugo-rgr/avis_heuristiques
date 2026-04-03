package fr.esgi.avis.service;

import fr.esgi.avis.business.Classification;
import fr.esgi.avis.dto.in.ClassificationDtoIn;
import fr.esgi.avis.dto.out.ClassificationDtoOut;
import fr.esgi.avis.mapper.ClassificationMapper;
import fr.esgi.avis.port.out.ClassificationPort;
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
@DisplayName("ClassificationService Tests")
class ClassificationServiceTest {

    @Mock
    private ClassificationPort classificationPort;

    @Mock
    private ClassificationMapper classificationMapper;

    @InjectMocks
    private ClassificationService classificationService;

    private Classification classification;
    private ClassificationDtoIn classificationDtoIn;

    @BeforeEach
    void setUp() {
        classificationDtoIn = new ClassificationDtoIn(null, "PEGI 12", "FFFFFF");
        classification = new Classification(1L, "PEGI 12", List.of(), "FFFFFF");
    }

    @Test
    @DisplayName("Should create a new classification")
    void testCreerUneClassification() {
        when(classificationMapper.toDomain(any(ClassificationDtoIn.class))).thenReturn(classification);
        when(classificationPort.save(any(Classification.class))).thenReturn(classification);
        when(classificationMapper.toDto(any(Classification.class))).thenReturn(new ClassificationDtoOut(1L, "PEGI 12", "FFFFFF"));

        ClassificationDtoOut result = classificationService.creerUneClassification(classificationDtoIn);

        assertThat(result).isNotNull();
        assertThat(result.nom()).isEqualTo("PEGI 12");
        verify(classificationPort, times(1)).save(any(Classification.class));
    }

    @Test
    @DisplayName("Should update an existing classification")
    void testMettreAJourUneClassification() {
        ClassificationDtoIn updateDto = new ClassificationDtoIn(1L, "PEGI 18", "000000");
        Classification updatedClassification = new Classification(1L, "PEGI 18", List.of(), "000000");

        when(classificationPort.findById(1L)).thenReturn(Optional.of(classification));
        when(classificationPort.save(any(Classification.class))).thenReturn(updatedClassification);
        when(classificationMapper.toDto(any(Classification.class))).thenReturn(new ClassificationDtoOut(1L, "PEGI 18", "000000"));

        ClassificationDtoOut result = classificationService.mettreAJourUneClassification(1L, updateDto);

        assertThat(result).isNotNull();
        assertThat(result.nom()).isEqualTo("PEGI 18");
        verify(classificationPort, times(1)).findById(1L);
        verify(classificationPort, times(1)).save(any(Classification.class));
    }

    @Test
    @DisplayName("Should throw exception when classification not found")
    void testMettreAJourUneClassificationNotFound() {
        when(classificationPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> classificationService.mettreAJourUneClassification(999L, classificationDtoIn))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Classification non trouvée");
    }

    @Test
    @DisplayName("Should retrieve classification by id")
    void testRecupererUneClassificationParId() {
        when(classificationPort.findById(1L)).thenReturn(Optional.of(classification));
        when(classificationMapper.toDto(any(Classification.class))).thenReturn(new ClassificationDtoOut(1L, "PEGI 12", "FFFFFF"));

        ClassificationDtoOut result = classificationService.recupererUneClassificationParId(1L);

        assertThat(result).isNotNull();
        assertThat(result.nom()).isEqualTo("PEGI 12");
        verify(classificationPort, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when classification not found by id")
    void testRecupererUneClassificationParIdNotFound() {
        when(classificationPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> classificationService.recupererUneClassificationParId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Classification non trouvée");
    }

    @Test
    @DisplayName("Should retrieve all classifications")
    void testRecupererToutesLesClassifications() {
        Classification classification2 = new Classification(2L, "PEGI 16", List.of(), "CCCCCC");
        when(classificationPort.findAll()).thenReturn(List.of(classification, classification2));
        when(classificationMapper.toDto(classification)).thenReturn(new ClassificationDtoOut(1L, "PEGI 12", "FFFFFF"));
        when(classificationMapper.toDto(classification2)).thenReturn(new ClassificationDtoOut(2L, "PEGI 16", "CCCCCC"));

        List<ClassificationDtoOut> result = classificationService.recupererToutesLesClassifications();

        assertThat(result).hasSize(2);
        assertThat(result).extracting("nom").contains("PEGI 12", "PEGI 16");
        verify(classificationPort, times(1)).findAll();
    }

    @Test
    @DisplayName("Should delete a classification")
    void testSupprimerUneClassification() {
        classificationService.supprimerUneClassification(1L);

        verify(classificationPort, times(1)).deleteById(1L);
    }
}
