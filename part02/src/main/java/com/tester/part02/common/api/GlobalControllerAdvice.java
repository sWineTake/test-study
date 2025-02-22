package com.tester.part02.common.api;


import com.tester.part02.common.api.dto.ErrorMessage;
import com.tester.part02.common.exception.AccessDeniedException;
import com.tester.part02.common.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNoSuchElementException(ResourceNotFoundException ex)  {
        return new ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage handleAccessDeniedException(AccessDeniedException ex) {
        return new ErrorMessage(HttpStatus.FORBIDDEN.value(), ex.getMessage(), LocalDateTime.now());
    }
}
