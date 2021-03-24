package com.mynt.deliveryparcelservice.exception;

import com.mynt.deliveryparcelservice.constant.ErrorMessageEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class ApplicationExceptionHandler extends AbstractExceptionHandler{

    @ExceptionHandler(DataAccessException.class)
    protected ResponseEntity<Object> handleDataAccessException(DataAccessException ex) {
        ApiError apiError = new ApiError();
        apiError.setStatusCode(HttpStatus.NOT_FOUND.value());
        apiError.setUserMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
        apiError.setDeveloperMessage(ErrorMessageEnum.getEnum(HttpStatus.NOT_FOUND.value()).getMessage() + " - " +  ex.getMessage());
        apiError.setDetails(ErrorMessageEnum.MORE_DETAILS.getMessage());
        log.info(apiError.getDeveloperMessage());

        return buildResponseEntity(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ ApplicationException.class, Exception.class })
    protected ResponseEntity<Object> handleApplicationException(Exception ex) {
        ApiError apiError = new ApiError();
        apiError.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiError.setUserMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        apiError.setDeveloperMessage(ErrorMessageEnum.getEnum(HttpStatus.INTERNAL_SERVER_ERROR.value()).getMessage() + " - " + ex.getMessage());
        log.info(apiError.getDeveloperMessage());

        return buildResponseEntity(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> buildResponseEntity(ApiError apiError, HttpStatus httpStatus) {
        return new ResponseEntity<>(apiError, httpStatus);
    }
}
