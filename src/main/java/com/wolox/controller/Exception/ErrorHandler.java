package com.wolox.controller.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(AlbumAccessException.class)
    public ResponseEntity notFoundHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            javax.validation.ConstraintViolationException.class
    })
    public ResponseEntity badRequestHandler(Exception e) {
        return ResponseEntity.badRequest().body(new Response(e.getMessage()));
    }

    @Data
    @AllArgsConstructor
    private static class Response {
        private String message;
    }
}
