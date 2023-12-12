package ru.moore.task_management_systems.service.impl;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import ru.moore.task_management_systems.dto.TaskDto;
import ru.moore.task_management_systems.enums.TaskStatusEnum;
import ru.moore.task_management_systems.exception.ErrorResponse;
import ru.moore.task_management_systems.model.Account;
import ru.moore.task_management_systems.model.QAccount;
import ru.moore.task_management_systems.model.QTask;
import ru.moore.task_management_systems.model.Task;
import ru.moore.task_management_systems.repository.AccountRepository;
import ru.moore.task_management_systems.repository.TaskRepository;
import ru.moore.task_management_systems.service.TaskService;
import ru.moore.task_management_systems.utils.MapperUtils;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final AccountRepository accountRepository;

    private final TaskRepository taskRepository;

    private final MapperUtils mapperUtils;

    private Account getAuthor(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        Predicate predicate = QAccount.account.email.eq(principal.getUsername());
        return accountRepository.findOne(predicate).orElseThrow(() -> new ErrorResponse(HttpStatus.BAD_REQUEST, String.format("Пользователь с e-mail '%s' не найдена.", principal.getUsername())));
    }

    /**
     * Метод позволяет создать задачу
     *
     * @param taskDto        принимает в качестве параметра taskDto
     * @param authentication принимает в качестве параметра пользователя авторизованной сессии
     */
    @Override
    public TaskDto newTask(TaskDto taskDto, Authentication authentication) {
        taskDto.setStatus(TaskStatusEnum.NEW.toString());
        Task task = mapperUtils.map(taskDto, Task.class);
        task.setAuthor(getAuthor(authentication));

        taskRepository.save(task);
        log.info("Задача создана");
        return mapperUtils.map(task, TaskDto.class);
    }

    /**
     * Метод позволяет редактировать задачу
     *
     * @param taskDto        принимает в качестве параметра taskDto
     * @param authentication принимает в качестве параметра пользователя авторизованной сессии
     */
    @Override
    public TaskDto editTask(TaskDto taskDto, Authentication authentication) {
        Task findTask = findTaskById(taskDto.getId());

        checkAuthorAndSender(findTask, authentication);

        findTask.setHeader(taskDto.getHeader());
        findTask.setText(taskDto.getText());
        findTask.setPriority(taskDto.getPriority());

        taskRepository.save(findTask);
        log.info("Задача отредактирована");
        return mapperUtils.map(findTask, TaskDto.class);
    }

    /**
     * Метод позволяет получить все задачи
     */
    @Override
    public List<TaskDto> getAllTask(Specification<Task> spec, int page, int pageSize) {
        log.info("Все задачи отправлены");
        return mapperUtils.mapAll(taskRepository.findAll(spec, PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "createdAt"))).stream().toList(), TaskDto.class);
    }

    /**
     * Метод позволяет получить задачу по id
     *
     * @param id принимает в качестве параметра id задачи
     */
    @Override
    public TaskDto getTaskById(UUID id) {
        Task findTask = findTaskById(id);
        log.info("Задача по id отправлена");
        return mapperUtils.map(findTask, TaskDto.class);
    }

    /**
     * Метод позволяет удалить задачу
     *
     * @param id             принимает в качестве параметра id задачи
     * @param authentication принимает в качестве параметра пользователя авторизованной сессии
     */
    @Override
    public ResponseEntity<String> deleteTask(UUID id, Authentication authentication) {
        Task findTask = findTaskById(id);

        checkAuthorAndSender(findTask, authentication);

        findTask.setDeleted(true);

        taskRepository.save(findTask);
        log.info("Задача удалена");
        return new ResponseEntity<>("Запись удалена", HttpStatus.OK);
    }

    /**
     * Метод позволяет изменить статус задачи
     *
     * @param taskDto        принимает в качестве параметра taskDto
     * @param authentication принимает в качестве параметра пользователя авторизованной сессии
     */
    @Override
    public TaskDto changeStatusTask(TaskDto taskDto, Authentication authentication) {
        Task findTask = findTaskById(taskDto.getId());

        checkAuthorAndSender(findTask, authentication);
        checkExecutorAndSender(findTask, authentication);

        findTask.setStatus(taskDto.getStatus());

        taskRepository.save(findTask);
        log.info("Статус задачи изменен");
        return mapperUtils.map(findTask, TaskDto.class);
    }

    /**
     * Метод позволяет назначить исполнителя
     *
     * @param taskDto        принимает в качестве параметра taskDto
     * @param authentication принимает в качестве параметра пользователя авторизованной сессии
     */
    @Override
    public TaskDto changeExecutorTask(TaskDto taskDto, Authentication authentication) {
        Task findTask = findTaskById(taskDto.getId());

        checkExecutorAndSender(findTask, authentication);

        Predicate predicateAccount = QAccount.account.id.eq(taskDto.getExecutorId());
        Account executor = accountRepository.findOne(predicateAccount).orElseThrow(() -> new ErrorResponse(HttpStatus.BAD_REQUEST, String.format("Автор с id '%s' ненайдена.", taskDto.getExecutorId())));
        findTask.setExecutor(executor);

        taskRepository.save(findTask);
        log.info("Исполнитель задачи изменен");
        return mapperUtils.map(findTask, TaskDto.class);
    }

    /**
     * Метод позволяет найти задачу по id
     *
     * @param id принимает в качестве параметра id
     */
    private Task findTaskById(UUID id) {
        Predicate predicate = QTask.task.id.eq(id);
        return taskRepository.findOne(predicate).orElseThrow(() -> new ErrorResponse(HttpStatus.BAD_REQUEST, String.format("Задача с id '%s' не найдена.", id)));
    }

    /**
     * Метод позволяет проверить автора задачи с отправителем
     *
     * @param task           принимает в качестве параметра task
     * @param authentication принимает в качестве параметра пользователя авторизованной сессии
     */
    private void checkAuthorAndSender(Task task, Authentication authentication) {
        if (!task.getAuthor().getEmail().equals(getAuthor(authentication).getEmail())) {
            throw new ErrorResponse(HttpStatus.BAD_REQUEST, "Автор задачи не совпадает с отправителем.");
        }
    }

    /**
     * Метод позволяет проверить исполнителя задачи с отправителем
     *
     * @param task           принимает в качестве параметра task
     * @param authentication принимает в качестве параметра пользователя авторизованной сессии
     */
    private void checkExecutorAndSender(Task task, Authentication authentication) {
        if (task.getExecutor() != null && !task.getExecutor().getEmail().equals(getAuthor(authentication).getEmail())) {
            throw new ErrorResponse(HttpStatus.BAD_REQUEST, "Исполнитель задачи не совпадает с отправителем.");
        }
    }
}
