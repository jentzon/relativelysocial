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
import org.json.JSONException;
import org.json.JSONObject;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

/**
 * Handler for auth service.
 *
 * @author Johan Lorentzon
 * @since 2017-09-21.
 */
public class AuthServiceHandler implements ServiceHandler {

    private ServiceDiscovery serviceDiscovery;
    private ServiceInfo serviceInfo;

    private final Logger logger = Log.getLogger(AuthServiceHandler.class);

    private boolean available = false;

    public AuthServiceHandler() {
        serviceDiscovery = ServiceDiscovery.getInstance();
        this.connect();
    }

    @Override
    public ServiceInfo getServiceInfo() throws ServiceUnavailableException {
        if (!isAvailable())
            throw new ServiceUnavailableException(ServiceHandlerFacotory.AUTH_SERVICE + " is not available");
        return this.serviceInfo;
    }

    @Override
    public void connect() {

        try {
            serviceInfo = serviceDiscovery.getService(ServiceHandlerFacotory.AUTH_SERVICE);
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

    public String acquireAdminToken() {
        logger.info("Creating admin token for request.");

        JSONObject body = new JSONObject();
        String urlAddress = this.serviceInfo.getFullAddress() + "/login";

        try {
            URL url = new URL(urlAddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/json");

            // TODO. Read from sysfile.
            body.put("user", "admin");
            body.put("pass", "admin");

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(body.toString().getBytes("UTF-8"));
            outputStream.close();

            JSONObject response = new JSONObject(IOUtils.toString(connection.getInputStream()));
            String adminToken = response.getString("token");
            logger.info("Admin token created: " + adminToken);

            return adminToken;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
