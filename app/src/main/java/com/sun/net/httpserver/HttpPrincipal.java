package com.sun.net.httpserver;

import java.security.Principal;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/net/httpserver/HttpPrincipal.class */
public class HttpPrincipal implements Principal {
    private String username;
    private String realm;

    public HttpPrincipal(String str, String str2) {
        if (str == null || str2 == null) {
            throw new NullPointerException();
        }
        this.username = str;
        this.realm = str2;
    }

    @Override // java.security.Principal
    public boolean equals(Object obj) {
        if (!(obj instanceof HttpPrincipal)) {
            return false;
        }
        HttpPrincipal httpPrincipal = (HttpPrincipal) obj;
        return this.username.equals(httpPrincipal.username) && this.realm.equals(httpPrincipal.realm);
    }

    @Override // java.security.Principal
    public String getName() {
        return this.username;
    }

    public String getUsername() {
        return this.username;
    }

    public String getRealm() {
        return this.realm;
    }

    @Override // java.security.Principal
    public int hashCode() {
        return (this.username + this.realm).hashCode();
    }

    @Override // java.security.Principal
    public String toString() {
        return getName();
    }
}
