package fr.esgi.avis.controller;

import fr.esgi.avis.exception.AvisException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    record ErrorResponse(int status, String error, String message, Instant timestamp) {}

    @ExceptionHandler(AvisException.class)
    public ResponseEntity<ErrorResponse> handleAvisException(AvisException ex) {
        HttpStatus httpStatus = HttpStatus.resolve(ex.getHttpStatus());
        String errorPhrase = httpStatus != null ? httpStatus.getReasonPhrase() : "Error";
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(new ErrorResponse(ex.getHttpStatus(), errorPhrase, ex.getMessage(), Instant.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity
                .status(422)
                .body(new ErrorResponse(422, "Unprocessable Entity", message, Instant.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(500)
                .body(new ErrorResponse(500, "Internal Server Error", "Une erreur interne est survenue", Instant.now()));
    }
}
