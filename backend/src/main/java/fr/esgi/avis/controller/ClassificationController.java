package fr.esgi.avis.controller;

import fr.esgi.avis.dto.in.ClassificationDtoIn;
import fr.esgi.avis.dto.out.ClassificationDtoOut;
import fr.esgi.avis.port.in.ClassificationUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('MODERATEUR')")
    public ResponseEntity<ClassificationDtoOut> creerUneClassification(@Valid @RequestBody ClassificationDtoIn dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(classificationUseCase.creerUneClassification(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATEUR')")
    public ResponseEntity<ClassificationDtoOut> mettreAJourUneClassification(@PathVariable Long id, @Valid @RequestBody ClassificationDtoIn dto) {
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
    @PreAuthorize("hasRole('MODERATEUR')")
    public ResponseEntity<Void> supprimerUneClassification(@PathVariable Long id) {
        classificationUseCase.supprimerUneClassification(id);
        return ResponseEntity.noContent().build();
    }
}
