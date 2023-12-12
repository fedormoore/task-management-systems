package ru.moore.task_management_systems.dto;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.moore.task_management_systems.dto.utils.OnSave;
import ru.moore.task_management_systems.dto.utils.OnUpdate;
import ru.moore.task_management_systems.dto.utils.View;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность для комментария")
public class CommentDto {

    @JsonView({View.RESPONSE.class, View.UPDATE.class})
    @NotNull(groups = OnUpdate.class, message = "Значение 'id' не может быть пустым")
    @Schema(description = "Id комментария")
    private UUID id;

    @JsonView({View.SAVE.class, View.UPDATE.class})
    @NotNull(groups = OnSave.class, message = "Значение 'taskId' не может быть пустым")
    @Schema(description = "Id задачи")
    private UUID taskId;

    @JsonView({View.RESPONSE.class, View.UPDATE.class, View.SAVE.class})
    @NotBlank(groups = {OnSave.class, OnUpdate.class}, message = "Значение 'text' не может быть пустым")
    @Schema(description = "Текст комментария")
    private String text;

    @JsonView(View.RESPONSE.class)
    @Schema(description = "Автор комментария")
    private AccountDTO author;

    @JsonView(View.RESPONSE.class)
    @Schema(description = "Дата создания задачи")
    private String createdAt;

}
