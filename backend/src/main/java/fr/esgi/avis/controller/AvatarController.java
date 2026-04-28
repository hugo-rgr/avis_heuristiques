package fr.esgi.avis.controller;

import fr.esgi.avis.dto.in.AvatarDtoIn;
import fr.esgi.avis.dto.out.AvatarDtoOut;
import fr.esgi.avis.port.in.AvatarUseCase;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avatars")
public class AvatarController {

    private final AvatarUseCase avatarUseCase;

    public AvatarController(AvatarUseCase avatarUseCase) {
        this.avatarUseCase = avatarUseCase;
    }

    @PostMapping
    public ResponseEntity<AvatarDtoOut> creerUnAvatar(@Valid @RequestBody AvatarDtoIn dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(avatarUseCase.creerUnAvatar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvatarDtoOut> mettreAJour(@PathVariable Long id, @Valid @RequestBody AvatarDtoIn dto) {
        return ResponseEntity.ok(avatarUseCase.mettreAJour(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvatarDtoOut> recupererUnAvatarParId(@PathVariable Long id) {
        return ResponseEntity.ok(avatarUseCase.recupererUnAvatarParId(id));
    }

    @GetMapping
    public ResponseEntity<List<AvatarDtoOut>> recupererTousLesAvatars() {
        return ResponseEntity.ok(avatarUseCase.recupererTousLesAvatars());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<AvatarDtoOut>> recupererTousLesAvatarsPagine(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(avatarUseCase.recupererTousLesAvatars(pageable));
    }

    @GetMapping("/joueur/{joueurId}")
    public ResponseEntity<List<AvatarDtoOut>> recupererAvatarsParJoueur(@PathVariable Long joueurId) {
        return ResponseEntity.ok(avatarUseCase.recupererAvatarsParJoueur(joueurId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerUnAvatar(@PathVariable Long id) {
        avatarUseCase.supprimerUnAvatar(id);
        return ResponseEntity.noContent().build();
    }
}
