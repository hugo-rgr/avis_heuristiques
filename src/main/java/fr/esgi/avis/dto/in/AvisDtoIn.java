package fr.esgi.avis.dto.in;

import java.io.Serializable;

public record AvisDtoIn(String description, float note, Long jeuId) implements Serializable {
}

