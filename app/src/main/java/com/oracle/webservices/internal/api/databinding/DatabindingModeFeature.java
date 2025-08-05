package com.oracle.webservices.internal.api.databinding;

import com.sun.xml.internal.ws.api.ServiceSharedFeatureMarker;
import java.util.HashMap;
import java.util.Map;
import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/oracle/webservices/internal/api/databinding/DatabindingModeFeature.class */
public class DatabindingModeFeature extends WebServiceFeature implements ServiceSharedFeatureMarker {
    public static final String ID = "http://jax-ws.java.net/features/databinding";
    public static final String GLASSFISH_JAXB = "glassfish.jaxb";
    private String mode;
    private Map<String, Object> properties = new HashMap();

    public DatabindingModeFeature(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return this.mode;
    }

    @Override // javax.xml.ws.WebServiceFeature
    public String getID() {
        return ID;
    }

    public Map<String, Object> getProperties() {
        return this.properties;
    }

    public static Builder builder() {
        return new Builder(new DatabindingModeFeature(null));
    }

    /* loaded from: rt.jar:com/oracle/webservices/internal/api/databinding/DatabindingModeFeature$Builder.class */
    public static final class Builder {

        /* renamed from: o, reason: collision with root package name */
        private final DatabindingModeFeature f11798o;

        Builder(DatabindingModeFeature x2) {
            this.f11798o = x2;
        }

        public DatabindingModeFeature build() {
            return this.f11798o;
        }

        public Builder value(String x2) {
            this.f11798o.mode = x2;
            return this;
        }
    }
}
