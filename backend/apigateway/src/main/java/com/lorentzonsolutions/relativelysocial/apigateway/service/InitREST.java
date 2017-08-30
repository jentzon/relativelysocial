package com.lorentzonsolutions.relativelysocial.apigateway.service;

import com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.ServiceDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

/**
 * Entrypoint for the API gateway controller for Relatively Social.
 *
 * @author Johan Lorentzon
 * @since 2017-07-02
 */
public class InitREST {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger("TEST");

        get("/", (req, res) -> "Relatively Social APIGateway controller greets you!");

        get("/help", (req, res) -> "Help is on the way!");

        get("/listservices", (req, res) -> {
            return ServiceDiscovery.getInstance().getAvailableServices();
        });

        get("/findservice", (req, res) -> {
            logger.debug("TEST!");
           return ServiceDiscovery.getInstance().getService(req.params("servicename"));
        });

    }
}
