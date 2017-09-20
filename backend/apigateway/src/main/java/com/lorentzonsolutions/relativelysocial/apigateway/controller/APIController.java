package com.lorentzonsolutions.relativelysocial.apigateway.controller;


import com.google.gson.Gson;
import com.lorentzonsolutions.relativelysocial.apigateway.service.APIService;
import com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.ServiceDiscovery;
import com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.ServiceDiscoveryException;
import com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.ServiceNotFoundException;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import spark.Request;
import spark.Response;

import static spark.Spark.get;

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
    }



    private Object listservices(Request request, Response response) {
        logger.info("Method: GET, Path: /listservices");
        return service.listservices();
    }

    private Object findservice(Request request, Response response) {
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


}
