package fr.esgi.avis.service;

import fr.esgi.avis.business.Plateforme;
import fr.esgi.avis.dto.in.PlatefomeDtoIn;
import fr.esgi.avis.dto.out.PlatefomeDtoOut;
import fr.esgi.avis.mapper.PlateformeMapper;
import fr.esgi.avis.port.in.PlateformeUseCase;
import fr.esgi.avis.port.out.PlateformePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlateformeService implements PlateformeUseCase {

    private final PlateformePort plateformePort;
    private final PlateformeMapper plateformeMapper;

    public PlateformeService(PlateformePort plateformePort, PlateformeMapper plateformeMapper) {
        this.plateformePort = plateformePort;
        this.plateformeMapper = plateformeMapper;
    }

    @Override
    public PlatefomeDtoOut creerUnePlateforme(PlatefomeDtoIn dto) {
        return plateformeMapper.toDto(plateformePort.save(plateformeMapper.toDomain(dto)));
    }

    @Override
    public PlatefomeDtoOut mettreAJour(Long id, PlatefomeDtoIn dto) {
        Plateforme existing = plateformePort.findById(id)
                .orElseThrow(() -> new RuntimeException("Plateforme non trouvée : " + id));
        existing.setNom(dto.nom());
        existing.setDateDeSortie(dto.dateDeSortie());
        return plateformeMapper.toDto(plateformePort.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public PlatefomeDtoOut recupererUnePlateformeParId(Long id) {
        return plateformePort.findById(id)
                .map(plateformeMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Plateforme non trouvée : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlatefomeDtoOut> recupererToutesLesPlateformes() {
        return plateformePort.findAll().stream().map(plateformeMapper::toDto).toList();
    }

    @Override
    public void supprimerUnePlateforme(Long id) {
        plateformePort.deleteById(id);
    }
}
