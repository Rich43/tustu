package com.sun.net.httpserver;

import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/net/httpserver/Authenticator.class */
public abstract class Authenticator {

    /* loaded from: rt.jar:com/sun/net/httpserver/Authenticator$Result.class */
    public static abstract class Result {
    }

    public abstract Result authenticate(HttpExchange httpExchange);

    @Exported
    /* loaded from: rt.jar:com/sun/net/httpserver/Authenticator$Failure.class */
    public static class Failure extends Result {
        private int responseCode;

        public Failure(int i2) {
            this.responseCode = i2;
        }

        public int getResponseCode() {
            return this.responseCode;
        }
    }

    @Exported
    /* loaded from: rt.jar:com/sun/net/httpserver/Authenticator$Success.class */
    public static class Success extends Result {
        private HttpPrincipal principal;

        public Success(HttpPrincipal httpPrincipal) {
            this.principal = httpPrincipal;
        }

        public HttpPrincipal getPrincipal() {
            return this.principal;
        }
    }

    @Exported
    /* loaded from: rt.jar:com/sun/net/httpserver/Authenticator$Retry.class */
    public static class Retry extends Result {
        private int responseCode;

        public Retry(int i2) {
            this.responseCode = i2;
        }

        public int getResponseCode() {
            return this.responseCode;
        }
    }
}
