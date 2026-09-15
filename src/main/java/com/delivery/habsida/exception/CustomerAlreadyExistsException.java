package com.delivery.habsida.exception;

public class CustomerAlreadyExistsException extends  RuntimeException {
    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
}
