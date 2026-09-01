package com.delivery.habsida.exception;

public class InvalidCredentialsException  extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}