package com.lorentzonsolutions.relativelysocial.apigateway.servicefinder;

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


        return services;
    }

    public String getService(String serviceName) {
        StringBuilder response = new StringBuilder();
        String serviceAddress = "";
        String servicePort = "";
        try {
            String data = readURL(ServiceDiscovery.serviceAddress + serviceName);
            JSONArray jsonArray = (JSONArray) parser.parse(data);
            for(Object o : jsonArray) {
                if(((JSONObject) o).get("ServiceName").equals(serviceName)) {
                    serviceAddress = (String) ((JSONObject) o).get("ServiceAddress");
                    servicePort = (String) ((JSONObject) o).get("ServicePort");
                }
                serviceAddress = !serviceAddress.equals("") ? "Address: " + ServiceDiscovery.serviceAddress : "Address: N/A";
                servicePort = !servicePort.equals("") ? "Port: " + servicePort : "Port: N/A";
            }
            response.append(serviceAddress); response.append("\n"); response.append(servicePort);

        } catch (IOException | ParseException e) {
            response = new StringBuilder();
            if(e instanceof ParseException) response.append("Could not parse JSON data.");
            if(e instanceof IOException) response.append("IO Exception.");
            e.printStackTrace();
        }
        return response.toString();
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
