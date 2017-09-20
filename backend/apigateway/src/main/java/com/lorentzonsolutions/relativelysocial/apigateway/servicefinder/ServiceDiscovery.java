package com.lorentzonsolutions.relativelysocial.apigateway.servicefinder;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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

    private Logger logger = Log.getLogger(ServiceDiscovery.class);

    private JSONParser parser = new JSONParser();

    private static ServiceDiscovery instance;

    private ServiceDiscovery() {}

    public static ServiceDiscovery getInstance() {
        if(instance == null) instance = new ServiceDiscovery();
        return instance;
    }

    //TODO. Fix error message handling.
    public List<String> getAvailableServices() {
        List<String> services = new ArrayList<>();

        try {
            String data = readURL(allServicesAddress);

            if(!data.equals("")) {
                JSONObject jsonObject = (JSONObject) parser.parse(data);

                for (Object o : jsonObject.keySet()) {
                    String key = (String) o;
                    services.add(key);
                }
            }

        } catch (MalformedURLException e) {
            logger.warn("Could not connect to Consul.");
            e.printStackTrace();
        } catch (IOException e) {
            logger.warn("Could not open input stream.");
            e.printStackTrace();
        } catch (ParseException e) {
            logger.warn("Could not parse JSON.");
            e.printStackTrace();
        } catch (ClassCastException e) {
            logger.warn("JSON object conversion error.");
            e.printStackTrace();
        }


        return services;
    }

    public String getService(String serviceName) throws ServiceNotFoundException, ServiceDiscoveryException {

        logger.info("Trying to find service: " + serviceName);

        StringBuilder response = new StringBuilder();
        String serviceAddress = "";
        String servicePort = "";

        try {
            String data = readURL(ServiceDiscovery.serviceAddress + serviceName);
            JSONArray jsonArray = (JSONArray) parser.parse(data);

            if(jsonArray.isEmpty()) throw new ServiceNotFoundException();

            for(Object o : jsonArray) {

                if(((JSONObject) o).get("ServiceName").equals(serviceName)) {

                    logger.info("Service found, accessing address and port...");

                    serviceAddress = ((JSONObject) o).get("ServiceAddress").toString();
                    servicePort = ((JSONObject) o).get("ServicePort").toString();

                    logger.info("Address: " + serviceAddress + " Port: " + servicePort);
                }

                if(serviceAddress.equals("")) throw new ServiceDiscoveryException("Service address not available.");
                if(servicePort.equals("")) throw new ServiceDiscoveryException("Service port not available.");
            }
            response.append(serviceAddress); response.append(":"); response.append(servicePort);

            return response.toString();

        } catch (ParseException e) {
            logger.warn(e.toString());
            throw new ServiceDiscoveryException("Error parsing JSON.");
        } catch (IOException e) {
            logger.warn(e.toString());
            throw new ServiceDiscoveryException("Error opening URL stream.");
        }
    }

    private String readURL(String urlAddress) throws IOException {
        URL url = new URL(urlAddress);
        StringBuilder data = new StringBuilder();
        BufferedReader urlInput = new BufferedReader(new InputStreamReader(url.openStream()));

        String line;
        while ((line = urlInput.readLine()) != null) data.append(line);
        return data.toString();
    }

}
