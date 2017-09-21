package com.lorentzonsolutions.relativelysocial.apigateway.servicefinder;

import com.lorentzonsolutions.relativelysocial.apigateway.exceptions.ServiceDiscoveryException;
import com.lorentzonsolutions.relativelysocial.apigateway.exceptions.ServiceNotFoundException;
import com.lorentzonsolutions.relativelysocial.apigateway.model.ServiceInfo;
import com.lorentzonsolutions.relativelysocial.apigateway.model.ServicesList;
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

    // TODO. Check possibility to make env variables.
    private static String allServicesAddress = "http://172.19.0.3:8500/v1/catalog/services";
    private static String serviceAddress = "http://172.19.0.3:8500/v1/catalog/service/";

    private Logger logger = Log.getLogger(ServiceDiscovery.class);
    private JSONParser parser = new JSONParser();
    private static ServiceDiscovery instance;

    private ServiceDiscovery() {}

    public static ServiceDiscovery getInstance() {
        if(instance == null) instance = new ServiceDiscovery();
        return instance;
    }

    //TODO. Fix error message handling.
    public ServicesList getAvailableServices() {
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
            logger.warn(e.toString());
        } catch (IOException e) {
            logger.warn("Could not open input stream.");
            logger.warn(e.toString());
        } catch (ParseException e) {
            logger.warn("Could not parse JSON.");
            logger.warn(e.toString());
        } catch (ClassCastException e) {
            logger.warn("JSON object conversion error.");
            logger.warn(e.toString());
        }

        return new ServicesList(services);
    }

    public ServiceInfo getService(String serviceName) throws ServiceNotFoundException, ServiceDiscoveryException {

        logger.info("Trying to find service: " + serviceName);

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
            return new ServiceInfo(serviceName, serviceAddress, servicePort);

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
