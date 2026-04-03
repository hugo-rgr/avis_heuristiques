package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.business.Moderateur;
import fr.esgi.avis.infrastructure.entity.ModerateurEntity;
import fr.esgi.avis.port.out.ModerateurPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ModerateurAdapter implements ModerateurPort {

    private final ModerateurJpaRepository jpaRepository;

    public ModerateurAdapter(ModerateurJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Moderateur save(Moderateur moderateur) {
        return toDomain(jpaRepository.save(toEntity(moderateur)));
    }

    @Override
    public Optional<Moderateur> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Moderateur> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    private ModerateurEntity toEntity(Moderateur moderateur) {
        ModerateurEntity entity = new ModerateurEntity();
        entity.setId(moderateur.getId());
        entity.setPseudo(moderateur.getPseudo());
        entity.setEmail(moderateur.getEmail());
        entity.setMotDePasse(moderateur.getMotDePasse());
        entity.setNumeroDeTelephone(moderateur.getNumeroDeTelephone());
        return entity;
    }

    private Moderateur toDomain(ModerateurEntity entity) {
        Moderateur moderateur = new Moderateur(entity.getNumeroDeTelephone());
        moderateur.setId(entity.getId());
        moderateur.setPseudo(entity.getPseudo());
        moderateur.setEmail(entity.getEmail());
        moderateur.setMotDePasse(entity.getMotDePasse());
        return moderateur;
    }
}
