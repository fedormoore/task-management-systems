package ru.moore.task_management_systems.dto;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.moore.task_management_systems.dto.utils.View;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность для пользователя")
public class AccountDTO {

    @JsonView(View.RESPONSE.class)
    @Schema(description = "Id пользователя")
    private UUID id;

    @JsonView(View.RESPONSE.class)
    @Schema(description = "Email пользователя")
    private String email;

    @JsonView(View.RESPONSE.class)
    @Schema(description = "Имя пользователя")
    private String userName;

}
