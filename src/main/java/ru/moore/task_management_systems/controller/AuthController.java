package ru.moore.task_management_systems.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.moore.task_management_systems.dto.auth.JwtResponseDTO;
import ru.moore.task_management_systems.dto.auth.RefreshJwtRequestDTO;
import ru.moore.task_management_systems.dto.auth.SignInRequestDTO;
import ru.moore.task_management_systems.dto.auth.SignUpRequestDTO;
import ru.moore.task_management_systems.service.AuthService;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/auth")
@Validated
@RequiredArgsConstructor
@Tag(name = "Название контроллера: auth", description = "Контроллер служит для аутентификации и авторизации пользователей")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @Operation(
            summary = "Регистрация пользователя",
            description = "Позволяет зарегистрировать нового пользователя"
    )
    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
        return authService.signUp(signUpRequestDTO);
    }

    @Operation(
            summary = "Обновление JWT токена",
            description = "Позволяет обновить устаревший JWT токен"
    )
    @PostMapping("/refresh-tokens")
    public ResponseEntity<JwtResponseDTO> refreshTokens(@Valid @RequestBody RefreshJwtRequestDTO refreshJwtRequestDTO) {
        JwtResponseDTO jwtResponseDTO = authService.refreshJWTToken(refreshJwtRequestDTO.getRefreshToken());
        return ResponseEntity.ok(jwtResponseDTO);
    }

    @Operation(
            summary = "Авторизация пользователя",
            description = "Позволяет аутентифицировать и авторизовать пользователя"
    )
    @PostMapping("/signIn")
    public ResponseEntity<JwtResponseDTO> signIn(@Valid @RequestBody SignInRequestDTO signInRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequestDTO.getEmail(), signInRequestDTO.getPassword()));
        JwtResponseDTO jwtResponseDTO = authService.signIn(authentication);
        return ResponseEntity.ok(jwtResponseDTO);
    }

}
