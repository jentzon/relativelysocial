package com.lorentzonsolutions.relativelysocial.apigateway.service;

import static spark.Spark.*;

/**
 * Entrypoint for the API gateway controller for Relatively Social.
 *
 * @author Johan Lorentzon
 * @since 2017-07-02
 */
public class InitREST {
    public static void main(String[] args) {

        get("/", (req, res) -> "Relatively Social APIGateway controller greets you!");

        get("/help", (req, res) -> "Help is on the way!");

    }
}
