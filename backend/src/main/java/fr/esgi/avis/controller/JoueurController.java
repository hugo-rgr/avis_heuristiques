package fr.esgi.avis.controller;

import fr.esgi.avis.dto.in.AvisDtoIn;
import fr.esgi.avis.dto.in.JoueurDtoIn;
import fr.esgi.avis.dto.out.AvisDtoOut;
import fr.esgi.avis.dto.out.JoueurDtoOut;
import fr.esgi.avis.port.in.JoueurUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/joueurs")
public class JoueurController {

    private final JoueurUseCase joueurUseCase;

    public JoueurController(JoueurUseCase joueurUseCase) {
        this.joueurUseCase = joueurUseCase;
    }

    @PostMapping("/inscription")
    public ResponseEntity<JoueurDtoOut> sInscrire(@RequestBody JoueurDtoIn dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(joueurUseCase.sInscrire(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JoueurDtoOut> trouverParId(@PathVariable Long id) {
        return ResponseEntity.ok(joueurUseCase.trouverParId(id));
    }

    @GetMapping("/{id}/avis")
    public ResponseEntity<List<AvisDtoOut>> listerAvisDuJoueur(@PathVariable Long id) {
        return ResponseEntity.ok(joueurUseCase.listerAvisDuJoueur(id));
    }

    @PostMapping("/{id}/avis")
    public ResponseEntity<AvisDtoOut> redigerUnAvis(@PathVariable Long id, @RequestBody AvisDtoIn dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(joueurUseCase.redigerUnAvis(id, dto));
    }
}
