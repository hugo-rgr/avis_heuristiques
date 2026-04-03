package fr.esgi.avis.controller;

import fr.esgi.avis.dto.in.PlatefomeDtoIn;
import fr.esgi.avis.dto.out.PlatefomeDtoOut;
import fr.esgi.avis.port.in.PlateformeUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plateformes")
public class PlateformeController {

    private final PlateformeUseCase plateformeUseCase;

    public PlateformeController(PlateformeUseCase plateformeUseCase) {
        this.plateformeUseCase = plateformeUseCase;
    }

    @PostMapping
    public ResponseEntity<PlatefomeDtoOut> creerUnePlateforme(@RequestBody PlatefomeDtoIn dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(plateformeUseCase.creerUnePlateforme(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlatefomeDtoOut> mettreAJour(@PathVariable Long id, @RequestBody PlatefomeDtoIn dto) {
        return ResponseEntity.ok(plateformeUseCase.mettreAJour(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatefomeDtoOut> recupererUnePlateformeParId(@PathVariable Long id) {
        return ResponseEntity.ok(plateformeUseCase.recupererUnePlateformeParId(id));
    }

    @GetMapping
    public ResponseEntity<List<PlatefomeDtoOut>> recupererToutesLesPlateformes() {
        return ResponseEntity.ok(plateformeUseCase.recupererToutesLesPlateformes());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerUnePlateforme(@PathVariable Long id) {
        plateformeUseCase.supprimerUnePlateforme(id);
        return ResponseEntity.noContent().build();
    }
}
