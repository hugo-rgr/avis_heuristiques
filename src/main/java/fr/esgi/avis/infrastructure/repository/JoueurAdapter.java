package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.business.Avatar;
import fr.esgi.avis.business.Joueur;
import fr.esgi.avis.infrastructure.entity.JoueurEntity;
import fr.esgi.avis.port.out.JoueurPort;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class JoueurAdapter implements JoueurPort {

    private final JoueurJpaRepository jpaRepository;

    public JoueurAdapter(JoueurJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Joueur save(Joueur joueur) {
        return toDomain(jpaRepository.save(toEntity(joueur)));
    }

    @Override
    public Optional<Joueur> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Joueur> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    private JoueurEntity toEntity(Joueur joueur) {
        JoueurEntity entity = new JoueurEntity();
        entity.setId(joueur.getId());
        entity.setPseudo(joueur.getPseudo());
        entity.setEmail(joueur.getEmail());
        entity.setMotDePasse(joueur.getMotDePasse());
        entity.setDateDeNaissance(joueur.getDateDeNaissance());
        return entity;
    }

    private Joueur toDomain(JoueurEntity entity) {
        Avatar avatar = entity.getAvatar() != null
                ? new Avatar(entity.getAvatar().getId(), entity.getAvatar().getNom(), null)
                : null;
        Joueur joueur = new Joueur(Collections.emptyList(), entity.getDateDeNaissance(), avatar);
        joueur.setId(entity.getId());
        joueur.setPseudo(entity.getPseudo());
        joueur.setEmail(entity.getEmail());
        joueur.setMotDePasse(entity.getMotDePasse());
        return joueur;
    }
}
