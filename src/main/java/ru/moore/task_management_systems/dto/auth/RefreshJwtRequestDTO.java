package ru.moore.task_management_systems.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Сущность для обновления JWT токена")
public class RefreshJwtRequestDTO {

    @NotBlank(message = "Значение 'refreshToken' не может быть пустым")
    @Schema(description = "RefreshToken токен")
    public String refreshToken;

}
