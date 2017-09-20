package com.lorentzonsolutions.relativelysocial.relationsservice;

import com.lorentzonsolutions.relativelysocial.relationsservice.controller.RelationsController;
import com.lorentzonsolutions.relativelysocial.relationsservice.service.RelationsService;

/**
 * Main class for relations service.
 *
 * @author Johan Lorentzon
 * @since 2017-09-20
 *
 */
public class Main {
    public static void main(String[] args) {
        new RelationsController(new RelationsService());
    }
}
