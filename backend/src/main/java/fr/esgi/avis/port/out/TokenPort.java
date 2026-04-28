package fr.esgi.avis.port.out;

public interface TokenPort {
    String generateToken(String email, String role, Long id);
    String extractEmail(String token);
    String extractRole(String token);
    boolean validateToken(String token);
}
