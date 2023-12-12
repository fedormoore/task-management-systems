package ru.moore.task_management_systems.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.moore.task_management_systems.exception.ErrorResponse;
import ru.moore.task_management_systems.model.Account;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secretAccess}")
    private String jwtAccessSecret;

    @Value("${jwt.secretRefresh}")
    private String jwtRefreshSecret;

    public String generateAccessToken(Account account) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusDays(1).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);

        return Jwts.builder()
                .setSubject(account.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(accessExpiration)
                .signWith(getAccessKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Account account) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(2).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);

        return Jwts.builder()
                .setSubject(account.getEmail())
                .setExpiration(refreshExpiration)
                .signWith(getRefreshKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getAccessKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtAccessSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getRefreshKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtRefreshSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateAccessToken(@NonNull String token) {
        return validateToken(token, jwtAccessSecret);
    }

    public boolean validateRefreshToken(@NonNull String token) {
        return validateToken(token, jwtRefreshSecret);
    }

    private boolean validateToken(@NonNull String token, @NonNull String secret) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            throw new ErrorResponse(HttpStatus.UNAUTHORIZED, "Срок действия токена JWT истек");
        } catch (UnsupportedJwtException unsEx) {

        } catch (MalformedJwtException mjEx) {
            throw new ErrorResponse(HttpStatus.UNAUTHORIZED, "Не верный JWT");
        } catch (SignatureException sEx) {

        } catch (Exception e) {

        }
        return false;
    }

    public Claims getAccessClaims(@NonNull String token) {
        return getUserPrincipalFromToken(token, jwtAccessSecret);
    }

    public Claims getRefreshClaims(@NonNull String token) {
        return getUserPrincipalFromToken(token, jwtRefreshSecret);
    }

    public Claims getUserPrincipalFromToken(@NonNull String token, @NonNull String secret) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
