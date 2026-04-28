package fr.esgi.avis.controller;

import fr.esgi.avis.dto.in.JeuDtoIn;
import fr.esgi.avis.dto.out.JeuDtoOut;
import fr.esgi.avis.port.in.JeuUseCase;
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
@RequestMapping("/api/jeux")
public class JeuController {

    private final JeuUseCase jeuUseCase;

    public JeuController(JeuUseCase jeuUseCase) {
        this.jeuUseCase = jeuUseCase;
    }

    @PostMapping
    @PreAuthorize("hasRole('MODERATEUR')")
    public ResponseEntity<JeuDtoOut> creerUnJeu(@Valid @RequestBody JeuDtoIn dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jeuUseCase.creerUnJeu(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATEUR')")
    public ResponseEntity<JeuDtoOut> mettreAJourUnJeu(@PathVariable Long id, @Valid @RequestBody JeuDtoIn dto) {
        return ResponseEntity.ok(jeuUseCase.mettreAJourUnJeu(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JeuDtoOut> recupererUnJeuParId(@PathVariable Long id) {
        return ResponseEntity.ok(jeuUseCase.recupererUnJeuParId(id));
    }

    @GetMapping
    public ResponseEntity<List<JeuDtoOut>> recupererTousLesJeux() {
        return ResponseEntity.ok(jeuUseCase.recupererTousLesJeux());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<JeuDtoOut>> recupererTousLesJeuxPagine(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(jeuUseCase.recupererTousLesJeux(pageable));
    }

    @GetMapping("/genre/{genreId}")
    public ResponseEntity<List<JeuDtoOut>> recupererDesJeuxDUnGenre(@PathVariable Long genreId) {
        return ResponseEntity.ok(jeuUseCase.recupererDesJeuxDUnGenre(genreId));
    }

    @GetMapping("/editeur/{editeurId}")
    public ResponseEntity<List<JeuDtoOut>> recupererDesJeuxDUnEditeur(@PathVariable Long editeurId) {
        return ResponseEntity.ok(jeuUseCase.recupererDesJeuxDUnEditeur(editeurId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATEUR')")
    public ResponseEntity<Void> supprimerUnJeu(@PathVariable Long id) {
        jeuUseCase.supprimerUnJeu(id);
        return ResponseEntity.noContent().build();
    }
}
