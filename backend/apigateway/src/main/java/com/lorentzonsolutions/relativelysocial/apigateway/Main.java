package com.lorentzonsolutions.relativelysocial.apigateway;

import com.lorentzonsolutions.relativelysocial.apigateway.controller.APIController;
import com.lorentzonsolutions.relativelysocial.apigateway.service.APIService;

/**
 * Main class for API gateway service.
 *
 * @author Johan Lorentzon
 * @since 2017-09-20
 *
 */

public class Main {
    public static void main(String[] args) {
        new APIController(new APIService());
    }
}
