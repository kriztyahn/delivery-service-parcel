package com.mynt.deliveryparcelservice.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum ErrorMessageEnum {
    NOT_FOUND(404, " Requested resource not found"),
    UNAUTHORIZED(401, " The request doesn't have required token to authenticated or the information is forbidden"),
    DATA_CONSTRAINT(409, "Data validation error"),
    SERVICE_FAILURE(500, "The application failed."),
    INVALID_REQUEST(400, " Request sent to the application is invalid"),
    MORE_DETAILS("Please refer to for more details");

    private final String message;
    private int statusCode;

    ErrorMessageEnum(String message) {
        this.message = message;
    }

    ErrorMessageEnum(int statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public static ErrorMessageEnum getEnum(int statusCode) {
        final Optional<ErrorMessageEnum> optionalErrorMessageEnum = Arrays.stream(ErrorMessageEnum.values()).filter(error -> error.getStatusCode() == statusCode).findFirst();
        return optionalErrorMessageEnum.isPresent() ? optionalErrorMessageEnum.get() : SERVICE_FAILURE;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
