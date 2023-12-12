package ru.moore.task_management_systems.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Сущность для создания пользователя")
public class SignUpRequestDTO {

    @Email(message = "Некорректный E-mail")
    @NotBlank(message = "Значение 'e-mail' не может быть пустым")
    @Schema(description = "E-mail пользователя")
    private String email;

    @NotBlank(message = "Значение 'password' не может быть пустым")
    @Size(min = 6, max = 30, message = "Пароль должен состоять не менее, чем из 6 символов, и не более 30 символов, и содержать заглавные, строчные буквы, а также цифры.")
    @Schema(description = "Пароль пользователя")
    private String password;

    @NotBlank(message = "Значение 'userName' не может быть пустым")
    @Schema(description = "Имя пользователя")
    private String userName;

}
