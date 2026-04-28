package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.business.*;
import fr.esgi.avis.infrastructure.entity.AvisEntity;
import fr.esgi.avis.port.out.AvisPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class AvisAdapter implements AvisPort {

    private final AvisJpaRepository jpaRepository;
    private final JeuJpaRepository jeuJpaRepository;
    private final JoueurJpaRepository joueurJpaRepository;
    private final ModerateurJpaRepository moderateurJpaRepository;

    public AvisAdapter(AvisJpaRepository jpaRepository,
                       JeuJpaRepository jeuJpaRepository,
                       JoueurJpaRepository joueurJpaRepository,
                       ModerateurJpaRepository moderateurJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.jeuJpaRepository = jeuJpaRepository;
        this.joueurJpaRepository = joueurJpaRepository;
        this.moderateurJpaRepository = moderateurJpaRepository;
    }

    @Override
    public Avis save(Avis avis) {
        return toDomain(jpaRepository.save(toEntity(avis)));
    }

    @Override
    public Optional<Avis> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Avis> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Page<Avis> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public List<Avis> findAllByJeuId(Long jeuId) {
        return jpaRepository.findAllByJeuId(jeuId).stream().map(this::toDomain).toList();
    }

    @Override
    public Page<Avis> findAllByJeuId(Long jeuId, Pageable pageable) {
        return jpaRepository.findAllByJeuId(jeuId, pageable).map(this::toDomain);
    }

    @Override
    public List<Avis> findAllByJoueurId(Long joueurId) {
        return jpaRepository.findAllByJoueurId(joueurId).stream().map(this::toDomain).toList();
    }

    @Override
    public Page<Avis> findAllByJoueurId(Long joueurId, Pageable pageable) {
        return jpaRepository.findAllByJoueurId(joueurId, pageable).map(this::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Optional<Double> findNoteMoyenneByJeuId(Long jeuId) {
        return jpaRepository.findNoteMoyenneByJeuId(jeuId);
    }

    private AvisEntity toEntity(Avis avis) {
        AvisEntity entity = new AvisEntity();
        entity.setId(avis.getId());
        entity.setDescription(avis.getDescription());
        entity.setNote(avis.getNote());
        entity.setDateDEnvoi(avis.getDateDEnvoi());
        if (avis.getJeu() != null) {
            entity.setJeu(jeuJpaRepository.getReferenceById(avis.getJeu().getId()));
        }
        if (avis.getJoueur() != null) {
            entity.setJoueur(joueurJpaRepository.getReferenceById(avis.getJoueur().getId()));
        }
        if (avis.getModerateur() != null) {
            entity.setModerateur(moderateurJpaRepository.getReferenceById(avis.getModerateur().getId()));
        }
        return entity;
    }

    private Avis toDomain(AvisEntity entity) {
        Jeu jeu = entity.getJeu() != null
                ? new Jeu(entity.getJeu().getId(), entity.getJeu().getNom(), null, null, null, 0, null, null, null, Collections.emptyList(), null)
                : null;
        Joueur joueur = null;
        if (entity.getJoueur() != null) {
            joueur = new Joueur(Collections.emptyList(), entity.getJoueur().getDateDeNaissance(), null);
            joueur.setId(entity.getJoueur().getId());
            joueur.setPseudo(entity.getJoueur().getPseudo());
            joueur.setEmail(entity.getJoueur().getEmail());
            joueur.setMotDePasse(entity.getJoueur().getMotDePasse());
        }
        Moderateur moderateur = null;
        if (entity.getModerateur() != null) {
            moderateur = new Moderateur(entity.getModerateur().getNumeroDeTelephone());
            moderateur.setId(entity.getModerateur().getId());
            moderateur.setPseudo(entity.getModerateur().getPseudo());
            moderateur.setEmail(entity.getModerateur().getEmail());
            moderateur.setMotDePasse(entity.getModerateur().getMotDePasse());
        }
        return new Avis(entity.getId(), entity.getDescription(), jeu, joueur, entity.getNote(), moderateur, entity.getDateDEnvoi());
    }
}
