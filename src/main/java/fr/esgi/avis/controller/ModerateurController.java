package fr.esgi.avis.controller;

import fr.esgi.avis.dto.in.JeuDtoIn;
import fr.esgi.avis.dto.out.JeuDtoOut;
import fr.esgi.avis.dto.out.ModerateurDtoOut;
import fr.esgi.avis.port.in.ModerateurUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/moderateurs")
public class ModerateurController {

    private final ModerateurUseCase moderateurUseCase;

    public ModerateurController(ModerateurUseCase moderateurUseCase) {
        this.moderateurUseCase = moderateurUseCase;
    }

    @PostMapping("/connexion")
    public ResponseEntity<ModerateurDtoOut> seConnecter(@RequestParam String email, @RequestParam String motDePasse) {
        return ResponseEntity.ok(moderateurUseCase.seConnecter(email, motDePasse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModerateurDtoOut> trouverParId(@PathVariable Long id) {
        return ResponseEntity.ok(moderateurUseCase.trouverParId(id));
    }

    @PostMapping("/{id}/jeux")
    public ResponseEntity<JeuDtoOut> ajouterJeu(@PathVariable Long id, @RequestBody JeuDtoIn dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(moderateurUseCase.ajouterJeu(id, dto));
    }

    @DeleteMapping("/{id}/avis/{avisId}")
    public ResponseEntity<Void> supprimerAvis(@PathVariable Long id, @PathVariable Long avisId) {
        moderateurUseCase.supprimerAvis(id, avisId);
        return ResponseEntity.noContent().build();
    }
}
