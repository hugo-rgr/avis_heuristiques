package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.infrastructure.entity.JoueurEntity;

import java.time.LocalDate;

final class TestEntities {
    private TestEntities() {
    }

    static JoueurEntity joueurEntity(String pseudo, String email) {
        JoueurEntity e = new JoueurEntity();
        e.setPseudo(pseudo);
        e.setEmail(email);
        e.setMotDePasse("pwd");
        e.setDateDeNaissance(LocalDate.of(1995, 5, 15));
        return e;
    }
}

