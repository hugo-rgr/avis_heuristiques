package fr.esgi.avis.controller;

import fr.esgi.avis.dto.in.EditeurDtoIn;
import fr.esgi.avis.dto.out.EditeurDtoOut;
import fr.esgi.avis.port.in.EditeurUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/editeurs")
public class EditeurController {

    private final EditeurUseCase editeurUseCase;

    public EditeurController(EditeurUseCase editeurUseCase) {
        this.editeurUseCase = editeurUseCase;
    }

    @PostMapping
    @PreAuthorize("hasRole('MODERATEUR')")
    public ResponseEntity<EditeurDtoOut> creerUnEditeur(@Valid @RequestBody EditeurDtoIn dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(editeurUseCase.creerUnEditeur(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATEUR')")
    public ResponseEntity<EditeurDtoOut> mettreAJour(@PathVariable Long id, @Valid @RequestBody EditeurDtoIn dto) {
        return ResponseEntity.ok(editeurUseCase.mettreAJour(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EditeurDtoOut> recupererUnEditeurParId(@PathVariable Long id) {
        return ResponseEntity.ok(editeurUseCase.recupererUnEditeurParId(id));
    }

    @GetMapping
    public ResponseEntity<List<EditeurDtoOut>> recupererTousLesEditeurs() {
        return ResponseEntity.ok(editeurUseCase.recupererTousLesEditeurs());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATEUR')")
    public ResponseEntity<Void> supprimerUnEditeur(@PathVariable Long id) {
        editeurUseCase.supprimerUnEditeur(id);
        return ResponseEntity.noContent().build();
    }
}
