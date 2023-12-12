package ru.moore.task_management_systems.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import ru.moore.task_management_systems.dto.TaskDto;
import ru.moore.task_management_systems.model.Task;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    /**
     * Метод позволяет создать задачу
     *
     * @param taskDto        принимает в качестве параметра taskDto
     * @param authentication принимает в качестве параметра пользователя авторизованной сессии
     */
    TaskDto newTask(TaskDto taskDto, Authentication authentication);

    /**
     * Метод позволяет редактировать задачу
     *
     * @param taskDto        принимает в качестве параметра taskDto
     * @param authentication принимает в качестве параметра пользователя авторизованной сессии
     */
    TaskDto editTask(TaskDto taskDto, Authentication authentication);

    /**
     * Метод позволяет получить все задачи
     */
    List<TaskDto> getAllTask(Specification<Task> spec, int page, int pageSize);

    /**
     * Метод позволяет получить задачу по id
     *
     * @param id принимает в качестве параметра id задачи
     */
    TaskDto getTaskById(UUID id);

    /**
     * Метод позволяет удалить задачу
     *
     * @param id             принимает в качестве параметра id задачи
     * @param authentication принимает в качестве параметра пользователя авторизованной сессии
     */
    ResponseEntity<String> deleteTask(UUID id, Authentication authentication);

    /**
     * Метод позволяет изменить статус задачи
     *
     * @param taskDto        принимает в качестве параметра taskDto
     * @param authentication принимает в качестве параметра пользователя авторизованной сессии
     */
    TaskDto changeStatusTask(TaskDto taskDto, Authentication authentication);

    /**
     * Метод позволяет назначить исполнителя
     *
     * @param taskDto        принимает в качестве параметра taskDto
     * @param authentication принимает в качестве параметра пользователя авторизованной сессии
     */
    TaskDto changeExecutorTask(TaskDto taskDto, Authentication authentication);
}
