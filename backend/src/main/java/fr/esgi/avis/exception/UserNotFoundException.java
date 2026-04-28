package fr.esgi.avis.exception;

public class UserNotFoundException extends AvisException {

    public UserNotFoundException(String email) {
        super("Utilisateur non trouvé : " + email, 401);
    }
}
