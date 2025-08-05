package com.sun.xml.internal.ws.developer;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.org.glassfish.gmbal.ManagedAttribute;
import com.sun.org.glassfish.gmbal.ManagedData;
import com.sun.xml.internal.ws.api.FeatureConstructor;
import com.sun.xml.internal.ws.server.DraconianValidationErrorHandler;
import javax.xml.ws.WebServiceFeature;

@ManagedData
/* loaded from: rt.jar:com/sun/xml/internal/ws/developer/SchemaValidationFeature.class */
public class SchemaValidationFeature extends WebServiceFeature {
    public static final String ID = "http://jax-ws.dev.java.net/features/schema-validation";
    private final Class<? extends ValidationErrorHandler> clazz;
    private final boolean inbound;
    private final boolean outbound;

    public SchemaValidationFeature() {
        this(true, true, DraconianValidationErrorHandler.class);
    }

    public SchemaValidationFeature(Class<? extends ValidationErrorHandler> clazz) {
        this(true, true, clazz);
    }

    public SchemaValidationFeature(boolean inbound, boolean outbound) {
        this(inbound, outbound, DraconianValidationErrorHandler.class);
    }

    @FeatureConstructor({"inbound", "outbound", Constants.TRANSLET_OUTPUT_PNAME})
    public SchemaValidationFeature(boolean inbound, boolean outbound, Class<? extends ValidationErrorHandler> clazz) {
        this.enabled = true;
        this.inbound = inbound;
        this.outbound = outbound;
        this.clazz = clazz;
    }

    @Override // javax.xml.ws.WebServiceFeature
    @ManagedAttribute
    public String getID() {
        return ID;
    }

    @ManagedAttribute
    public Class<? extends ValidationErrorHandler> getErrorHandler() {
        return this.clazz;
    }

    public boolean isInbound() {
        return this.inbound;
    }

    public boolean isOutbound() {
        return this.outbound;
    }
}
