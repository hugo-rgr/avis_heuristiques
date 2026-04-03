package fr.esgi.avis.dto.out;

import java.io.Serializable;

public record ModerateurDtoOut(Long id, String pseudo, String email, String numeroDeTelephone) implements Serializable {
}

