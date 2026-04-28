package fr.esgi.avis.exception;

public class ResourceNotFoundException extends AvisException {

    public ResourceNotFoundException(String resourceType, Object id) {
        super(resourceType + " non trouvé(e) avec l'identifiant : " + id, 404);
    }
}
