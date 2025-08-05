package com.sun.xml.internal.ws.runtime.config;

import com.sun.org.glassfish.gmbal.ManagedAttribute;
import com.sun.org.glassfish.gmbal.ManagedData;
import com.sun.xml.internal.ws.api.FeatureConstructor;
import java.util.List;
import javax.xml.ws.WebServiceFeature;
import jdk.jfr.Enabled;

@ManagedData
/* loaded from: rt.jar:com/sun/xml/internal/ws/runtime/config/TubelineFeature.class */
public class TubelineFeature extends WebServiceFeature {
    public static final String ID = "com.sun.xml.internal.ws.runtime.config.TubelineFeature";

    @FeatureConstructor({Enabled.NAME})
    public TubelineFeature(boolean enabled) {
        this.enabled = enabled;
    }

    @Override // javax.xml.ws.WebServiceFeature
    @ManagedAttribute
    public String getID() {
        return ID;
    }

    List<String> getTubeFactories() {
        return null;
    }
}
