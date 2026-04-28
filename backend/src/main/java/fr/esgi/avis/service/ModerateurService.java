package fr.esgi.avis.service;

import fr.esgi.avis.dto.in.JeuDtoIn;
import fr.esgi.avis.dto.out.JeuDtoOut;
import fr.esgi.avis.dto.out.ModerateurDtoOut;
import fr.esgi.avis.exception.ResourceNotFoundException;
import fr.esgi.avis.mapper.ModerateurMapper;
import fr.esgi.avis.port.in.JeuUseCase;
import fr.esgi.avis.port.in.ModerateurUseCase;
import fr.esgi.avis.port.out.AvisPort;
import fr.esgi.avis.port.out.ModerateurPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ModerateurService implements ModerateurUseCase {

    private final ModerateurPort moderateurPort;
    private final AvisPort avisPort;
    private final JeuUseCase jeuUseCase;
    private final ModerateurMapper moderateurMapper;

    public ModerateurService(ModerateurPort moderateurPort, AvisPort avisPort,
                             JeuUseCase jeuUseCase, ModerateurMapper moderateurMapper) {
        this.moderateurPort = moderateurPort;
        this.avisPort = avisPort;
        this.jeuUseCase = jeuUseCase;
        this.moderateurMapper = moderateurMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public ModerateurDtoOut trouverParId(Long id) {
        return moderateurPort.findById(id)
                .map(moderateurMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Modérateur", id));
    }

    @Override
    public JeuDtoOut ajouterJeu(Long moderateurId, JeuDtoIn dto) {
        moderateurPort.findById(moderateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Modérateur", moderateurId));
        return jeuUseCase.creerUnJeu(dto);
    }

    @Override
    public void supprimerAvis(Long moderateurId, Long avisId) {
        moderateurPort.findById(moderateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Modérateur", moderateurId));
        avisPort.findById(avisId)
                .orElseThrow(() -> new ResourceNotFoundException("Avis", avisId));
        avisPort.deleteById(avisId);
    }
}
