package fr.esgi.avis.exception;

public class DuplicateEmailException extends AvisException {

    public DuplicateEmailException(String email) {
        super("Email déjà utilisé : " + email, 409);
    }
}
