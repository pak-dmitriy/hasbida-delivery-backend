package com.delivery.habsida.exception;

import com.delivery.habsida.dto.ErrorResponse;
import com.delivery.habsida.entity.ProductStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidCredentialsException(InvalidCredentialsException ex, HttpServletRequest request) {
        return new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                List.of(ex.getMessage()),
                LocalDateTime.now(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getBindingResult().getAllErrors().stream().map(error -> error.getDefaultMessage()).toList(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(StoreNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleStoreNotFoundException(StoreNotFoundException ex, HttpServletRequest request) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
        List.of(ex.getMessage()),
        LocalDateTime.now(),
        request.getRequestURI()
        );
    }

    @ExceptionHandler(EmployeeAlreadyAssignedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerEmployeeAlreadyAssignedException(EmployeeAlreadyAssignedException ex, HttpServletRequest request) {
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                List.of(ex.getMessage()),
                LocalDateTime.now(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerRoleNotFoundException(RoleNotFoundException ex, HttpServletRequest request) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                List.of(ex.getMessage()),
                LocalDateTime.now(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                List.of(ex.getMessage()),
                LocalDateTime.now(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        String message = "A record with this value already exists";

        Throwable cause = ex.getMostSpecificCause();
        if (cause instanceof SQLException sqlException) {
            if ("23503".equals(sqlException.getSQLState())) {
                message = "Cannot delete this record because other records depend on it";
            }
        }

        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                List.of(message),
                LocalDateTime.now(),
                request.getRequestURI()
        );
    }


    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerCategoryNotFoundException(CategoryNotFoundException ex, HttpServletRequest request) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                List.of(ex.getMessage()),
                LocalDateTime.now(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerProductNotFoundException(ProductNotFoundException ex, HttpServletRequest request){
       return new ErrorResponse(
               HttpStatus.NOT_FOUND.value(),
               List.of(ex.getMessage()),
               LocalDateTime.now(),
               request.getRequestURI()
       );
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerCustomerNotFoundException(CustomerNotFoundException ex, HttpServletRequest request) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                List.of(ex.getMessage()),
                LocalDateTime.now(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(CustomerAddressNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerCustomerAddressNotFoundException(CustomerAddressNotFoundException ex, HttpServletRequest request) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                List.of(ex.getMessage()),
                LocalDateTime.now(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerCustomerAlreadyExistsException(CustomerAlreadyExistsException ex, HttpServletRequest request) {
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                List.of(ex.getMessage()),
                LocalDateTime.now(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ProductNotAvailableException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerProductNotAvailableException(ProductNotAvailableException ex, HttpServletRequest request) {
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                List.of(ex.getMessage()),
                LocalDateTime.now(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(InvalidQuantityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerInvalidQuantityException(InvalidQuantityException ex, HttpServletRequest request) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                List.of(ex.getMessage()),
                LocalDateTime.now(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(InsufficientStockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerInsufficientStockException(InsufficientStockException ex, HttpServletRequest request) {
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                List.of(ex.getMessage()),
                LocalDateTime.now(),
                request.getRequestURI()
        );
    }
}