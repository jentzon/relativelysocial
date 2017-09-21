package com.lorentzonsolutions.relativelysocial.apigateway.servicehandler;

import com.lorentzonsolutions.relativelysocial.apigateway.exceptions.ServiceUnavailableException;
import com.lorentzonsolutions.relativelysocial.apigateway.model.ServiceInfo;

import java.util.Map;

/**
 * Interface for handlers to communicate with services.
 *
 * @author Johan Lorentzon
 * @since 2017-09-20
 *
 */
public interface ServiceHandler {

    ServiceInfo getServiceInfo() throws ServiceUnavailableException;
    void connect();
    boolean isAvailable();
}
