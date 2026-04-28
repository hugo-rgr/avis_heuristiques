package fr.esgi.avis.service;

import fr.esgi.avis.dto.in.LoginDtoIn;
import fr.esgi.avis.dto.out.TokenDtoOut;
import fr.esgi.avis.exception.BadCredentialsException;
import fr.esgi.avis.exception.UserNotFoundException;
import fr.esgi.avis.port.in.AuthUseCase;
import fr.esgi.avis.port.out.JoueurPort;
import fr.esgi.avis.port.out.ModerateurPort;
import fr.esgi.avis.port.out.TokenPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService implements AuthUseCase {

    private final JoueurPort joueurPort;
    private final ModerateurPort moderateurPort;
    private final TokenPort tokenPort;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JoueurPort joueurPort, ModerateurPort moderateurPort,
                       TokenPort tokenPort, PasswordEncoder passwordEncoder) {
        this.joueurPort = joueurPort;
        this.moderateurPort = moderateurPort;
        this.tokenPort = tokenPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public TokenDtoOut login(LoginDtoIn dto) {
        return joueurPort.findByEmail(dto.email())
                .map(joueur -> {
                    if (!passwordEncoder.matches(dto.motDePasse(), joueur.getMotDePasse())) {
                        throw new BadCredentialsException();
                    }
                    return new TokenDtoOut(tokenPort.generateToken(joueur.getEmail(), "JOUEUR", joueur.getId()), "JOUEUR");
                })
                .orElseGet(() -> moderateurPort.findByEmail(dto.email())
                        .map(mod -> {
                            if (!passwordEncoder.matches(dto.motDePasse(), mod.getMotDePasse())) {
                                throw new BadCredentialsException();
                            }
                            return new TokenDtoOut(tokenPort.generateToken(mod.getEmail(), "MODERATEUR", mod.getId()), "MODERATEUR");
                        })
                        .orElseThrow(() -> new UserNotFoundException(dto.email()))
                );
    }
}
