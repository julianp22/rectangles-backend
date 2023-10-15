package com.julian.rectangles.infrastructure.controller.exception;

import com.julian.rectangles.application.InvalidRectangleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RectangleExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RectangleExceptionHandler.class);
    private static final String DATA_MUST_BE_NUMERIC = "Data must be numeric.";
    private static final String INVALID_RECTANGLE_POINTS = "Invalid order of rectangle points.";

    @ExceptionHandler(InvalidRectangleException.class)
    protected ResponseEntity<Object> handleInvalidRectangleException(InvalidRectangleException exception) {
        Map<String, Object> errorResponse = RectangleExceptionHandler.mapExceptionToResponse(exception, INVALID_RECTANGLE_POINTS);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        List<String> errors = exception.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());

        Map<String, Object> errorResponse = RectangleExceptionHandler.mapExceptionToResponse(exception, errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Object> handleNumberFormatException(NumberFormatException exception) {
        Map<String, Object> errorResponse = RectangleExceptionHandler.mapExceptionToResponse(exception, DATA_MUST_BE_NUMERIC);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private static Map<String, Object> mapExceptionToResponse(Exception exception, Object error) {
        LOGGER.error(exception.getMessage(), exception);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", error);

        return body;
    }

}
