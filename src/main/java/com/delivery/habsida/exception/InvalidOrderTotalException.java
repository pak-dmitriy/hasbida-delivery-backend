package com.delivery.habsida.exception;

public class InvalidOrderTotalException extends RuntimeException {
    public InvalidOrderTotalException(String message) {
        super(message);
    }
}
