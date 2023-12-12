package ru.moore.task_management_systems.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.moore.task_management_systems.dto.auth.JwtResponseDTO;
import ru.moore.task_management_systems.dto.auth.SignUpRequestDTO;

public interface AuthService extends UserDetailsService {

    /**
     * Метод позволяет зарегистрировать нового пользователя
     *
     * @param signUpRequestDTO принимает в качестве параметра signUpRequestDTO
     */
    ResponseEntity<String> signUp(SignUpRequestDTO signUpRequestDTO);

    /**
     * Метод позволяет аутентифицировать и авторизовать пользователя
     *
     * @param authentication пользователь авторизованной сессии
     */
    JwtResponseDTO signIn(Authentication authentication);

    /**
     * Метод позволяет обновить JWT токен
     *
     * @param refreshToken токен для обновления
     */
    JwtResponseDTO refreshJWTToken(String refreshToken);

}
