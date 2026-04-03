package fr.esgi.avis.service;

import fr.esgi.avis.business.Avatar;
import fr.esgi.avis.business.Joueur;
import fr.esgi.avis.dto.in.AvatarDtoIn;
import fr.esgi.avis.dto.out.AvatarDtoOut;
import fr.esgi.avis.port.out.AvatarPort;
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
@DisplayName("AvatarService Tests")
class AvatarServiceTest {

    @Mock
    private AvatarPort avatarPort;

    @Mock
    private JoueurPort joueurPort;

    @InjectMocks
    private AvatarService avatarService;

    private Avatar avatar;
    private Joueur joueur;
    private AvatarDtoIn avatarDtoIn;

    @BeforeEach
    void setUp() {
        joueur = new Joueur(List.of(), LocalDate.of(1995, 5, 15), null);
        joueur.setId(1L);
        joueur.setPseudo("gamer123");

        avatarDtoIn = new AvatarDtoIn("Knight Avatar", 1L);
        avatar = new Avatar(1L, "Knight Avatar", joueur);
    }

    @Test
    @DisplayName("Should create a new avatar")
    void testCreerUnAvatar() {
        when(joueurPort.findById(1L)).thenReturn(Optional.of(joueur));
        when(avatarPort.save(any(Avatar.class))).thenReturn(avatar);

        AvatarDtoOut result = avatarService.creerUnAvatar(avatarDtoIn);

        assertThat(result).isNotNull();
        assertThat(result.nom()).isEqualTo("Knight Avatar");
        assertThat(result.joueurPseudo()).isEqualTo("gamer123");
        verify(joueurPort, times(1)).findById(1L);
        verify(avatarPort, times(1)).save(any(Avatar.class));
    }

    @Test
    @DisplayName("Should throw exception when player not found during avatar creation")
    void testCreerUnAvatarJoueurNotFound() {
        when(joueurPort.findById(999L)).thenReturn(Optional.empty());

        AvatarDtoIn badDto = new AvatarDtoIn("Knight Avatar", 999L);
        assertThatThrownBy(() -> avatarService.creerUnAvatar(badDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Joueur non trouvé");
    }

    @Test
    @DisplayName("Should update an existing avatar")
    void testMettreAJour() {
        Avatar updatedAvatar = new Avatar(1L, "Wizard Avatar", joueur);
        AvatarDtoIn updateDto = new AvatarDtoIn("Wizard Avatar", 1L);

        when(avatarPort.findById(1L)).thenReturn(Optional.of(avatar));
        when(joueurPort.findById(1L)).thenReturn(Optional.of(joueur));
        when(avatarPort.save(any(Avatar.class))).thenReturn(updatedAvatar);

        AvatarDtoOut result = avatarService.mettreAJour(1L, updateDto);

        assertThat(result).isNotNull();
        assertThat(result.nom()).isEqualTo("Wizard Avatar");
        verify(avatarPort, times(1)).findById(1L);
        verify(avatarPort, times(1)).save(any(Avatar.class));
    }

    @Test
    @DisplayName("Should throw exception when avatar not found for update")
    void testMettreAJourAvatarNotFound() {
        when(avatarPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> avatarService.mettreAJour(999L, avatarDtoIn))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Avatar non trouvé");
    }

    @Test
    @DisplayName("Should retrieve avatar by id")
    void testRecupererUnAvatarParId() {
        when(avatarPort.findById(1L)).thenReturn(Optional.of(avatar));

        AvatarDtoOut result = avatarService.recupererUnAvatarParId(1L);

        assertThat(result).isNotNull();
        assertThat(result.nom()).isEqualTo("Knight Avatar");
        verify(avatarPort, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when avatar not found by id")
    void testRecupererUnAvatarParIdNotFound() {
        when(avatarPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> avatarService.recupererUnAvatarParId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Avatar non trouvé");
    }

    @Test
    @DisplayName("Should retrieve all avatars")
    void testRecupererTousLesAvatars() {
        Joueur joueur2 = new Joueur(List.of(), LocalDate.of(2000, 1, 1), null);
        joueur2.setId(2L);
        joueur2.setPseudo("player456");

        Avatar avatar2 = new Avatar(2L, "Dragon Avatar", joueur2);
        when(avatarPort.findAll()).thenReturn(List.of(avatar, avatar2));

        List<AvatarDtoOut> result = avatarService.recupererTousLesAvatars();

        assertThat(result).hasSize(2);
        assertThat(result).extracting("nom").contains("Knight Avatar", "Dragon Avatar");
        verify(avatarPort, times(1)).findAll();
    }

    @Test
    @DisplayName("Should retrieve avatars by player id")
    void testRecupererAvatarsParJoueur() {
        when(avatarPort.findAllByJoueurId(1L)).thenReturn(List.of(avatar));

        List<AvatarDtoOut> result = avatarService.recupererAvatarsParJoueur(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).nom()).isEqualTo("Knight Avatar");
        verify(avatarPort, times(1)).findAllByJoueurId(1L);
    }

    @Test
    @DisplayName("Should delete an avatar")
    void testSupprimerUnAvatar() {
        avatarService.supprimerUnAvatar(1L);

        verify(avatarPort, times(1)).deleteById(1L);
    }
}

