package fr.esgi.avis.controller;

import fr.esgi.avis.dto.in.ClassificationDtoIn;
import fr.esgi.avis.dto.out.ClassificationDtoOut;
import fr.esgi.avis.port.in.ClassificationUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classifications")
public class ClassificationController {

    private final ClassificationUseCase classificationUseCase;

    public ClassificationController(ClassificationUseCase classificationUseCase) {
        this.classificationUseCase = classificationUseCase;
    }

    @PostMapping
    public ResponseEntity<ClassificationDtoOut> creerUneClassification(@RequestBody ClassificationDtoIn dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(classificationUseCase.creerUneClassification(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassificationDtoOut> mettreAJourUneClassification(@PathVariable Long id, @RequestBody ClassificationDtoIn dto) {
        return ResponseEntity.ok(classificationUseCase.mettreAJourUneClassification(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassificationDtoOut> recupererUneClassificationParId(@PathVariable Long id) {
        return ResponseEntity.ok(classificationUseCase.recupererUneClassificationParId(id));
    }

    @GetMapping
    public ResponseEntity<List<ClassificationDtoOut>> recupererToutesLesClassifications() {
        return ResponseEntity.ok(classificationUseCase.recupererToutesLesClassifications());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerUneClassification(@PathVariable Long id) {
        classificationUseCase.supprimerUneClassification(id);
        return ResponseEntity.noContent().build();
    }
}
