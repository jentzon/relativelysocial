package com.lorentzonsolutions.relativelysocial.relationsservice.controller;

import com.lorentzonsolutions.relativelysocial.relationsservice.service.RelationsService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

/**
 *  Controller class for relations service for Relatively Social.
 *
 * @author Johan Lorentzon
 * @since 2017-09-20
 */
public class RelationsController {

    private static String contentType = "application/json";
    RelationsService service;
    Gson gson;

    public RelationsController(RelationsService service) {

        gson = new Gson();
        this.service = service;

        get("/help", this::help, gson::toJson);

        System.out.println("Relations controller running...");
    }


    private Object help(Request req, Response res) {
        res.type(contentType);
        return "Relations Service greats you!";
    }
}
