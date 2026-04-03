package fr.esgi.avis.service;

import fr.esgi.avis.business.Plateforme;
import fr.esgi.avis.dto.in.PlatefomeDtoIn;
import fr.esgi.avis.dto.out.PlatefomeDtoOut;
import fr.esgi.avis.port.out.PlateformePort;
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
@DisplayName("PlateformeService Tests")
class PlateformeServiceTest {

    @Mock
    private PlateformePort plateformePort;

    @InjectMocks
    private PlateformeService plateformeService;

    private Plateforme plateforme;
    private PlatefomeDtoIn platefomeDtoIn;

    @BeforeEach
    void setUp() {
        platefomeDtoIn = new PlatefomeDtoIn("PlayStation 5", LocalDate.of(2020, 11, 12));
        plateforme = new Plateforme(1L, "PlayStation 5", List.of(), LocalDate.of(2020, 11, 12));
    }

    @Test
    @DisplayName("Should create a new platform")
    void testCreerUnePlateforme() {
        when(plateformePort.save(any(Plateforme.class))).thenReturn(plateforme);

        PlatefomeDtoOut result = plateformeService.creerUnePlateforme(platefomeDtoIn);

        assertThat(result).isNotNull();
        assertThat(result.nom()).isEqualTo("PlayStation 5");
        verify(plateformePort, times(1)).save(any(Plateforme.class));
    }

    @Test
    @DisplayName("Should update an existing platform")
    void testMettreAJour() {
        PlatefomeDtoIn updateDto = new PlatefomeDtoIn("PlayStation 5 Pro", LocalDate.of(2024, 11, 7));
        Plateforme updatedPlateforme = new Plateforme(1L, "PlayStation 5 Pro", List.of(), LocalDate.of(2024, 11, 7));

        when(plateformePort.findById(1L)).thenReturn(Optional.of(plateforme));
        when(plateformePort.save(any(Plateforme.class))).thenReturn(updatedPlateforme);

        PlatefomeDtoOut result = plateformeService.mettreAJour(1L, updateDto);

        assertThat(result).isNotNull();
        assertThat(result.nom()).isEqualTo("PlayStation 5 Pro");
        verify(plateformePort, times(1)).findById(1L);
        verify(plateformePort, times(1)).save(any(Plateforme.class));
    }

    @Test
    @DisplayName("Should throw exception when platform not found for update")
    void testMettreAJourNotFound() {
        when(plateformePort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> plateformeService.mettreAJour(999L, platefomeDtoIn))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Plateforme non trouvée");
    }

    @Test
    @DisplayName("Should retrieve platform by id")
    void testRecupererUnePlateformeParId() {
        when(plateformePort.findById(1L)).thenReturn(Optional.of(plateforme));

        PlatefomeDtoOut result = plateformeService.recupererUnePlateformeParId(1L);

        assertThat(result).isNotNull();
        assertThat(result.nom()).isEqualTo("PlayStation 5");
        verify(plateformePort, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when platform not found by id")
    void testRecupererUnePlateformeParIdNotFound() {
        when(plateformePort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> plateformeService.recupererUnePlateformeParId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Plateforme non trouvée");
    }

    @Test
    @DisplayName("Should retrieve all platforms")
    void testRecupererToutesLesPlateformes() {
        Plateforme plateforme2 = new Plateforme(2L, "Xbox Series X", List.of(), LocalDate.of(2020, 11, 10));
        when(plateformePort.findAll()).thenReturn(List.of(plateforme, plateforme2));

        List<PlatefomeDtoOut> result = plateformeService.recupererToutesLesPlateformes();

        assertThat(result).hasSize(2);
        assertThat(result).extracting("nom").contains("PlayStation 5", "Xbox Series X");
        verify(plateformePort, times(1)).findAll();
    }

    @Test
    @DisplayName("Should delete a platform")
    void testSupprimerUnePlateforme() {
        plateformeService.supprimerUnePlateforme(1L);

        verify(plateformePort, times(1)).deleteById(1L);
    }
}

