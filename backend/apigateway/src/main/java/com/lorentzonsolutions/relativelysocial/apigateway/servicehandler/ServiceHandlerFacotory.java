package com.lorentzonsolutions.relativelysocial.apigateway.servicehandler;

import com.lorentzonsolutions.relativelysocial.apigateway.exceptions.ServiceNotSupportedException;
import com.lorentzonsolutions.relativelysocial.apigateway.servicehandler.impl.AuthServiceHandler;
import com.lorentzonsolutions.relativelysocial.apigateway.servicehandler.impl.RelationsServiceHandler;
import com.lorentzonsolutions.relativelysocial.apigateway.servicehandler.impl.UserServiceHandler;

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

    public ServiceHandler getServiceHandler(String serviceName) throws ServiceNotSupportedException {
        if(serviceName.equals(USER_SERVICE)) return new UserServiceHandler();
        if(serviceName.equals(AUTH_SERVICE)) return new AuthServiceHandler();
        if(serviceName.equals(RELATIONS_SERVICE)) return new RelationsServiceHandler();
        throw new ServiceNotSupportedException();
    }



}
