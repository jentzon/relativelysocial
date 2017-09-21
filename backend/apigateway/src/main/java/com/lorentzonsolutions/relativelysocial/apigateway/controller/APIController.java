package com.lorentzonsolutions.relativelysocial.apigateway.controller;


import com.google.gson.Gson;
import com.lorentzonsolutions.relativelysocial.apigateway.exceptions.ServiceNotSupportedException;
import com.lorentzonsolutions.relativelysocial.apigateway.service.APIService;
import com.lorentzonsolutions.relativelysocial.apigateway.exceptions.ServiceDiscoveryException;
import com.lorentzonsolutions.relativelysocial.apigateway.exceptions.ServiceNotFoundException;
import com.lorentzonsolutions.relativelysocial.apigateway.servicehandler.ServiceHandler;
import com.lorentzonsolutions.relativelysocial.apigateway.servicehandler.ServiceHandlerFacotory;
import com.lorentzonsolutions.relativelysocial.apigateway.servicehandler.impl.UserServiceHandler;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

public class APIController {

    private static String contentType = "application/json";
    private APIService service;
    private Gson gson;

    private Logger logger = Log.getLogger(APIController.class);

    public APIController(APIService service) {

        this.service = service;
        this.gson = new Gson();

        get("/listservices", this::listservices, gson::toJson);

        get("/findservice", this::findservice, gson::toJson);

        post("/signup", this::signup, gson::toJson);
    }

    private Object listservices(Request request, Response response) {
        response.type(contentType);
        logger.info("Method: GET, Path: /listservices");
        return service.listservices();
    }

    private Object findservice(Request request, Response response) {
        response.type(contentType);
        logger.info("Method: GET, Path: /findservice");

        String serviceName = request.queryParams("servicename");

        try {
            return service.findservice(serviceName);
        }
        catch (ServiceDiscoveryException | ServiceNotFoundException e) {
            logger.warn(e.toString());

            if(e instanceof ServiceDiscoveryException) response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);

            response.status(HttpStatus.NO_CONTENT_204);
        }

        return "";
    }

    private Object signup(Request request, Response response) {
        response.type(contentType);
        logger.info("Method: POST, Path: /signup");

        try {
            UserServiceHandler handler = (UserServiceHandler) ServiceHandlerFacotory.getServiceHandler(ServiceHandlerFacotory.USER_SERVICE);
            Map<String, String> body = new HashMap<>();
            JSONObject requestBody = new JSONObject(request.body());
            body.put("email", requestBody.getString("email"));
            body.put("firstName", requestBody.getString("firstName"));
            body.put("lastName", requestBody.getString("lastName"));
            body.put("password", requestBody.getString("password"));

            handler.POSTMethod("/signup", body);

        } catch (ServiceNotSupportedException e) {
            logger.warn("Could not reach service.");
            e.printStackTrace();
        } catch (JSONException e) {
            logger.warn("Request body error");
            e.printStackTrace();
        }

        // TODO. User service.
        return null;
    }


}
