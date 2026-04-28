package fr.esgi.avis.controller;

import fr.esgi.avis.dto.in.GenreDtoIn;
import fr.esgi.avis.dto.out.GenreDtoOut;
import fr.esgi.avis.port.in.GenreUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreUseCase genreUseCase;

    public GenreController(GenreUseCase genreUseCase) {
        this.genreUseCase = genreUseCase;
    }

    @PostMapping
    @PreAuthorize("hasRole('MODERATEUR')")
    public ResponseEntity<GenreDtoOut> creerUnGenre(@Valid @RequestBody GenreDtoIn dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(genreUseCase.creerUnGenre(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATEUR')")
    public ResponseEntity<GenreDtoOut> mettreAJourUnGenre(@PathVariable Long id, @Valid @RequestBody GenreDtoIn dto) {
        return ResponseEntity.ok(genreUseCase.mettreAJourUnGenre(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDtoOut> recupererUnGenreParId(@PathVariable Long id) {
        return ResponseEntity.ok(genreUseCase.recupererUnGenreParId(id));
    }

    @GetMapping
    public ResponseEntity<List<GenreDtoOut>> recupererTouslesGenre() {
        return ResponseEntity.ok(genreUseCase.recupererTouslesGenre());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATEUR')")
    public ResponseEntity<Void> supprimerUnGenre(@PathVariable Long id) {
        genreUseCase.supprimerUnGenre(id);
        return ResponseEntity.noContent().build();
    }
}
