package fr.esgi.avis.service;

import fr.esgi.avis.business.Editeur;
import fr.esgi.avis.dto.in.EditeurDtoIn;
import fr.esgi.avis.dto.out.EditeurDtoOut;
import fr.esgi.avis.exception.ResourceNotFoundException;
import fr.esgi.avis.mapper.EditeurMapper;
import fr.esgi.avis.port.in.EditeurUseCase;
import fr.esgi.avis.port.out.EditeurPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EditeurService implements EditeurUseCase {

    private final EditeurPort editeurPort;
    private final EditeurMapper editeurMapper;

    public EditeurService(EditeurPort editeurPort, EditeurMapper editeurMapper) {
        this.editeurPort = editeurPort;
        this.editeurMapper = editeurMapper;
    }

    @Override
    public EditeurDtoOut creerUnEditeur(EditeurDtoIn dto) {
        return editeurMapper.toDto(editeurPort.save(editeurMapper.toDomain(dto)));
    }

    @Override
    public EditeurDtoOut mettreAJour(Long id, EditeurDtoIn dto) {
        Editeur existing = editeurPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editeur", id));
        existing.setNom(dto.nom());
        return editeurMapper.toDto(editeurPort.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public EditeurDtoOut recupererUnEditeurParId(Long id) {
        return editeurPort.findById(id)
                .map(editeurMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Editeur", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EditeurDtoOut> recupererTousLesEditeurs() {
        return editeurPort.findAll().stream().map(editeurMapper::toDto).toList();
    }

    @Override
    public void supprimerUnEditeur(Long id) {
        editeurPort.deleteById(id);
    }
}
