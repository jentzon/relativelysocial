package com.lorentzonsolutions.relativelysocial.userservice.service.requestvalidation;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import spark.Request;
import spark.utils.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Helper class for validating incoming requests. Requests can only be made from the API gateway service.
 *
 * @author Johan Lorentzon
 * @since 2017-09-22
 *
 */
public class RequestValidator {

    private static final String SYSTEM_PROP_FILE = "/home/configs/auth-service-address.props";
    private String authServiceAddress;

    Logger logger = Log.getLogger(RequestValidator.class);

    public RequestValidator() throws RequestValidationException {
        File propFile = new File(SYSTEM_PROP_FILE);
        try {

            BufferedReader reader = new BufferedReader(new FileReader(propFile));
            authServiceAddress = reader.readLine();

            logger.info("System prop file read. Auth service address acquired : " + authServiceAddress);

        }  catch (IOException e) {

            e.printStackTrace();

            logger.warn("Could not read system prop file.");
            throw new RequestValidationException("Could not read system prop file.");
        }
    }

    /**
     *
     * Validates the request. Requests can only be made from the APIGateway in the internal docker network. This method
     * checks the IP and port of the requests and uses them to validate the token against the APIGateways /validate path.
     * Throws RequestValidationException if the request is not made from the APIGateway service or if the gateway rejects the token.
     *
     * @param request
     * @throws RequestValidationException
     */

    public void validateRequest(Request request) throws RequestValidationException {
        if(authServiceAddress.equals("")) throw new RequestValidationException("Auth-service not available.");
        logger.info("Validating request. Connecting to auth-service...");

        String tokenValue = extractTokenValue(request.headers("Authorization"));
        logger.info("Token: " + tokenValue);

        String authURL = "http://" + authServiceAddress + "/validate";

        URL url;
        HttpURLConnection connection = null;

        try {
            url = new URL(authURL);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("Authorization", "Bearer " + tokenValue);
            connection.setRequestMethod("GET");

            connection.connect();

            int responseCode = connection.getResponseCode();

            if(responseCode == HttpStatus.ACCEPTED_202) {
                logger.info("Validation success.");
                return;
            }
            else {
                logger.info("Unauthorized request. Rejected.");
                throw new RequestValidationException("Unauthorized request");
            }


        } catch (IOException e) {
            logger.warn("Could not connect to auth-service.");
            e.printStackTrace();
            throw new RequestValidationException(e);
        }
    }

    private String extractTokenValue(String authorization) throws RequestValidationException {
        String[] values = authorization.split("\\s");
        if(values.length < 2) throw new RequestValidationException("Auth header has invalid format.");
        return values[1];
    }
}
