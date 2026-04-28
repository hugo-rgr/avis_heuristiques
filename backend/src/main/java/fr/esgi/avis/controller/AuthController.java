package fr.esgi.avis.controller;

import fr.esgi.avis.dto.in.LoginDtoIn;
import fr.esgi.avis.dto.out.TokenDtoOut;
import fr.esgi.avis.port.in.AuthUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthUseCase authUseCase;

    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDtoOut> login(@Valid @RequestBody LoginDtoIn dto) {
        return ResponseEntity.ok(authUseCase.login(dto));
    }
}
