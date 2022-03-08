package com.platform.recipe.exception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
public class RecipePlatformControllerAdvice {

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ErrorResponse> handleGameBusinessException(EmptyResultDataAccessException businessException) {
        ErrorResponse errorHandlerObject = new ErrorResponse();
        List<String> messages = new ArrayList<>();
        messages.add(businessException.getMessage());
        messages.add(ErrorCode.INVALID_RECIPE_ID_FOR_DELETION.getErrMsgKey());
        errorHandlerObject.setErrorCode(ErrorCode.INVALID_RECIPE_ID_FOR_DELETION.getErrCode());


        errorHandlerObject.setMessage(messages);
        errorHandlerObject.setStatus(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorHandlerObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecipePlatformRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleGameBusinessException(RecipePlatformRuntimeException businessException) {
        ErrorResponse errorHandlerObject = new ErrorResponse();
        List<String> messages = new ArrayList<>();
        messages.add(businessException.getErrMsgKey());
        errorHandlerObject.setErrorCode(businessException.getErrorCode());
        errorHandlerObject.setMessage(messages);
        errorHandlerObject.setStatus(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorHandlerObject, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<String> errorList = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.toList());
        ErrorResponse errorHandlerObject = new ErrorResponse();
        errorHandlerObject.setStatus(HttpStatus.BAD_REQUEST);
        errorHandlerObject.setErrorCode(HttpStatus.BAD_REQUEST.toString());
        errorHandlerObject.setMessage(errorList);

        return new ResponseEntity(errorHandlerObject, HttpStatus.BAD_REQUEST);
    }

}
