package com.lorentzonsolutions.relativelysocial.userservice.service.model;

import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;

/**
 * Class representing user information for signing up.
 *
 * @author Johan Lorentzon
 * @since 2017-09-21
 *
 */
public class UserCredentials {

    private String email;
    private String firstName;
    private String lastName;
    private String family;
    private String password;

    public UserCredentials(Request request) throws UserCreationException {

        String email;
        String firstName;
        String lastName;
        String password;
        String family = "";

        try {
            JSONObject body = new JSONObject(request.body());
            try {

                email = body.getString("email");
                firstName = body.getString("firstName");
                lastName = body.getString("lastName");
                password = decryptPassword(body.getString("password"));

            } catch (JSONException e) {
                throw new UserCreationException("Missing parameters in body.");
            }

            try {
                family = body.getString("family");
            } catch (JSONException ignored) {}

            this.email = email; this.firstName = firstName; this.lastName = lastName; this.family = family; this.password = password;

        } catch (JSONException e) {
            throw new UserCreationException("Could not parse request body");
        }
    }

    // Check immutability.
    public String getEmail() {
        String copy = email;
        return copy;
    }

    public String getFirstName() {
        String copy = firstName;
        return copy;
    }

    public String getLastName() {
        String copy = lastName;
        return copy;
    }

    public String getFamily() {
        String copy = family;
        return copy;
    }

    // TODO. Method for extracting hashed password.
    private static String decryptPassword(String hashedPass) {
        return hashedPass;
    }
    // Front end sends hashed value of password using secret shared as env variable.
    // Extract value to save and compare to saved pwd in DB.
}
