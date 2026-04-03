package fr.esgi.avis.controller;

import fr.esgi.avis.dto.in.AvisDtoIn;
import fr.esgi.avis.dto.out.AvisDtoOut;
import fr.esgi.avis.use_case.AvisUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AvisDtoOut> creerUnAvis(@RequestBody AvisDtoIn dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(avisUseCase.creerUnAvis(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvisDtoOut> mettreAJourUnAvis(@PathVariable Long id, @RequestBody AvisDtoIn dto) {
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

    @GetMapping("/jeu/{jeuId}")
    public ResponseEntity<List<AvisDtoOut>> recupererTousLesAvisParJeu(@PathVariable Long jeuId) {
        return ResponseEntity.ok(avisUseCase.recupererTousLesAvisParJeu(jeuId));
    }

    @GetMapping("/joueur/{joueurId}")
    public ResponseEntity<List<AvisDtoOut>> recupererTousLesAvisParJoueur(@PathVariable Long joueurId) {
        return ResponseEntity.ok(avisUseCase.recupererTousLesAvisParJoueur(joueurId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerUnAvis(@PathVariable Long id) {
        avisUseCase.supprimerUnAvis(id);
        return ResponseEntity.noContent().build();
    }
}
