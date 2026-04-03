package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.business.Genre;
import fr.esgi.avis.infrastructure.entity.GenreEntity;
import fr.esgi.avis.port.out.GenrePort;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class GenreAdapter implements GenrePort {

    private final GenreJpaRepository jpaRepository;

    public GenreAdapter(GenreJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Genre save(Genre genre) {
        return toDomain(jpaRepository.save(toEntity(genre)));
    }

    @Override
    public Optional<Genre> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Genre> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private GenreEntity toEntity(Genre genre) {
        GenreEntity entity = new GenreEntity();
        entity.setId(genre.getId());
        entity.setNom(genre.getNom());
        return entity;
    }

    private Genre toDomain(GenreEntity entity) {
        return new Genre(entity.getId(), entity.getNom(), Collections.emptyList());
    }
}
