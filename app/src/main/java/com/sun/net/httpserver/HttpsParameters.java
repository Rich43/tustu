package com.sun.net.httpserver;

import java.net.InetSocketAddress;
import javax.net.ssl.SSLParameters;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/net/httpserver/HttpsParameters.class */
public abstract class HttpsParameters {
    private String[] cipherSuites;
    private String[] protocols;
    private boolean wantClientAuth;
    private boolean needClientAuth;

    public abstract HttpsConfigurator getHttpsConfigurator();

    public abstract InetSocketAddress getClientAddress();

    public abstract void setSSLParameters(SSLParameters sSLParameters);

    protected HttpsParameters() {
    }

    public String[] getCipherSuites() {
        if (this.cipherSuites != null) {
            return (String[]) this.cipherSuites.clone();
        }
        return null;
    }

    public void setCipherSuites(String[] strArr) {
        this.cipherSuites = strArr != null ? (String[]) strArr.clone() : null;
    }

    public String[] getProtocols() {
        if (this.protocols != null) {
            return (String[]) this.protocols.clone();
        }
        return null;
    }

    public void setProtocols(String[] strArr) {
        this.protocols = strArr != null ? (String[]) strArr.clone() : null;
    }

    public boolean getWantClientAuth() {
        return this.wantClientAuth;
    }

    public void setWantClientAuth(boolean z2) {
        this.wantClientAuth = z2;
    }

    public boolean getNeedClientAuth() {
        return this.needClientAuth;
    }

    public void setNeedClientAuth(boolean z2) {
        this.needClientAuth = z2;
    }
}
