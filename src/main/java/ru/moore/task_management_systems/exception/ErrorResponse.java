package ru.moore.task_management_systems.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
public class ErrorResponse extends RuntimeException {

    private HttpStatus status;
    private String message;
    private Date timestamp;

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
