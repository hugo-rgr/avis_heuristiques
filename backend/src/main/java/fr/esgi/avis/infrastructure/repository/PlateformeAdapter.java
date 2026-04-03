package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.business.Plateforme;
import fr.esgi.avis.infrastructure.entity.PlateformeEntity;
import fr.esgi.avis.port.out.PlateformePort;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class PlateformeAdapter implements PlateformePort {

    private final PlateformeJpaRepository jpaRepository;

    public PlateformeAdapter(PlateformeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Plateforme save(Plateforme plateforme) {
        return toDomain(jpaRepository.save(toEntity(plateforme)));
    }

    @Override
    public Optional<Plateforme> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Plateforme> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Plateforme> findAllByIds(List<Long> ids) {
        return jpaRepository.findAllByIdIn(ids).stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private PlateformeEntity toEntity(Plateforme plateforme) {
        PlateformeEntity entity = new PlateformeEntity();
        entity.setId(plateforme.getId());
        entity.setNom(plateforme.getNom());
        entity.setDateDeSortie(plateforme.getDateDeSortie());
        return entity;
    }

    private Plateforme toDomain(PlateformeEntity entity) {
        return new Plateforme(entity.getId(), entity.getNom(), Collections.emptyList(), entity.getDateDeSortie());
    }
}
