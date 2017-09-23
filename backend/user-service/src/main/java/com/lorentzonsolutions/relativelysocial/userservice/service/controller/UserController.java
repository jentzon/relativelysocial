package com.lorentzonsolutions.relativelysocial.userservice.service.controller;


import com.google.gson.Gson;
import com.lorentzonsolutions.relativelysocial.userservice.service.requestvalidation.RequestValidationException;
import com.lorentzonsolutions.relativelysocial.userservice.service.model.UserCreationException;
import com.lorentzonsolutions.relativelysocial.userservice.service.model.UserCredentials;
import com.lorentzonsolutions.relativelysocial.userservice.service.requestvalidation.RequestValidator;
import com.lorentzonsolutions.relativelysocial.userservice.service.service.UserService;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import spark.Request;
import spark.Response;
import static spark.Spark.post;

/**
 *  Controller class for relations service for Relatively Social.
 *
 * @author Johan Lorentzon
 * @since 2017-09-20
 */

public class UserController {

    private String contentType = "application/json";
    private UserService service;
    private Gson gson;
    private RequestValidator requestValidator;

    private Logger logger = Log.getLogger(UserController.class);

    public UserController(UserService service) {

        this.service = service;
        this.gson = new Gson();

        try {
            this.requestValidator = new RequestValidator();
        } catch (RequestValidationException e) {

            logger.warn("Could not create RequestValidator.");
            logger.warn(e.getMessage());

            e.printStackTrace();
        }

        post("/createuser", this::createUser, gson::toJson);
    }

    private Object createUser(Request request, Response response) {

        logger.info("Method: POST, Path: /createuser");

        response.type(contentType);

        try {
            requestValidator.validateRequest(request);
        }
        catch (RequestValidationException e) {
            logger.warn("Request could not be validated.");
            logger.warn(e.getMessage());
            response.status(HttpStatus.UNAUTHORIZED_401);
            e.printStackTrace();
            return e.getMessage();
        }

        try {
            UserCredentials userCredentials = new UserCredentials(request);
            return service.signup(userCredentials);
        }
        catch (UserCreationException e) {
            response.status(HttpStatus.BAD_REQUEST_400);
            return e.getMessage();
        }
    }
}
