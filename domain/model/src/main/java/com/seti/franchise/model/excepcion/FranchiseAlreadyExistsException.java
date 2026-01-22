package com.seti.franchise.model.excepcion;

public class FranchiseAlreadyExistsException extends RuntimeException {

    public FranchiseAlreadyExistsException(String message) {
        super(message);
    }

}
