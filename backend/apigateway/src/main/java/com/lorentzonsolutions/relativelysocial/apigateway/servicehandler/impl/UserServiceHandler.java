package com.lorentzonsolutions.relativelysocial.apigateway.servicehandler.impl;

import com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.models.ServiceInfo;
import com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.ServiceDiscovery;
import com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.exceptions.ServiceDiscoveryException;
import com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.exceptions.ServiceNotFoundException;
import com.lorentzonsolutions.relativelysocial.apigateway.servicehandler.ServiceHandler;
import com.lorentzonsolutions.relativelysocial.apigateway.servicehandler.ServiceHandlerFacotory;
import com.lorentzonsolutions.relativelysocial.apigateway.exceptions.ServiceUnavailableException;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.json.simple.JSONObject;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;


/**
 * Handler for user service.
 *
 * @author Johan Lorentzon
 * @since 2017-09-20
 *
 */
public class UserServiceHandler implements ServiceHandler {

    private ServiceDiscovery serviceDiscovery;
    private ServiceInfo serviceInfo;

    private final Logger logger = Log.getLogger(UserServiceHandler.class);

    private boolean available = false;

    public UserServiceHandler() {
        serviceDiscovery = ServiceDiscovery.getInstance();
        this.connect();
    }

    @Override
    public ServiceInfo getServiceInfo() throws ServiceUnavailableException {
        if(!isAvailable()) throw new ServiceUnavailableException(ServiceHandlerFacotory.USER_SERVICE + " is not available");
        return this.serviceInfo;
    }

    @Override
    public void connect() {

        try {
            serviceInfo = serviceDiscovery.getService(ServiceHandlerFacotory.USER_SERVICE);
            available = true;

        } catch (ServiceNotFoundException | ServiceDiscoveryException e) {
            available = false;
            logger.warn("Unable to reach service.");
            logger.warn(e.toString());
            e.printStackTrace();
        }

    }

    @Override
    public boolean isAvailable() {
        return available && (serviceInfo != null);
    }

    public String POSTMethod(String path, Map<String, String> bodyParams, Map<String, String> headers) {

        JSONObject body = new JSONObject();

        for (Map.Entry<String, String> entry : bodyParams.entrySet()) {
            body.put(entry.getKey(), entry.getValue());
        }


        String urlAddress = this.serviceInfo.getFullAddress() + path;

        try {
            URL url = new URL(urlAddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.setRequestMethod("POST");

            // connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Authorization", acquireAdminToken());

            // Additional headers.
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(body.toString().getBytes("UTF-8"));
            outputStream.close();

            String result = IOUtils.toString(connection.getInputStream());

            logger.info("RESPONSE: " + "\n" + result);

        } catch (MalformedURLException e) {
            logger.warn("Malformed URL: " + e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            logger.warn("IOException" + e.toString());
            e.printStackTrace();
        }

        return null;

    }

    private String acquireAdminToken() {
        return "test";
    }
}
