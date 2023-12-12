package ru.moore.task_management_systems.dto;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.moore.task_management_systems.dto.utils.*;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность для задачи")
public class TaskDto {

    @JsonView({View.RESPONSE.class, View.UPDATE.class})
    @NotNull(groups = {OnUpdate.class, OnChangeStatus.class, OnChangeExecutor.class}, message = "Значение 'id' не может быть пустым")
    @Schema(description = "Id задачи")
    private UUID id;

    @JsonView({View.RESPONSE.class, View.UPDATE.class})
    @NotBlank(groups = OnChangeStatus.class, message = "Значение 'status' не может быть пустым")
    @Schema(description = "Статус задачи")
    private String status;

    @JsonView({View.RESPONSE.class, View.SAVE.class, View.UPDATE.class})
    @NotBlank(groups = {OnSave.class, OnUpdate.class}, message = "Значение 'priority' не может быть пустым")
    @Schema(description = "Приоритет задачи")
    private String priority;

    @JsonView({View.RESPONSE.class, View.UPDATE.class, View.SAVE.class})
    @NotBlank(groups = {OnSave.class, OnUpdate.class}, message = "Значение 'header' не может быть пустым")
    @Schema(description = "Заголовок задачи")
    private String header;

    @JsonView({View.RESPONSE.class, View.UPDATE.class, View.SAVE.class})
    @NotBlank(groups = {OnSave.class, OnUpdate.class}, message = "Значение 'text' не может быть пустым")
    @Schema(description = "Текст задачи")
    private String text;

    @JsonView({View.RESPONSE.class, View.UPDATE.class, View.SAVE.class})
    @Schema(description = "Исполнитель задачи")
    private AccountDTO executor;

    @JsonView(View.CHANGEEXECUTOR.class)
    @NotNull(groups = OnChangeExecutor.class, message = "Значение 'executorId' не может быть пустым")
    @Schema(description = "Id исполнителя задачи")
    private UUID executorId;

    @JsonView(View.RESPONSE.class)
    @Schema(description = "Автор задачи")
    private AccountDTO author;

    @JsonView(View.RESPONSE.class)
    @Schema(description = "Дата создания задачи")
    private Date createdAt;

}
