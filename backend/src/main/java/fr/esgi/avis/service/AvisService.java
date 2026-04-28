package fr.esgi.avis.service;

import fr.esgi.avis.business.Jeu;
import fr.esgi.avis.business.Joueur;
import fr.esgi.avis.business.Moderateur;
import fr.esgi.avis.dto.in.AvisDtoIn;
import fr.esgi.avis.dto.out.AvisDtoOut;
import fr.esgi.avis.exception.ResourceNotFoundException;
import fr.esgi.avis.mapper.AvisMapper;
import fr.esgi.avis.port.in.AvisUseCase;
import fr.esgi.avis.port.out.AvisPort;
import fr.esgi.avis.port.out.JeuPort;
import fr.esgi.avis.port.out.JoueurPort;
import fr.esgi.avis.port.out.ModerateurPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AvisService implements AvisUseCase {

    private final AvisPort avisPort;
    private final JeuPort jeuPort;
    private final JoueurPort joueurPort;
    private final ModerateurPort moderateurPort;
    private final AvisMapper avisMapper;

    public AvisService(AvisPort avisPort, JeuPort jeuPort, JoueurPort joueurPort,
                       ModerateurPort moderateurPort, AvisMapper avisMapper) {
        this.avisPort = avisPort;
        this.jeuPort = jeuPort;
        this.joueurPort = joueurPort;
        this.moderateurPort = moderateurPort;
        this.avisMapper = avisMapper;
    }

    @Override
    public AvisDtoOut creerUnAvis(AvisDtoIn dto) {
        return avisMapper.toDto(avisPort.save(avisMapper.toDomain(dto, resolveJeu(dto), resolveJoueur(dto), resolveModerateur(dto))));
    }

    @Override
    public AvisDtoOut mettreAJourUnAvis(Long id, AvisDtoIn dto) {
        avisPort.findById(id).orElseThrow(() -> new ResourceNotFoundException("Avis", id));
        return avisMapper.toDto(avisPort.save(avisMapper.toDomain(dto, resolveJeu(dto), resolveJoueur(dto), resolveModerateur(dto))));
    }

    @Override
    @Transactional(readOnly = true)
    public AvisDtoOut recupererUnAvisParId(Long id) {
        return avisPort.findById(id)
                .map(avisMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Avis", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvisDtoOut> recupererTousLesAvis() {
        return avisPort.findAll().stream().map(avisMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AvisDtoOut> recupererTousLesAvis(Pageable pageable) {
        return avisPort.findAll(pageable).map(avisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvisDtoOut> recupererTousLesAvisParJeu(Long jeuId) {
        return avisPort.findAllByJeuId(jeuId).stream().map(avisMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AvisDtoOut> recupererTousLesAvisParJeu(Long jeuId, Pageable pageable) {
        return avisPort.findAllByJeuId(jeuId, pageable).map(avisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvisDtoOut> recupererTousLesAvisParJoueur(Long joueurId) {
        return avisPort.findAllByJoueurId(joueurId).stream().map(avisMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AvisDtoOut> recupererTousLesAvisParJoueur(Long joueurId, Pageable pageable) {
        return avisPort.findAllByJoueurId(joueurId, pageable).map(avisMapper::toDto);
    }

    @Override
    public void supprimerUnAvis(Long id) {
        avisPort.deleteById(id);
    }

    private Jeu resolveJeu(AvisDtoIn dto) {
        return jeuPort.findById(dto.jeuId())
                .orElseThrow(() -> new ResourceNotFoundException("Jeu", dto.jeuId()));
    }

    private Joueur resolveJoueur(AvisDtoIn dto) {
        return joueurPort.findById(dto.joueurId())
                .orElseThrow(() -> new ResourceNotFoundException("Joueur", dto.joueurId()));
    }

    private Moderateur resolveModerateur(AvisDtoIn dto) {
        return dto.moderateurId() != null
                ? moderateurPort.findById(dto.moderateurId())
                        .orElseThrow(() -> new ResourceNotFoundException("Modérateur", dto.moderateurId()))
                : null;
    }
}
