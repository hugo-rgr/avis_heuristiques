package fr.esgi.avis.port.in;

import fr.esgi.avis.dto.in.LoginDtoIn;
import fr.esgi.avis.dto.out.TokenDtoOut;

public interface AuthUseCase {
    TokenDtoOut login(LoginDtoIn dto);
}
