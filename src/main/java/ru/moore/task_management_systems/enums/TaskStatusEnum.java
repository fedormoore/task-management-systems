package ru.moore.task_management_systems.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.moore.task_management_systems.exception.ErrorResponse;

@Getter
public enum TaskStatusEnum {

    NEW("Новая"),
    WORK("В работе"),
    COMPLETED("Завершена");

    private String name;

    TaskStatusEnum(String name) {
        this.name = name;
    }

    public static String convertToEntityAttribute(String value) {
        for (TaskStatusEnum sta : TaskStatusEnum.values()) {
            if (sta.name().equals(value)) {
                return sta.getName();
            }
        }
        throw new ErrorResponse(HttpStatus.BAD_REQUEST, "Unknown database value:" + value);
    }

    public static String convertToDatabaseColumn(String value) {
        for (TaskStatusEnum sta : TaskStatusEnum.values()) {
            if (sta.getName().equals(value)) {
                return sta.name();
            }
            if (sta.name().equals(value)) {
                return sta.name();
            }
        }
        throw new ErrorResponse(HttpStatus.BAD_REQUEST, "Unknown database value: " + value);
    }
}
