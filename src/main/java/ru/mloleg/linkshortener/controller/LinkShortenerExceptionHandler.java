package ru.mloleg.linkshortener.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.mloleg.linkshortener.dto.common.CommonResponse;
import ru.mloleg.linkshortener.dto.common.ValidationError;
import ru.mloleg.linkshortener.exception.NotFoundPageException;

import java.util.List;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class LinkShortenerExceptionHandler {

    private final String notFoundPage;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
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

    @ExceptionHandler(NotFoundPageException.class)
    public ResponseEntity<String> handleNotFoundPageException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .contentType(MediaType.TEXT_HTML)
                             .body(notFoundPage);
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
