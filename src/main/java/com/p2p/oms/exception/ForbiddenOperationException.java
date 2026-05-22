package com.p2p.oms.exception;

public class ForbiddenOperationException extends ApiException {

    public ForbiddenOperationException(String message) {
        super("FORBIDDEN_OPERATION", message);
    }
}