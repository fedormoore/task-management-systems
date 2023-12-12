package ru.moore.task_management_systems.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Сущность для отправики JWT токена")
public class JwtResponseDTO {

    @Schema(description = "Тип")
    private final String type = "Bearer";

    @Schema(description = "Токен для доступа")
    private final String accessToken;

    @Schema(description = "Токен для обновления")
    private final String refreshToken;

}
