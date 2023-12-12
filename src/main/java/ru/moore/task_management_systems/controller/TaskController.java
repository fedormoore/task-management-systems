package ru.moore.task_management_systems.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.moore.task_management_systems.dto.TaskDto;
import ru.moore.task_management_systems.dto.utils.*;
import ru.moore.task_management_systems.service.TaskService;
import ru.moore.task_management_systems.specification.TaskSpecifications;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@Tag(name = "Название контроллера: task", description = "Контроллер служит для создания новых, редактирования существующих, просматривать и удалять, менять статус и назначать исполнителей задачи")
public class TaskController {


    private final TaskService taskService;

    @Operation(
            summary = "Создание задачи",
            description = "Позволяет создать задачу"
    )
    @PostMapping(value = "/new")
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(View.RESPONSE.class)
    @Validated(OnSave.class)
    public ResponseEntity<TaskDto> newTask(@Parameter(description = "Сущность для задачи") @RequestBody @Valid TaskDto taskDto, Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        log.info("POST/Создание задачи dto " + taskDto + ". Email пользователя:" + principal.getUsername());
        return ResponseEntity.ok(taskService.newTask(taskDto, authentication));
    }

    @Operation(
            summary = "Редактирование задачи",
            description = "Позволяет редактировать задачу"
    )
    @PutMapping(value = "/edit")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(View.RESPONSE.class)
    @Validated(OnUpdate.class)
    public ResponseEntity<TaskDto> editTask(@Parameter(description = "Сущность для задачи") @RequestBody @Valid TaskDto taskDto, Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        log.info("PUT/Редактирование задачи dto " + taskDto + ". Email пользователя:" + principal.getUsername());
        return ResponseEntity.ok(taskService.editTask(taskDto, authentication));
    }

    @Operation(
            summary = "Получение всех задач",
            description = "Позволяет получить все задачи"
    )
    @GetMapping(value = "/get_all")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(View.RESPONSE.class)
    public List<TaskDto> getAllTask(@RequestParam MultiValueMap<String, String> params, @RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "limit", defaultValue = "20") int limit, Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        log.info("GET/Получение всех задач. Email пользователя:" + principal.getUsername());
        if (page < 1) {
            page = 1;
        }
        return taskService.getAllTask(TaskSpecifications.build(params), page, limit);
    }

    @Operation(
            summary = "Получение задачи по id",
            description = "Позволяет получить задачу по id"
    )
    @GetMapping(value = "/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(View.RESPONSE.class)
    public TaskDto getAllTask(@PathVariable UUID id, Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        log.info("GET/Получение задачи по Id " + id + ". Email пользователя:" + principal.getUsername());
        return taskService.getTaskById(id);
    }

    @Operation(
            summary = "Удаление задачи по id",
            description = "Позволяет удалить задачу по id"
    )
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(View.RESPONSE.class)
    public ResponseEntity<String> deleteTask(@PathVariable UUID id, Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        log.info("DELETE/Удаление задачи по Id " + id + ". Email пользователя:" + principal.getUsername());
        return taskService.deleteTask(id, authentication);
    }

    @Operation(
            summary = "Изменение статуса задачи",
            description = "Позволяет изменить статус задачи"
    )
    @PutMapping(value = "/change_status")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(View.RESPONSE.class)
    @Validated(OnChangeStatus.class)
    public ResponseEntity<TaskDto> changeStatusTask(@Parameter(description = "Сущность для задачи") @RequestBody @Valid TaskDto taskDto, Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        log.info("PUT/Изменение статуса задачи dto " + taskDto + ". Email пользователя:" + principal.getUsername());
        return ResponseEntity.ok(taskService.changeStatusTask(taskDto, authentication));
    }

    @Operation(
            summary = "Изменение исполнителя задачи",
            description = "Позволяет изменить исполнителя задачи"
    )
    @PutMapping(value = "/change_executor")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(View.RESPONSE.class)
    @Validated(OnChangeExecutor.class)
    public ResponseEntity<TaskDto> changeExecutorTask(@Parameter(description = "Сущность для задачи") @RequestBody @Valid TaskDto taskDto, Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        log.info("PUT/Изменение исполнителя задачи dto " + taskDto + ". Email пользователя:" + principal.getUsername());
        return ResponseEntity.ok(taskService.changeExecutorTask(taskDto, authentication));
    }

}
