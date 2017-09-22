package com.lorentzonsolutions.relativelysocial.authservice.service;

import com.lorentzonsolutions.relativelysocial.authservice.model.AuthException;
import com.lorentzonsolutions.relativelysocial.authservice.model.Token;
import com.lorentzonsolutions.relativelysocial.authservice.tokens.JWTGenerator;

/**
 * Service class for the Auth controller.
 *
 * @author Johan Lorentzon
 * @since 2017-07-02
 */

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



    // TODO. Connect to DB for user information.
    private boolean validateUser(String user, String password) {
        if(user.equals("admin") && password.equals("admin")) return true;
        else return false;
    }
}
