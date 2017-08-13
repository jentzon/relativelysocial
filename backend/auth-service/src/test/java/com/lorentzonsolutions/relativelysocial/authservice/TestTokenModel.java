package com.lorentzonsolutions.relativelysocial.authservice;

import com.lorentzonsolutions.relativelysocial.authservice.model.Token;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Testing token class.
 *
 * @author Johan Lorentzon
 * @since 2017-07-03
 */
public class TestTokenModel {

    @Test
    public void testMutability() {
        Date expiration = new Date();
        Token token = new Token.Builder().user("testuser").expiration(expiration).token("secret").build();

        String tokenUser = token.readUser();
        tokenUser = "newuser";

        String tokenValue = token.readToken();
        tokenValue = "newtoken";

        Date newExpiration = token.readExpiration();
        newExpiration = new Date();

        assertTrue(token.readUser().equals("testuser"));
        assertTrue(token.readToken().equals("secret"));
        assertTrue(token.readExpiration().equals(expiration));
    }


}
