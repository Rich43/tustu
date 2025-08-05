package com.sun.xml.internal.ws.api.client;

import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/client/ThrowableInPacketCompletionFeature.class */
public class ThrowableInPacketCompletionFeature extends WebServiceFeature {
    public ThrowableInPacketCompletionFeature() {
        this.enabled = true;
    }

    @Override // javax.xml.ws.WebServiceFeature
    public String getID() {
        return ThrowableInPacketCompletionFeature.class.getName();
    }
}
