package com.lorentzonsolutions.relativelysocial.familyservice.controller;

import com.lorentzonsolutions.relativelysocial.familyservice.service.FamilyService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

/**
 *  Controller class for family service for Relatively Social.
 *
 * @author Johan Lorentzon
 * @since 2017-09-20
 */
public class FamilyController {

    private static String contentType = "application/json";
    FamilyService service;
    Gson gson;

    public FamilyController(FamilyService service) {

        gson = new Gson();
        this.service = service;

        get("/help", this::help, gson::toJson);

        System.out.println("Family controller running...");
    }


    private Object help(Request req, Response res) {
        res.type(contentType);
        return "Family Service greats you!";
    }
}
