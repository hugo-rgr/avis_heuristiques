package fr.esgi.avis.controller;

import fr.esgi.avis.dto.in.AvisDtoIn;
import fr.esgi.avis.dto.out.AvisDtoOut;
import fr.esgi.avis.port.in.AvisUseCase;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avis")
public class AvisController {

    private final AvisUseCase avisUseCase;

    public AvisController(AvisUseCase avisUseCase) {
        this.avisUseCase = avisUseCase;
    }

    @PostMapping
    @PreAuthorize("hasRole('JOUEUR')")
    public ResponseEntity<AvisDtoOut> creerUnAvis(@Valid @RequestBody AvisDtoIn dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(avisUseCase.creerUnAvis(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('JOUEUR')")
    public ResponseEntity<AvisDtoOut> mettreAJourUnAvis(@PathVariable Long id, @Valid @RequestBody AvisDtoIn dto) {
        return ResponseEntity.ok(avisUseCase.mettreAJourUnAvis(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvisDtoOut> recupererUnAvisParId(@PathVariable Long id) {
        return ResponseEntity.ok(avisUseCase.recupererUnAvisParId(id));
    }

    @GetMapping
    public ResponseEntity<List<AvisDtoOut>> recupererTousLesAvis() {
        return ResponseEntity.ok(avisUseCase.recupererTousLesAvis());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<AvisDtoOut>> recupererTousLesAvisPagine(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(avisUseCase.recupererTousLesAvis(pageable));
    }

    @GetMapping("/jeu/{jeuId}")
    public ResponseEntity<List<AvisDtoOut>> recupererTousLesAvisParJeu(@PathVariable Long jeuId) {
        return ResponseEntity.ok(avisUseCase.recupererTousLesAvisParJeu(jeuId));
    }

    @GetMapping("/jeu/{jeuId}/page")
    public ResponseEntity<Page<AvisDtoOut>> recupererTousLesAvisParJeuPagine(
            @PathVariable Long jeuId, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(avisUseCase.recupererTousLesAvisParJeu(jeuId, pageable));
    }

    @GetMapping("/joueur/{joueurId}")
    public ResponseEntity<List<AvisDtoOut>> recupererTousLesAvisParJoueur(@PathVariable Long joueurId) {
        return ResponseEntity.ok(avisUseCase.recupererTousLesAvisParJoueur(joueurId));
    }

    @GetMapping("/joueur/{joueurId}/page")
    public ResponseEntity<Page<AvisDtoOut>> recupererTousLesAvisParJoueurPagine(
            @PathVariable Long joueurId, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(avisUseCase.recupererTousLesAvisParJoueur(joueurId, pageable));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATEUR')")
    public ResponseEntity<Void> supprimerUnAvis(@PathVariable Long id) {
        avisUseCase.supprimerUnAvis(id);
        return ResponseEntity.noContent().build();
    }
}
