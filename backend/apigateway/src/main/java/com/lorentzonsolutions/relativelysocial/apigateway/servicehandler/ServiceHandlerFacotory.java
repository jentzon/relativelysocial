package com.lorentzonsolutions.relativelysocial.apigateway.servicehandler;

/**
 *
 * Factory for producing handlers to talk to services. Uses service discovery to find where services are located.
 *
 * @author Johan Lorentzon
 * @since 2017-09-20
 *
 */
public class ServiceHandlerFacotory {

    public static final String USER_SERVICE = "user-service";
    public static final String AUTH_SERVICE = "auth-service";
    public static final String FAMILY_SERVICE = "family-service";
    public static final String RELATIONS_SERVICE = "relations-service";

    public ServiceHandler getServiceHandler(String serviceName) throws ServiceNotSupportedException{
        if(serviceName.equals(USER_SERVICE)) return new UserServiceHandler();
        throw new ServiceNotSupportedException();
    }



}
