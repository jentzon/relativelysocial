package com.lorentzonsolutions.relativelysocial.userservice.service;

import com.lorentzonsolutions.relativelysocial.userservice.service.controller.UserController;
import com.lorentzonsolutions.relativelysocial.userservice.service.service.UserService;

/**
 * Main class for user service.
 *
 * @author Johan Lorentzon
 * @since 2017-09-21
 *
 */

public class Main {
    public static void main(String[] args) {
        new UserController(new UserService());
    }
}
