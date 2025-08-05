package com.sun.xml.internal.ws.developer;

import java.lang.reflect.Constructor;
import java.net.CookieHandler;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/developer/HttpConfigFeature.class */
public final class HttpConfigFeature extends WebServiceFeature {
    public static final String ID = "http://jax-ws.java.net/features/http-config";
    private static final Constructor cookieManagerConstructor;
    private static final Object cookiePolicy;
    private final CookieHandler cookieJar;

    static {
        Constructor tempConstructor;
        Object tempPolicy;
        try {
            Class policyClass = Class.forName("java.net.CookiePolicy");
            Class storeClass = Class.forName("java.net.CookieStore");
            tempConstructor = Class.forName("java.net.CookieManager").getConstructor(storeClass, policyClass);
            tempPolicy = policyClass.getField("ACCEPT_ALL").get(null);
        } catch (Exception e2) {
            try {
                Class policyClass2 = Class.forName("com.sun.xml.internal.ws.transport.http.client.CookiePolicy");
                Class storeClass2 = Class.forName("com.sun.xml.internal.ws.transport.http.client.CookieStore");
                tempConstructor = Class.forName("com.sun.xml.internal.ws.transport.http.client.CookieManager").getConstructor(storeClass2, policyClass2);
                tempPolicy = policyClass2.getField("ACCEPT_ALL").get(null);
            } catch (Exception ce) {
                throw new WebServiceException(ce);
            }
        }
        cookieManagerConstructor = tempConstructor;
        cookiePolicy = tempPolicy;
    }

    public HttpConfigFeature() {
        this(getInternalCookieHandler());
    }

    public HttpConfigFeature(CookieHandler cookieJar) {
        this.enabled = true;
        this.cookieJar = cookieJar;
    }

    private static CookieHandler getInternalCookieHandler() {
        try {
            return (CookieHandler) cookieManagerConstructor.newInstance(null, cookiePolicy);
        } catch (Exception e2) {
            throw new WebServiceException(e2);
        }
    }

    @Override // javax.xml.ws.WebServiceFeature
    public String getID() {
        return ID;
    }

    public CookieHandler getCookieHandler() {
        return this.cookieJar;
    }
}
