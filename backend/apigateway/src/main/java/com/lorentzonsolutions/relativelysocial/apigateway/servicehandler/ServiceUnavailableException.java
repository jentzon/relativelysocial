package com.lorentzonsolutions.relativelysocial.apigateway.servicehandler;

/**
 * Created by johanlorentzon on 2017-09-20.
 */
public class ServiceUnavailableException extends Exception {

    public ServiceUnavailableException(String message) {
        super(message);
    }
}
