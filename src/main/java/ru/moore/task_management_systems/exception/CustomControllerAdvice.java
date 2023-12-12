package ru.moore.task_management_systems.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<?> handleConstraintViolation(Exception ex) {
        String errors = ex.getMessage();
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);
        final ResponseEntity responseEntity = new ResponseEntity(error, HttpStatus.BAD_REQUEST);
        return responseEntity;
    }

}
