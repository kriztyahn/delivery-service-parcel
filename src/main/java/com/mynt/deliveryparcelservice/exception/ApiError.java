package com.mynt.deliveryparcelservice.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ApiError {

    @JsonProperty("status_code")
    private int statusCode;

    @JsonProperty("user_message")
    private String userMessage;

    @JsonProperty("developer_message")
    private String developerMessage;

    @JsonProperty("details")
    private String details;
}
