package com.lorentzonsolutions.relativelysocial.authservice.service;

import com.lorentzonsolutions.relativelysocial.authservice.model.AuthException;
import com.lorentzonsolutions.relativelysocial.authservice.model.Token;
import com.lorentzonsolutions.relativelysocial.authservice.tokens.JWTGenerator;

public class AuthenticationService {

    public Token authenticateUser(String user, String password) throws AuthException {
        if(validateUser(user, password)) return JWTGenerator.getInstance().generateToken(user);
        else {
            AuthException exception = new AuthException();
            exception.setRestStatusCode(401);
            exception.setInfo("No user exist with given credentials.");
            exception.setError("Authentication failed.");
            throw exception;
        }
    }

    public boolean validateToken(String token) {
        return JWTGenerator.getInstance().validateToken(token);
    }



    // TODO. Check with userservice if user exists.
    private boolean validateUser(String user, String password) {
        if(user.equals("test") && password.equals("test")) return true;
        else return false;
    }
}
