package fr.esgi.avis.use_case;

import fr.esgi.avis.business.Jeu;

import java.util.List;

public interface JeuUseCase {

    List<Jeu> recupererJeux();

    Jeu recupererJeu(Long id);

    List<Jeu> recupererParNom(String nom);

}