package com.lorentzonsolutions.relativelysocial.userservice.service.controller;


import com.google.gson.Gson;
import com.lorentzonsolutions.relativelysocial.userservice.service.exceptions.UserCreationException;
import com.lorentzonsolutions.relativelysocial.userservice.service.model.UserCredentials;
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

    private Logger logger = Log.getLogger(UserController.class);

    public UserController(UserService service) {
        this.service = service;
        this.gson = new Gson();

        post("/signup", this::signup, gson::toJson);
    }


    private Object signup(Request request, Response response) {
        logger.info("Method: POST, Path: /signup");
        response.type(contentType);

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
