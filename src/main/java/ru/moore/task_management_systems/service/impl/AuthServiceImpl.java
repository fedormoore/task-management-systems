package ru.moore.task_management_systems.service.impl;

import com.querydsl.core.types.Predicate;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.moore.task_management_systems.dto.auth.JwtResponseDTO;
import ru.moore.task_management_systems.dto.auth.SignUpRequestDTO;
import ru.moore.task_management_systems.exception.ErrorResponse;
import ru.moore.task_management_systems.model.Account;
import ru.moore.task_management_systems.model.QAccount;
import ru.moore.task_management_systems.repository.AccountRepository;
import ru.moore.task_management_systems.security.JwtProvider;
import ru.moore.task_management_systems.service.AuthService;
import ru.moore.task_management_systems.utils.MapperUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final HashMap<String, String> refreshStorage = new HashMap<>();

    private final JwtProvider jwtProvider;

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final MapperUtils mapperUtils;

    /**
     * Метод позволяет зарегистрировать нового пользователя
     *
     * @param signUpRequestDTO принимает в качестве параметра signUpRequestDTO
     */
    @Override
    @Transactional
    public ResponseEntity<String> signUp(SignUpRequestDTO signUpRequestDTO) {
        Predicate predicate = QAccount.account.email.eq(signUpRequestDTO.getEmail());
        Optional<Account> findAccount = accountRepository.findOne(predicate);
        if (findAccount.isPresent()) {
            throw new ErrorResponse(HttpStatus.NOT_FOUND, "E-mail занят!");
        }

        Account account = Account.builder()
                .email(signUpRequestDTO.getEmail())
                .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
                .userName(signUpRequestDTO.getUserName())
                .build();

        accountRepository.save(account);

        return new ResponseEntity<>("Пользователь зарегистрирован", HttpStatus.OK);
    }

    /**
     * Метод позволяет аутентифицировать и авторизовать пользователя
     *
     * @param authentication пользователь авторизованной сессии
     */
    @Override
    public JwtResponseDTO signIn(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        Predicate predicate = QAccount.account.email.eq(principal.getUsername());
        Account findAccount = accountRepository.findOne(predicate).get();

        final String accessToken = jwtProvider.generateAccessToken(findAccount);
        final String refreshToken = jwtProvider.generateRefreshToken(findAccount);
        refreshStorage.put(findAccount.getEmail(), refreshToken);

        return new JwtResponseDTO(accessToken, refreshToken);
    }

    /**
     * Метод позволяет обновить JWT токен
     *
     * @param refreshToken токен для обновления
     */
    @Override
    public JwtResponseDTO refreshJWTToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            Account account = mapperUtils.map(claims.get("user"), Account.class);
            String saveRefreshToken = refreshStorage.get(account.getEmail());
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                String accessToken = jwtProvider.generateAccessToken(account);
                String newRefreshToken = jwtProvider.generateRefreshToken(account);
                refreshStorage.put(account.getEmail(), newRefreshToken);
                return new JwtResponseDTO(accessToken, newRefreshToken);
            }
        }
        throw new ErrorResponse(HttpStatus.BAD_REQUEST, "Невалидный JWT токен");
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Predicate predicate = QAccount.account.email.eq(login);
        Account findAccount = accountRepository.findOne(predicate).orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", login)));

        List<GrantedAuthority> authorities = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(findAccount.getEmail(), findAccount.getPassword(), authorities);
    }
}
