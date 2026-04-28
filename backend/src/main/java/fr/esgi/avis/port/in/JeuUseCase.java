package fr.esgi.avis.port.in;

import fr.esgi.avis.dto.in.JeuDtoIn;
import fr.esgi.avis.dto.out.JeuDtoOut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JeuUseCase {

    JeuDtoOut creerUnJeu(JeuDtoIn dto);

    JeuDtoOut mettreAJourUnJeu(Long id, JeuDtoIn dto);

    JeuDtoOut recupererUnJeuParId(Long id);

    List<JeuDtoOut> recupererTousLesJeux();

    Page<JeuDtoOut> recupererTousLesJeux(Pageable pageable);

    List<JeuDtoOut> recupererDesJeuxDUnGenre(Long genreId);

    List<JeuDtoOut> recupererDesJeuxDUnEditeur(Long editeurId);

    void supprimerUnJeu(Long id);
}
