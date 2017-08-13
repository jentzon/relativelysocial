package com.lorentzonsolutions.relativelysocial.authservice.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Wrapping exceptionclass for handling exceptions in tokens controller.
 *
 * @author Johan Lorentzon
 * @since 2017-07-03
 *
 */
public class AuthException extends Exception {

    private int restStatusCode;
    private String info;
    private String error;

    public AuthException() {
        super();
    }

    public void setRestStatusCode(int code) {
        this.restStatusCode = code;
    }

    public int getRestStatusCode() {
        return this.restStatusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
