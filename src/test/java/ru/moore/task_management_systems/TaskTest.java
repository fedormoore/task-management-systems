package ru.moore.task_management_systems;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import ru.moore.task_management_systems.controller.AuthController;
import ru.moore.task_management_systems.controller.TaskController;
import ru.moore.task_management_systems.dto.TaskDto;
import ru.moore.task_management_systems.dto.auth.SignInRequestDTO;
import ru.moore.task_management_systems.enums.TaskPriorityEnum;
import ru.moore.task_management_systems.enums.TaskStatusEnum;
import ru.moore.task_management_systems.model.Task;
import ru.moore.task_management_systems.security.JwtProvider;
import ru.moore.task_management_systems.utils.MapperUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest(classes = TaskManagementSystemsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class TaskTest {

    @Autowired
    private AuthController authController;

    @Autowired
    private TaskController taskController;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    MapperUtils mapperUtils;

    static String accessTokenOneUser;

    static Task task;

    @BeforeEach
    void beforeAll() {
        SignInRequestDTO signInRequestDTO = new SignInRequestDTO();
        signInRequestDTO.setEmail("fedormoore@gmail.com");
        signInRequestDTO.setPassword("fedormoore@gmail.com");
        accessTokenOneUser = authController.signIn(signInRequestDTO).getBody().getAccessToken();
    }

    @Test
    @Order(1)
    public void newTask() {
        TaskDto taskDto = new TaskDto();
        taskDto.setPriority(TaskPriorityEnum.LOW.toString());
        taskDto.setHeader("Header");
        taskDto.setText("Text");

        ResponseEntity<TaskDto> returnTaskDto = taskController.newTask(taskDto, getAuthentication(accessTokenOneUser));

        task = mapperUtils.map(returnTaskDto.getBody(), Task.class);
        Assertions.assertEquals(returnTaskDto.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @Order(2)
    public void editTask() {
        TaskDto taskDto = mapperUtils.map(task, TaskDto.class);
        taskDto.setPriority(TaskPriorityEnum.HIGH.toString());
        taskDto.setHeader("Header 555");
        taskDto.setText("Text 555");

        ResponseEntity<TaskDto> returnTaskDto = taskController.editTask(taskDto, getAuthentication(accessTokenOneUser));

        Assertions.assertEquals(returnTaskDto.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @Order(3)
    public void changeStatusTask() {
        TaskDto taskDto = mapperUtils.map(task, TaskDto.class);
        taskDto.setStatus(TaskStatusEnum.WORK.toString());

        ResponseEntity<TaskDto> returnTaskDto = taskController.changeStatusTask(taskDto, getAuthentication(accessTokenOneUser));

        Assertions.assertEquals(returnTaskDto.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @Order(4)
    public void changeExecutorTask() {
        TaskDto taskDto = mapperUtils.map(task, TaskDto.class);
        taskDto.setExecutorId(UUID.fromString("af2aa31f-f12d-44d6-8dbf-4b304bd334e9"));

        ResponseEntity<TaskDto> returnTaskDto = taskController.changeExecutorTask(taskDto, getAuthentication(accessTokenOneUser));

        Assertions.assertEquals(returnTaskDto.getStatusCode(), HttpStatus.OK);
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = jwtProvider.getAccessClaims(accessToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        List<GrantedAuthority> authorities = new ArrayList<>();
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

}
