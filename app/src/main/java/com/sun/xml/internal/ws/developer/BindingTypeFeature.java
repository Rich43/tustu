package com.sun.xml.internal.ws.developer;

import com.sun.org.glassfish.gmbal.ManagedAttribute;
import com.sun.org.glassfish.gmbal.ManagedData;
import javax.xml.ws.WebServiceFeature;

@ManagedData
/* loaded from: rt.jar:com/sun/xml/internal/ws/developer/BindingTypeFeature.class */
public final class BindingTypeFeature extends WebServiceFeature {
    public static final String ID = "http://jax-ws.dev.java.net/features/binding";
    private final String bindingId;

    public BindingTypeFeature(String bindingId) {
        this.bindingId = bindingId;
    }

    @Override // javax.xml.ws.WebServiceFeature
    @ManagedAttribute
    public String getID() {
        return ID;
    }

    @ManagedAttribute
    public String getBindingId() {
        return this.bindingId;
    }
}
