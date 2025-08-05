package com.sun.net.httpserver;

import javax.net.ssl.SSLContext;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/net/httpserver/HttpsConfigurator.class */
public class HttpsConfigurator {
    private SSLContext context;

    public HttpsConfigurator(SSLContext sSLContext) {
        if (sSLContext == null) {
            throw new NullPointerException("null SSLContext");
        }
        this.context = sSLContext;
    }

    public SSLContext getSSLContext() {
        return this.context;
    }

    public void configure(HttpsParameters httpsParameters) {
        httpsParameters.setSSLParameters(getSSLContext().getDefaultSSLParameters());
    }
}
