package com.game.mancala.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionAdvice {


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionInfo> handleNotFoundException(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionInfo.builder()
                        .message(exception.getMessage())
                        .status(HttpStatus.NOT_FOUND.value())
                        .build()
                );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionInfo> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(ExceptionInfo.builder()
                        .message(exception.getMessage())
                        .status(HttpStatus.NOT_ACCEPTABLE.value())
                        .build()
                );
    }


}
