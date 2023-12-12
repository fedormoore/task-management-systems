package ru.moore.task_management_systems.service.impl;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import ru.moore.task_management_systems.dto.CommentDto;
import ru.moore.task_management_systems.exception.ErrorResponse;
import ru.moore.task_management_systems.model.*;
import ru.moore.task_management_systems.repository.AccountRepository;
import ru.moore.task_management_systems.repository.CommentRepository;
import ru.moore.task_management_systems.repository.TaskRepository;
import ru.moore.task_management_systems.service.CommentService;
import ru.moore.task_management_systems.utils.MapperUtils;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final AccountRepository accountRepository;

    private final TaskRepository taskRepository;

    private final CommentRepository commentRepository;

    private final MapperUtils mapperUtils;

    private Account getAuthor(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        Predicate predicate = QAccount.account.email.eq(principal.getUsername());
        return accountRepository.findOne(predicate).orElseThrow(() -> new ErrorResponse(HttpStatus.BAD_REQUEST, String.format("Пользователь с e-mail '%s' не найдена.", principal.getUsername())));
    }

    /**
     * Метод позволяет создать комментарий
     *
     * @param commentDto     принимает в качестве параметра commentDto
     * @param authentication принимает в качестве параметра пользователя авторизованной сессии
     */
    @Override
    public CommentDto newComment(CommentDto commentDto, Authentication authentication) {
        Task findTask = findTaskById(commentDto.getTaskId());

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setAuthor(getAuthor(authentication));
        comment.setTask(findTask);

        commentRepository.save(comment);
        log.info("Комментарий создан");
        return mapperUtils.map(comment, CommentDto.class);
    }

    /**
     * Метод позволяет редактировать комментарий
     *
     * @param commentDto     принимает в качестве параметра commentDto
     * @param authentication принимает в качестве параметра пользователя авторизованной сессии
     */
    @Override
    public CommentDto editComment(CommentDto commentDto, Authentication authentication) {
        Comment findComment = findCommentById(commentDto.getId());

        checkAuthorAndSender(findComment, authentication);

        findComment.setText(commentDto.getText());

        commentRepository.save(findComment);
        log.info("Комментарий отредактирована");
        return mapperUtils.map(findComment, CommentDto.class);
    }

    /**
     * Метод позволяет получить все комментарий по id задачи
     *
     * @param id принимает в качестве параметра id задачи
     */
    @Override
    public List<CommentDto> getAllCommentByTaskId(UUID id) {
        Task findTask = findTaskById(id);

        Predicate predicate = QComment.comment.task.eq(findTask);
        List<Comment> findComment = (List<Comment>) commentRepository.findAll(predicate);
        log.info("Все комментарии отправлены");
        return mapperUtils.mapAll(findComment, CommentDto.class);
    }

    /**
     * Метод позволяет удалить комментарий
     *
     * @param id             принимает в качестве параметра id задачи
     * @param authentication принимает в качестве параметра пользователя авторизованной сессии
     */
    @Override
    public ResponseEntity<String> deleteComment(UUID id, Authentication authentication) {
        Comment findComment = findCommentById(id);

        checkAuthorAndSender(findComment, authentication);

        findComment.setDeleted(true);

        commentRepository.save(findComment);
        log.info("Задача удалена");
        return new ResponseEntity<>("Запись удалена", HttpStatus.OK);
    }

    /**
     * Метод позволяет найти комментарий по id
     *
     * @param id принимает в качестве параметра id
     */
    private Comment findCommentById(UUID id) {
        Predicate predicate = QComment.comment.id.eq(id);
        return commentRepository.findOne(predicate).orElseThrow(() -> new ErrorResponse(HttpStatus.BAD_REQUEST, String.format("Комментарий с id '%s' не найдена.", id)));
    }

    /**
     * Метод позволяет найти задачу по ее id
     *
     * @param id принимает в качестве параметра id
     */
    private Task findTaskById(UUID id) {
        Predicate predicate = QTask.task.id.eq(id);
        return taskRepository.findOne(predicate).orElseThrow(() -> new ErrorResponse(HttpStatus.BAD_REQUEST, String.format("Задача с id '%s' не найдена.", id)));
    }

    /**
     * Метод позволяет проверить автора комментария с отправителем
     *
     * @param comment        принимает в качестве параметра comment
     * @param authentication принимает в качестве параметра пользователя авторизованной сессии
     */
    private void checkAuthorAndSender(Comment comment, Authentication authentication) {
        if (!comment.getAuthor().getEmail().equals(getAuthor(authentication).getEmail())) {
            throw new ErrorResponse(HttpStatus.BAD_REQUEST, "Автор комментария не совпадает с отправителем.");
        }
    }

}
