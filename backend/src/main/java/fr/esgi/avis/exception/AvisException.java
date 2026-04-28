package fr.esgi.avis.exception;

public abstract class AvisException extends RuntimeException {

    private final int httpStatus;

    protected AvisException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
