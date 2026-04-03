package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.business.Editeur;
import fr.esgi.avis.infrastructure.entity.EditeurEntity;
import fr.esgi.avis.port.out.EditeurPort;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class EditeurAdapter implements EditeurPort {

    private final EditeurJpaRepository jpaRepository;

    public EditeurAdapter(EditeurJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Editeur save(Editeur editeur) {
        return toDomain(jpaRepository.save(toEntity(editeur)));
    }

    @Override
    public Optional<Editeur> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Editeur> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private EditeurEntity toEntity(Editeur editeur) {
        EditeurEntity entity = new EditeurEntity();
        entity.setId(editeur.getId());
        entity.setNom(editeur.getNom());
        return entity;
    }

    private Editeur toDomain(EditeurEntity entity) {
        return new Editeur(entity.getId(), entity.getNom(), Collections.emptyList());
    }
}
