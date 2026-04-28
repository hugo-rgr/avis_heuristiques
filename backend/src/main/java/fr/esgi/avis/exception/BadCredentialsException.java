package fr.esgi.avis.exception;

public class BadCredentialsException extends AvisException {

    public BadCredentialsException() {
        super("Mot de passe incorrect", 401);
    }
}
