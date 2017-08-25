package com.lorentzonsolutions.relativelysocial.apigateway.servicefinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class for finding service IP in internal docker network.
 *
 * @author Johan Lorentzon
 * @since 2017-08-25
 */
public class ServiceDiscovery {

    private static String allServicesAddress = "172.18.0.2:8500/v1/catalog/services";
    private static String serviceAddress = "172.18.0.2:8500/v1/catalog/service/";

    private static ServiceDiscovery instance;

    private ServiceDiscovery() {}

    public static ServiceDiscovery getInstance() {
        if(instance == null) instance = new ServiceDiscovery();
        return instance;
    }

    //TODO. Fix error message handling.
    public String findServiceAddress(String serviceName) {
        StringBuilder address = new StringBuilder();

        try {
            URL allServices = new URL(allServicesAddress);
            BufferedReader urlInput = new BufferedReader(new InputStreamReader(allServices.openStream()));
            String line;
            while ((line = urlInput.readLine()) != null) address.append(line);

        } catch (MalformedURLException e) {
            System.out.println("Could not connect to Consul.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Could not open input stream.");
            e.printStackTrace();
        }


        return address.toString();
    }

}
