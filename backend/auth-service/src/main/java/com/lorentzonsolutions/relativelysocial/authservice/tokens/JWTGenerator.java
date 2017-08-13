package com.lorentzonsolutions.relativelysocial.authservice.tokens;

import com.lorentzonsolutions.relativelysocial.authservice.model.Token;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;
import java.util.Date;

/**
 * Handles token creation and validation for the API.
 *
 * @author Johan Lorentzon
 * @since 2017-07-03
 */
public class JWTGenerator {

    private JWTGenerator() {}

    private static JWTGenerator instance;

    public static JWTGenerator getInstance() {
        if(instance == null) instance = new JWTGenerator();
        return instance;
    }

    // TODO. Put the key in the application configuration.
    private final Key key = MacProvider.generateKey();

    private final long timeValid = 3600000;

    public Token generateToken(String user) {
        Date expiration = new Date(System.currentTimeMillis() + timeValid);

        String tokenValue = Jwts.builder()
                .setSubject(user)
                .signWith(SignatureAlgorithm.HS512, key)
                .setExpiration(expiration)
                .compact();

        System.out.println("Token created for user: " + user);

        return new Token.Builder().expiration(expiration).user(user).token(tokenValue).build();
    }

    public boolean validateToken(String tokenToTry) {

        System.out.println("Validating token...");

        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(tokenToTry);
        }
        catch (ExpiredJwtException expired) {
            System.out.println("Token has expired!");
            return false;
        }
        catch(SignatureException se) {
            System.out.println("Not a valid token signature.");
            return false;
        }
        catch(Exception se) {
            System.out.println("Token validation failed.");
            return false;
        }
        return true;
    }

}
