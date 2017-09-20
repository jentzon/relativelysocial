package com.lorentzonsolutions.relativelysocial.apigateway.service;

import com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.ServiceDiscovery;
import com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.ServiceDiscoveryException;
import com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.ServiceNotFoundException;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;


import static spark.Spark.*;

/**
 * Entrypoint for the API gateway controller for Relatively Social.
 *
 * @author Johan Lorentzon
 * @since 2017-07-02
 */
public class InitREST {

    public static void main(String[] args) {

        Logger logger = Log.getLogger(InitREST.class);

        get("/", (req, res) -> {
            logger.info("Method: GET, Path: /");
            return "Relatively Social APIGateway controller greets you!";
        });

        get("/help", (req, res) -> {
            logger.info("Method: GET, Path: /help");
            return "Help is on the way!";
        });

        get("/listservices", (req, res) -> {
            logger.info("Method: GET, Path: /listservices");
            return ServiceDiscovery.getInstance().getAvailableServices();
        });

        get("/findservice", (req, res) -> {
            logger.info("Method: GET, Path: /findservice");

            String response = "";

            try {
                response = ServiceDiscovery.getInstance().getService(req.queryParams("servicename"));
            }
            catch (ServiceDiscoveryException | ServiceNotFoundException e) {
                logger.warn(e.toString());

                if(e instanceof ServiceDiscoveryException) res.status(HttpStatus.INTERNAL_SERVER_ERROR_500);

                res.status(HttpStatus.NO_CONTENT_204);
            }

            return response;
        });

    }
}
