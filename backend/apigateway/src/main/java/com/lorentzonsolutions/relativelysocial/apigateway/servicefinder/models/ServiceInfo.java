package com.lorentzonsolutions.relativelysocial.apigateway.servicefinder.models;

/**
 * Model object for service information.
 *
 * @author Johan Lorentzon
 * @since 2017-09-20
 *
 */

public class ServiceInfo {
    private String serviceName;
    private String serviceAddress;
    private String servicePort;

    public ServiceInfo(String serviceName, String serviceAddress, String servicePort) {
        this.serviceName = serviceName;
        this.serviceAddress = serviceAddress;
        this.servicePort = servicePort;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public String getServicePort() {
        return servicePort;
    }

    public String getFullAddress() {
        return "http://" + serviceAddress + ":" + servicePort;
    }
}
