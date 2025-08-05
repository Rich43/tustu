package com.sun.xml.internal.ws.api.message;

import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/SuppressAutomaticWSARequestHeadersFeature.class */
public class SuppressAutomaticWSARequestHeadersFeature extends WebServiceFeature {
    public SuppressAutomaticWSARequestHeadersFeature() {
        this.enabled = true;
    }

    @Override // javax.xml.ws.WebServiceFeature
    public String getID() {
        return SuppressAutomaticWSARequestHeadersFeature.class.toString();
    }
}
