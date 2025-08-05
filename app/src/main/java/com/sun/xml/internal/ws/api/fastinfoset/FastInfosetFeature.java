package com.sun.xml.internal.ws.api.fastinfoset;

import com.sun.org.glassfish.gmbal.ManagedAttribute;
import com.sun.org.glassfish.gmbal.ManagedData;
import com.sun.xml.internal.ws.api.FeatureConstructor;
import javax.xml.ws.WebServiceFeature;
import jdk.jfr.Enabled;

@ManagedData
/* loaded from: rt.jar:com/sun/xml/internal/ws/api/fastinfoset/FastInfosetFeature.class */
public class FastInfosetFeature extends WebServiceFeature {
    public static final String ID = "http://java.sun.com/xml/ns/jaxws/fastinfoset";

    public FastInfosetFeature() {
        this.enabled = true;
    }

    @FeatureConstructor({Enabled.NAME})
    public FastInfosetFeature(boolean enabled) {
        this.enabled = enabled;
    }

    @Override // javax.xml.ws.WebServiceFeature
    @ManagedAttribute
    public String getID() {
        return ID;
    }
}
