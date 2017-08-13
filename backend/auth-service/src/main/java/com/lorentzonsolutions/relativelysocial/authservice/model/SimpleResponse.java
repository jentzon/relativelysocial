package com.lorentzonsolutions.relativelysocial.authservice.model;

/**
 * A class to represent a simple response opbject to be converted with Google Gson.
 *
 * @author Johan Lorentzon
 * @since 2017-07-09
 */
public class SimpleResponse {
    public String message;

    public SimpleResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
