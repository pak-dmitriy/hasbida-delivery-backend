package com.delivery.habsida.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }

    @ExceptionHandler(StoreNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleStoreNotFoundException(StoreNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(EmployeeAlreadyAssignedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handlerEmployeeAlreadyAssignedException(EmployeeAlreadyAssignedException ex) {return ex.getMessage();}

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlerRoleNotFoundException(RoleNotFoundException ex) {return ex.getMessage();}

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlerUserNotFoundException(UserNotFoundException ex) {return ex.getMessage();}

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handlerDataIntegrityViolationException(DataIntegrityViolationException ex) {return "A record with this value already exists";}

}