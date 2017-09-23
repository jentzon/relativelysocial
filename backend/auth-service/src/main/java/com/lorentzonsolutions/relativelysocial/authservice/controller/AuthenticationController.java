package com.lorentzonsolutions.relativelysocial.authservice.controller;

import com.google.gson.Gson;
import com.lorentzonsolutions.relativelysocial.authservice.model.SimpleResponse;
import com.lorentzonsolutions.relativelysocial.authservice.service.AuthenticationService;
import com.lorentzonsolutions.relativelysocial.authservice.model.AuthException;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

/**
 * AuthenticationController for the Auth service for Relatively Social.
 *
 * @author Johan Lorentzon
 * @since 2017-07-02
 */
public class AuthenticationController {

    private static String contentType = "application/json";
    AuthenticationService service;
    Gson gson;

    public AuthenticationController(AuthenticationService service) {

        gson = new Gson();
        this.service = service;

        post("/login", this::loginUser, gson::toJson);

        get("/validate", this::validateToken, gson::toJson);

        System.out.println("Authentication controller running...");
    }


    private Object loginUser(Request req, Response res) {
        res.type(contentType);

        try {
            JSONObject body = new JSONObject(req.body());
            String user = body.getString("user");
            String password = body.getString("pass");

            return service.authenticateUser(user, password);

        } catch (JSONException e) {

            AuthException exception = new AuthException();
            exception.setRestStatusCode(400);
            exception.setError(e.getMessage());
            exception.setInfo("Could not parse request body");
            res.status(exception.getRestStatusCode());

            return exception;

        } catch (AuthException e) {
            res.status(e.getRestStatusCode());
            return e;

        }
    }

    private Object validateToken(Request req, Response res) {
        res.type(contentType);
        String tokenHeader = req.headers("Authorization");

        try {
            if (service.validateToken(extractTokenValue(tokenHeader))) {
                res.status(202);
                System.out.println("Token valid.");
                return new SimpleResponse("Token valid.");
            } else {
                res.status(401);
                System.out.println("Token not valid.");
                return new SimpleResponse("Token not valid.");
            }
        }
        catch (AuthException e) {
            res.status(e.getRestStatusCode());
            return e;
        }
    }

    /*
    Extracts the token value from token header. Expected format of header is "Bearer: zyx.zyx.zyx"
     */
    private String extractTokenValue(String tokenHeader) throws AuthException{
        String[] values = tokenHeader.split("\\s");
        if(values.length > 1) {
            return values[1];
        } else {
            AuthException exception = new AuthException();
            exception.setRestStatusCode(404);
            exception.setError("Token header error.");
            exception.setInfo("Not a valid token header format.");
            throw exception;
        }
    }

}