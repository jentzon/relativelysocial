package com.lorentzonsolutions.relativelysocial.authservice;

import com.lorentzonsolutions.relativelysocial.authservice.tokens.JWTGenerator;
import com.lorentzonsolutions.relativelysocial.authservice.model.Token;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * JWTGenerator test.
 *
 */
public class JWTGeneratorTest {

    private JWTGenerator jwtGenerator;

    @Test
    public void shouldAcceptCreatedToken() {
        jwtGenerator = JWTGenerator.getInstance();
        Token token = jwtGenerator.generateToken("test");

        assertTrue(jwtGenerator.validateToken(token.readToken()));
    }

    @Test
    public void shouldSetCorrectTokenValues() {

    }
}
