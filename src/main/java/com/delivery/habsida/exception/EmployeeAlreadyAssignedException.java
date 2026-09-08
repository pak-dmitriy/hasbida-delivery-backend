package com.delivery.habsida.exception;

public class EmployeeAlreadyAssignedException extends RuntimeException {
    public EmployeeAlreadyAssignedException(String message) {
        super(message);
    }
}
