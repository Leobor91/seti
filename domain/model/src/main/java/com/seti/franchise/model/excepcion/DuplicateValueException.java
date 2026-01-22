package com.seti.franchise.model.excepcion;

public class DuplicateValueException extends RuntimeException {

    public DuplicateValueException(String message) {
        super(message);
    }

}
