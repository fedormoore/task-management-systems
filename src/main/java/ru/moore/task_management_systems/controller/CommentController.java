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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.moore.task_management_systems.dto.CommentDto;
import ru.moore.task_management_systems.dto.utils.OnSave;
import ru.moore.task_management_systems.dto.utils.OnUpdate;
import ru.moore.task_management_systems.dto.utils.View;
import ru.moore.task_management_systems.service.CommentService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@Tag(name = "Название контроллера: comment", description = "Контроллер служит для создания новых, редактирования существующих, просматривать и удалять комментарии")
public class CommentController {


    private final CommentService commentService;

    @Operation(
            summary = "Создание комментария",
            description = "Позволяет создать комментарий"
    )
    @PostMapping(value = "/new")
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(View.RESPONSE.class)
    @Validated(OnSave.class)
    public CommentDto newComment(@Parameter(description = "Сущность для комментария") @RequestBody @Valid CommentDto commentDto, Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        log.info("POST/Создание комментария dto " + commentDto + ". Email пользователя:" + principal.getUsername());
        return commentService.newComment(commentDto, authentication);
    }

    @Operation(
            summary = "Редактирование комментария",
            description = "Позволяет редактировать комментарий"
    )
    @PutMapping(value = "/edit")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(View.RESPONSE.class)
    @Validated(OnUpdate.class)
    public CommentDto editComment(@Parameter(description = "Сущность для комментария") @RequestBody @Valid CommentDto commentDto, Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        log.info("PUT/Редактирование комментария dto " + commentDto + ". Email пользователя:" + principal.getUsername());
        return commentService.editComment(commentDto, authentication);
    }

    @Operation(
            summary = "Получение всех комментарий по id задачи",
            description = "Позволяет получить все комментарии по id задачи"
    )
    @GetMapping(value = "/get_all/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(View.RESPONSE.class)
    public List<CommentDto> getAllCommentByTaskId(@PathVariable UUID id, Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        log.info("GET/Получение всех комментариев по id задачи " + id + ". Email пользователя:" + principal.getUsername());
        return commentService.getAllCommentByTaskId(id);
    }

    @Operation(
            summary = "Удаление комментария по id",
            description = "Позволяет удалить комментарий по id"
    )
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(View.RESPONSE.class)
    public ResponseEntity<String> deleteComment(@PathVariable UUID id, Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        log.info("DELETE/Удаление комментария по Id " + id + ". Email пользователя:" + principal.getUsername());
        return commentService.deleteComment(id, authentication);
    }

}
