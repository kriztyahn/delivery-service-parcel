package com.mynt.deliveryparcelservice.exception;

import com.mynt.deliveryparcelservice.constant.ErrorMessageEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public abstract class AbstractExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiError apiError = new ApiError();
        apiError.setStatusCode(status.value());
        apiError.setUserMessage("Request method not supported");
        apiError.setDeveloperMessage(ErrorMessageEnum.getEnum(status.value()).getMessage() + " - " + ex.getMessage() + (ex.getSupportedMethods() != null ? String.join(",", ex.getSupportedMethods()) : ""));
        log.info(apiError.getDeveloperMessage());
        return buildResponseEntity(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiError apiError = new ApiError();
        apiError.setStatusCode(status.value());
        apiError.setUserMessage("Media type not supported");
        apiError.setDeveloperMessage(ErrorMessageEnum.getEnum(status.value()).getMessage() + " - "  + ex.getMessage() + ex.getContentType());
        log.info(apiError.getDeveloperMessage());

        return buildResponseEntity(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiError apiError = new ApiError();
        apiError.setStatusCode(status.value());
        apiError.setUserMessage("URI parameters required");
        apiError.setDeveloperMessage(ErrorMessageEnum.getEnum(status.value()).getMessage() + " - " + ex.getMessage());
        log.info(apiError.getDeveloperMessage());

        return buildResponseEntity(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiError apiError = new ApiError();
        apiError.setStatusCode(status.value());
        apiError.setUserMessage("Validation failure");

        List<String> errors = new ArrayList();
        for(FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        apiError.setDeveloperMessage(ErrorMessageEnum.getEnum(status.value()).getMessage() + " - " + ex.getMessage());
        apiError.setDetails(ErrorMessageEnum.MORE_DETAILS.getMessage());
        log.info(apiError.getDeveloperMessage());

        return buildResponseEntity(apiError, status);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        ApiError apiError = new ApiError();
        apiError.setStatusCode(HttpStatus.BAD_REQUEST.value());
        apiError.setUserMessage("Constraint Violation");

        List<String> errors = new ArrayList();
        for(ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
        }

        apiError.setDeveloperMessage(String.join(",", errors));
        apiError.setDetails(ErrorMessageEnum.MORE_DETAILS.getMessage());
        log.info(apiError.getDeveloperMessage());

        return buildResponseEntity(apiError, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiError apiError = new ApiError();
        apiError.setStatusCode(status.value());
        apiError.setUserMessage("Request Message not readable");
        apiError.setDeveloperMessage(ErrorMessageEnum.getEnum(status.value()).getMessage() + " - " + ex.getMessage());
        apiError.setDetails(ErrorMessageEnum.MORE_DETAILS.getMessage());
        log.info(apiError.getDeveloperMessage());

        return buildResponseEntity(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError();
        apiError.setStatusCode(status.value());
        apiError.setUserMessage("Error writing response message");
        apiError.setDeveloperMessage(ErrorMessageEnum.getEnum(status.value()).getMessage() + " - " + ex.getMessage());
        log.info(apiError.getDeveloperMessage());

        return buildResponseEntity(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError();
        apiError.setStatusCode(status.value());
        apiError.setUserMessage("No handle found for request URL");
        apiError.setDeveloperMessage(ErrorMessageEnum.getEnum(status.value()).getMessage() + " - " + ex.getMessage());
        log.info(apiError.getDeveloperMessage());

        return buildResponseEntity(apiError, status);
    }

    protected abstract ResponseEntity<Object> buildResponseEntity(ApiError apiError, HttpStatus httpStatus);
}
