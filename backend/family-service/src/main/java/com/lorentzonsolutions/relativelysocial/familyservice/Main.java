package com.lorentzonsolutions.relativelysocial.familyservice;

import com.lorentzonsolutions.relativelysocial.familyservice.controller.FamilyController;
import com.lorentzonsolutions.relativelysocial.familyservice.service.FamilyService;

/**
 * Main class for family service.
 *
 * @author Johan Lorentzon
 * @since 2017-09-20
 *
 */
public class Main {
    public static void main(String[] args) {
        new FamilyController(new FamilyService());
    }
}
