package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.business.Avatar;
import fr.esgi.avis.business.Joueur;
import fr.esgi.avis.infrastructure.entity.AvatarEntity;
import fr.esgi.avis.infrastructure.entity.JoueurEntity;
import fr.esgi.avis.port.out.AvatarPort;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class AvatarAdapter implements AvatarPort {

    private final AvatarJpaRepository jpaRepository;
    private final JoueurJpaRepository joueurJpaRepository;

    public AvatarAdapter(AvatarJpaRepository jpaRepository, JoueurJpaRepository joueurJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.joueurJpaRepository = joueurJpaRepository;
    }

    @Override
    public Avatar save(Avatar avatar) {
        return toDomain(jpaRepository.save(toEntity(avatar)));
    }

    @Override
    public Optional<Avatar> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Avatar> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Avatar> findAllByJoueurId(Long joueurId) {
        return jpaRepository.findAllByJoueurId(joueurId).stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private AvatarEntity toEntity(Avatar avatar) {
        AvatarEntity entity = new AvatarEntity();
        entity.setId(avatar.getId());
        entity.setNom(avatar.getNom());
        if (avatar.getJoueur() != null) {
            JoueurEntity joueurEntity = joueurJpaRepository.getReferenceById(avatar.getJoueur().getId());
            entity.setJoueur(joueurEntity);
        }
        return entity;
    }

    private Avatar toDomain(AvatarEntity entity) {
        Joueur joueur = null;
        if (entity.getJoueur() != null) {
            joueur = new Joueur(Collections.emptyList(), entity.getJoueur().getDateDeNaissance(), null);
            joueur.setId(entity.getJoueur().getId());
            joueur.setPseudo(entity.getJoueur().getPseudo());
            joueur.setEmail(entity.getJoueur().getEmail());
            joueur.setMotDePasse(entity.getJoueur().getMotDePasse());
        }
        return new Avatar(entity.getId(), entity.getNom(), joueur);
    }
}
