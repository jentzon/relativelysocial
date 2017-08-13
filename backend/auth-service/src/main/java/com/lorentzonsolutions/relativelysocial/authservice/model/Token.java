package com.lorentzonsolutions.relativelysocial.authservice.model;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Token model object to store Token information.
 *
 * @author Johan Lorentzon
 * @since 2017-07-03
 */
public class Token {

    private Token() {}

    private String user;
    private String token;
    private Date expiration;

    public String readUser() {
        return user;
    }
    public String readToken() {
        return token;
    }
    public Date readExpiration() {
        return new Date(expiration.getTime());
    }

    public static class Builder {
        private String user;
        private String token;
        private Date expiration;

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder expiration(Date expiration) {
            this.expiration = expiration;
            return this;
        }

        public Token build() {
            Token token = new Token();
            token.user = this.user != null ? this.user : "";
            token.token = this.token != null ? this.token : "";
            token.expiration = this.expiration != null ? this.expiration : new Date();
            return token;
        }

    }

    @Override
    public String toString() {
        return "{\n"
                + "user: " + user +
                ",\ntoken: " + token +
                ",\nexpiration: " + expiration +
                "\n}";
    }
}
