package com.lorentzonsolutions.relativelysocial.apigateway.servicehandler.impl;

import com.lorentzonsolutions.relativelysocial.apigateway.model.ServiceInfo;
import com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.ServiceDiscovery;
import com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.ServiceDiscoveryException;
import com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.ServiceNotFoundException;
import com.lorentzonsolutions.relativelysocial.apigateway.servicehandler.ServiceHandler;
import com.lorentzonsolutions.relativelysocial.apigateway.servicehandler.ServiceHandlerFacotory;
import com.lorentzonsolutions.relativelysocial.apigateway.servicehandler.ServiceUnavailableException;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;


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
}
