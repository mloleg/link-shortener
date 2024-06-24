package ru.mloleg.linkshortener.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.mloleg.linkshortener.dto.common.CommonResponse;
import ru.mloleg.linkshortener.dto.common.ValidationError;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class LinkShortenerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        List<ValidationError> validationErrors = fieldErrors.stream()
                .map(validationError -> ValidationError.builder()
                        .field(validationError.getField())
                        .message(validationError.getDefaultMessage())
                        .build())
                .toList();

        log.error("Response validation error: {}", validationErrors, e);

        return CommonResponse.builder()
                .errorMessage("Validation error")
                .validationErrorList(validationErrors)
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CommonResponse<?> handleException(Exception e) {
        log.error("An exception occurred: {}", e.getMessage(), e);

        return CommonResponse.builder()
                .errorMessage("An exception occurred: {}" + e.getMessage())
                .build();
    }
}
