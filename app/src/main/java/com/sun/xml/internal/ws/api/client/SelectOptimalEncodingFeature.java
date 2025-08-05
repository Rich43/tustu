package com.sun.xml.internal.ws.api.client;

import com.sun.org.glassfish.gmbal.ManagedAttribute;
import com.sun.org.glassfish.gmbal.ManagedData;
import com.sun.xml.internal.ws.api.FeatureConstructor;
import javax.xml.ws.WebServiceFeature;
import jdk.jfr.Enabled;

@ManagedData
/* loaded from: rt.jar:com/sun/xml/internal/ws/api/client/SelectOptimalEncodingFeature.class */
public class SelectOptimalEncodingFeature extends WebServiceFeature {
    public static final String ID = "http://java.sun.com/xml/ns/jaxws/client/selectOptimalEncoding";

    public SelectOptimalEncodingFeature() {
        this.enabled = true;
    }

    @FeatureConstructor({Enabled.NAME})
    public SelectOptimalEncodingFeature(boolean enabled) {
        this.enabled = enabled;
    }

    @Override // javax.xml.ws.WebServiceFeature
    @ManagedAttribute
    public String getID() {
        return ID;
    }
}
