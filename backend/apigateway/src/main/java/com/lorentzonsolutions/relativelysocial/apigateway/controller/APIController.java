package com.lorentzonsolutions.relativelysocial.apigateway.controller;


import com.google.gson.Gson;
import com.lorentzonsolutions.relativelysocial.apigateway.exceptions.ServiceNotSupportedException;
import com.lorentzonsolutions.relativelysocial.apigateway.service.APIService;
import com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.ServiceDiscovery;
import com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.exceptions.ServiceDiscoveryException;
import com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.exceptions.ServiceNotFoundException;
import com.lorentzonsolutions.relativelysocial.apigateway.servicehandler.ServiceHandlerFacotory;
import com.lorentzonsolutions.relativelysocial.apigateway.servicehandler.impl.AuthServiceHandler;
import com.lorentzonsolutions.relativelysocial.apigateway.servicehandler.impl.UserServiceHandler;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Iterator;
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

        ServiceDiscovery.getInstance().writeAuthConnectionInfoToFile();

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
            AuthServiceHandler authServiceHandler = (AuthServiceHandler) ServiceHandlerFacotory.getServiceHandler(ServiceHandlerFacotory.AUTH_SERVICE);

            Map<String, String> body = new HashMap<>();
            JSONObject requestBody = new JSONObject(request.body());

            body.put("email", requestBody.getString("email"));
            body.put("firstName", requestBody.getString("firstName"));
            body.put("lastName", requestBody.getString("lastName"));
            body.put("password", requestBody.getString("password"));

            Map<String, String> headers = new HashMap<>();
            Iterator headerIterator = request.headers().iterator();
            while(headerIterator.hasNext()) {
                String headerKey = (String) headerIterator.next();
                headers.put(headerKey, request.headers(headerKey));
            }

            headers.put("Authorization", "Bearer " + authServiceHandler.acquireAdminToken());

            return handler.POSTMethod("/createuser", body, headers);

        } catch (ServiceNotSupportedException e) {
            logger.warn("Could not reach service.");
            logger.warn(e.getMessage());
            e.printStackTrace();
            return e.getMessage();
        } catch (JSONException e) {
            logger.warn("Request body error");
            e.printStackTrace();
            return e.getMessage();
        }

    }


}
