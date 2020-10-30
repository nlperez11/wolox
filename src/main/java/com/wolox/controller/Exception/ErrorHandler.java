package com.wolox.controller.Exception;

import com.wolox.app.WoloxChallengeApplication;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class ErrorHandler {

    private static Logger logger = LoggerFactory.getLogger(WoloxChallengeApplication.class);

    @ExceptionHandler(AlbumAccessException.class)
    public ResponseEntity notFoundHandler(Exception e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            javax.validation.ConstraintViolationException.class,
            NumberFormatException.class
    })
    public ResponseEntity badRequestHandler(Exception e) {
        logger.error(e.getMessage());
        return ResponseEntity.badRequest().body(new Response(e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity unprocessableEntityHandler(Exception e) {
        logger.error(e.getMessage());
        return ResponseEntity.unprocessableEntity().body(new Response(e.getMessage()));
    }

    @ExceptionHandler({
            IOException.class,
            InterruptedException.class
    })
    public ResponseEntity externalApiErrorHandler(Exception e) {
        logger.error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new Response("An error has received when call external api"));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity unexpectedErrorHandler(Exception e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An unexpected error has occurred"));
    }

    @Data
    @AllArgsConstructor
    private static class Response {
        private String message;
    }
}
