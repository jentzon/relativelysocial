package com.lorentzonsolutions.relativelysocial.userservice.service.requestvalidation;

public class RequestValidationException extends Exception {

    public RequestValidationException() {
        super();
    }

    public RequestValidationException(Throwable e) {
        super(e);
    }

    public RequestValidationException(String message) {
        super(message);
    }
}
