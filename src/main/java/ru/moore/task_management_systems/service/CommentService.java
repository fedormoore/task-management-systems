package ru.moore.task_management_systems.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import ru.moore.task_management_systems.dto.CommentDto;

import java.util.List;
import java.util.UUID;

public interface CommentService {

    /**
     * Метод позволяет создать комментарий
     *
     * @param commentDto     принимает в качестве параметра commentDto
     * @param authentication принимает в качестве параметра пользователя авторизованной сессии
     */
    CommentDto newComment(CommentDto commentDto, Authentication authentication);

    /**
     * Метод позволяет редактировать комментарий
     *
     * @param commentDto     принимает в качестве параметра commentDto
     * @param authentication принимает в качестве параметра пользователя авторизованной сессии
     */
    CommentDto editComment(CommentDto commentDto, Authentication authentication);

    /**
     * Метод позволяет получить все комментарий по id задачи
     *
     * @param id принимает в качестве параметра id задачи
     */
    List<CommentDto> getAllCommentByTaskId(UUID id);

    /**
     * Метод позволяет удалить комментарий
     *
     * @param id             принимает в качестве параметра id комментария
     * @param authentication принимает в качестве параметра пользователя авторизованной сессии
     */
    ResponseEntity<String> deleteComment(UUID id, Authentication authentication);

}
