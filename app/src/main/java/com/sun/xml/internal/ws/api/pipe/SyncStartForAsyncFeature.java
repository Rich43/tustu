package com.sun.xml.internal.ws.api.pipe;

import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/SyncStartForAsyncFeature.class */
public class SyncStartForAsyncFeature extends WebServiceFeature {
    public SyncStartForAsyncFeature() {
        this.enabled = true;
    }

    @Override // javax.xml.ws.WebServiceFeature
    public String getID() {
        return SyncStartForAsyncFeature.class.getSimpleName();
    }
}
