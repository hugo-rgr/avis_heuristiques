package fr.esgi.avis.service;

import fr.esgi.avis.business.Editeur;
import fr.esgi.avis.dto.in.EditeurDtoIn;
import fr.esgi.avis.dto.out.EditeurDtoOut;
import fr.esgi.avis.port.out.EditeurPort;
import fr.esgi.avis.port.in.EditeurUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class EditeurService implements EditeurUseCase {

    private final EditeurPort editeurPort;

    public EditeurService(EditeurPort editeurPort) {
        this.editeurPort = editeurPort;
    }

    @Override
    public EditeurDtoOut creerUnEditeur(EditeurDtoIn dto) {
        Editeur editeur = new Editeur(null, dto.nom(), Collections.emptyList());
        return toDto(editeurPort.save(editeur));
    }

    @Override
    public EditeurDtoOut mettreAJour(Long id, EditeurDtoIn dto) {
        Editeur existing = editeurPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Editeur non trouvé : " + id));
        existing.setNom(dto.nom());
        return toDto(editeurPort.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public EditeurDtoOut recupererUnEditeurParId(Long id) {
        return editeurPort.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Editeur non trouvé : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EditeurDtoOut> recupererTousLesEditeurs() {
        return editeurPort.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public void supprimerUnEditeur(Long id) {
        editeurPort.deleteById(id);
    }

    private EditeurDtoOut toDto(Editeur e) {
        return new EditeurDtoOut(e.getId(), e.getNom());
    }
}
