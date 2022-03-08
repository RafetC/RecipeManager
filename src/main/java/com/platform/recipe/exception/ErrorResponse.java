package com.platform.recipe.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class ErrorResponse {
    private HttpStatus status;
    private String errorCode;
    private List<String> message;
}



