package fr.esgi.avis.service;

import fr.esgi.avis.business.Plateforme;
import fr.esgi.avis.dto.in.PlatefomeDtoIn;
import fr.esgi.avis.dto.out.PlatefomeDtoOut;
import fr.esgi.avis.port.out.PlateformePort;
import fr.esgi.avis.use_case.PlateformeUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class PlateformeService implements PlateformeUseCase {

    private final PlateformePort plateformePort;

    public PlateformeService(PlateformePort plateformePort) {
        this.plateformePort = plateformePort;
    }

    @Override
    public PlatefomeDtoOut creerUnePlateforme(PlatefomeDtoIn dto) {
        Plateforme plateforme = new Plateforme(null, dto.nom(), Collections.emptyList(), dto.dateDeSortie());
        return toDto(plateformePort.save(plateforme));
    }

    @Override
    public PlatefomeDtoOut mettreAJour(Long id, PlatefomeDtoIn dto) {
        Plateforme existing = plateformePort.findById(id)
                .orElseThrow(() -> new RuntimeException("Plateforme non trouvée : " + id));
        existing.setNom(dto.nom());
        existing.setDateDeSortie(dto.dateDeSortie());
        return toDto(plateformePort.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public PlatefomeDtoOut recupererUnePlateformeParId(Long id) {
        return plateformePort.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Plateforme non trouvée : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlatefomeDtoOut> recupererToutesLesPlateformes() {
        return plateformePort.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public void supprimerUnePlateforme(Long id) {
        plateformePort.deleteById(id);
    }

    private PlatefomeDtoOut toDto(Plateforme p) {
        return new PlatefomeDtoOut(p.getId(), p.getNom(), p.getDateDeSortie(), Collections.emptyList());
    }
}
