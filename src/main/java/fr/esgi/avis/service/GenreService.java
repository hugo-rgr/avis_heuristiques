package fr.esgi.avis.service;

import fr.esgi.avis.business.Genre;
import fr.esgi.avis.dto.in.GenreDtoIn;
import fr.esgi.avis.dto.out.GenreDtoOut;
import fr.esgi.avis.port.out.GenrePort;
import fr.esgi.avis.port.in.GenreUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class GenreService implements GenreUseCase {

    private final GenrePort genrePort;

    public GenreService(GenrePort genrePort) {
        this.genrePort = genrePort;
    }

    @Override
    public GenreDtoOut creerUnGenre(GenreDtoIn dto) {
        Genre genre = new Genre(null, dto.nom(), Collections.emptyList());
        return toDto(genrePort.save(genre));
    }

    @Override
    public GenreDtoOut mettreAJourUnGenre(Long id, GenreDtoIn dto) {
        Genre existing = genrePort.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre non trouvé : " + id));
        existing.setNom(dto.nom());
        return toDto(genrePort.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public GenreDtoOut recupererUnGenreParId(Long id) {
        return genrePort.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Genre non trouvé : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreDtoOut> recupererTouslesGenre() {
        return genrePort.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public void supprimerUnGenre(Long id) {
        genrePort.deleteById(id);
    }

    private GenreDtoOut toDto(Genre g) {
        return new GenreDtoOut(g.getId(), g.getNom());
    }
}
