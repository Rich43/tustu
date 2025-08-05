package com.sun.xml.internal.ws.developer;

import com.sun.xml.internal.ws.api.FeatureConstructor;
import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/developer/SerializationFeature.class */
public class SerializationFeature extends WebServiceFeature {
    public static final String ID = "http://jax-ws.java.net/features/serialization";
    private final String encoding;

    public SerializationFeature() {
        this("");
    }

    @FeatureConstructor({"encoding"})
    public SerializationFeature(String encoding) {
        this.encoding = encoding;
    }

    @Override // javax.xml.ws.WebServiceFeature
    public String getID() {
        return ID;
    }

    public String getEncoding() {
        return this.encoding;
    }
}
