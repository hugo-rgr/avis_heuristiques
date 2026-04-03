package fr.esgi.avis.service;

import fr.esgi.avis.business.Genre;
import fr.esgi.avis.dto.in.GenreDtoIn;
import fr.esgi.avis.dto.out.GenreDtoOut;
import fr.esgi.avis.mapper.GenreMapper;
import fr.esgi.avis.port.in.GenreUseCase;
import fr.esgi.avis.port.out.GenrePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GenreService implements GenreUseCase {

    private final GenrePort genrePort;
    private final GenreMapper genreMapper;

    public GenreService(GenrePort genrePort, GenreMapper genreMapper) {
        this.genrePort = genrePort;
        this.genreMapper = genreMapper;
    }

    @Override
    public GenreDtoOut creerUnGenre(GenreDtoIn dto) {
        return genreMapper.toDto(genrePort.save(genreMapper.toDomain(dto)));
    }

    @Override
    public GenreDtoOut mettreAJourUnGenre(Long id, GenreDtoIn dto) {
        Genre existing = genrePort.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre non trouvé : " + id));
        existing.setNom(dto.nom());
        return genreMapper.toDto(genrePort.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public GenreDtoOut recupererUnGenreParId(Long id) {
        return genrePort.findById(id)
                .map(genreMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Genre non trouvé : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreDtoOut> recupererTouslesGenre() {
        return genrePort.findAll().stream().map(genreMapper::toDto).toList();
    }

    @Override
    public void supprimerUnGenre(Long id) {
        genrePort.deleteById(id);
    }
}
