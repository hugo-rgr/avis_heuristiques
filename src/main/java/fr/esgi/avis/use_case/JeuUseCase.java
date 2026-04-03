package fr.esgi.avis.use_case;

import fr.esgi.avis.dto.in.JeuDtoIn;
import fr.esgi.avis.dto.out.JeuDtoOut;

import java.util.List;

public interface JeuUseCase {

    JeuDtoOut creerUnJeu(JeuDtoIn dto);

    JeuDtoOut mettreAJourUnJeu(Long id, JeuDtoIn dto);

    JeuDtoOut recupererUnJeuParId(Long id);

    List<JeuDtoOut> recupererTousLesJeux();

    List<JeuDtoOut> recupererDesJeuxDUnGenre(Long genreId);

    List<JeuDtoOut> recupererDesJeuxDUnEditeur(Long editeurId);

    void supprimerUnJeu(Long id);
}