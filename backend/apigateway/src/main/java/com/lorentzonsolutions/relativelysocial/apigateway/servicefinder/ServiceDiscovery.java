package com.lorentzonsolutions.relativelysocial.apigateway.servicefinder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class for finding service IP in internal docker network.
 *
 * @author Johan Lorentzon
 * @since 2017-08-25
 */
public class ServiceDiscovery {

    private static String allServicesAddress = "http://172.18.0.2:8500/v1/catalog/services";
    private static String serviceAddress = "http://172.18.0.2:8500/v1/catalog/service/";

    private static ServiceDiscovery instance;

    private ServiceDiscovery() {}

    public static ServiceDiscovery getInstance() {
        if(instance == null) instance = new ServiceDiscovery();
        return instance;
    }

    //TODO. Fix error message handling.
    public String getAvailableServices() {
        StringBuilder address = new StringBuilder();
        List<String> services = new ArrayList<>();

        try {
            URL allServices = new URL(allServicesAddress);
            BufferedReader urlInput = new BufferedReader(new InputStreamReader(allServices.openStream()));
            String line;
            while ((line = urlInput.readLine()) != null) address.append(line);

            if(!address.toString().equals("")) {
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(address.toString());
                Iterator keyIterator = jsonObject.keySet().iterator();

                while (keyIterator.hasNext()) {
                    String key = (String) keyIterator.next();
                    services.add(key);
                }

            }

        } catch (MalformedURLException e) {
            System.out.println("Could not connect to Consul.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Could not open input stream.");
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Could not parse JSON.");
            e.printStackTrace();
        } catch (ClassCastException e) {
            System.out.println("JSON object conversion error.");
            e.printStackTrace();
        }


        return services.toString();
    }

}
