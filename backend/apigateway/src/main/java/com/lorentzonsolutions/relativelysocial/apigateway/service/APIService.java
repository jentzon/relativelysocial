package com.lorentzonsolutions.relativelysocial.apigateway.service;

import com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.ServiceDiscovery;
import com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.ServiceDiscoveryException;
import com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.ServiceNotFoundException;

/**
 * Service for APIController class.
 *
 * @author Johan Lorentzon
 * @since 2017-09-20
 *
 */
public class APIService {

    public Object listservices() {
        return ServiceDiscovery.getInstance().getAvailableServices();
    }

    public Object findservice(String serviceName) throws ServiceNotFoundException, ServiceDiscoveryException {
        return ServiceDiscovery.getInstance().getService(serviceName);
    }
}
