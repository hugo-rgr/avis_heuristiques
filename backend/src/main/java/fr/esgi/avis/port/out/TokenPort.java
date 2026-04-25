package fr.esgi.avis.port.out;

public interface TokenPort {
    String generateToken(Long id, String email, String role);
    String extractEmail(String token);
    String extractRole(String token);
    boolean validateToken(String token);
}
