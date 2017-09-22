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

/**
 * Handler for relations service.
 *
 * @author Johan Lorentzon
 * @since 2017-09-21
 *
 */

public class RelationsServiceHandler implements ServiceHandler {

    private ServiceDiscovery serviceDiscovery;
    private ServiceInfo serviceInfo;

    private final Logger logger = Log.getLogger(UserServiceHandler.class);

    private boolean available = false;

    public RelationsServiceHandler() {
        serviceDiscovery = ServiceDiscovery.getInstance();
        this.connect();
    }

    @Override
    public ServiceInfo getServiceInfo() throws ServiceUnavailableException {
        if(!isAvailable()) throw new ServiceUnavailableException(ServiceHandlerFacotory.RELATIONS_SERVICE + " is not available");
        return this.serviceInfo;
    }

    @Override
    public void connect() {

        try {
            serviceInfo = serviceDiscovery.getService(ServiceHandlerFacotory.RELATIONS_SERVICE);
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
