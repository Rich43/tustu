package com.oracle.webservices.internal.api;

import com.oracle.webservices.internal.api.EnvelopeStyle;
import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/oracle/webservices/internal/api/EnvelopeStyleFeature.class */
public class EnvelopeStyleFeature extends WebServiceFeature {
    private EnvelopeStyle.Style[] styles;

    public EnvelopeStyleFeature(EnvelopeStyle.Style... s2) {
        this.styles = s2;
    }

    public EnvelopeStyle.Style[] getStyles() {
        return this.styles;
    }

    @Override // javax.xml.ws.WebServiceFeature
    public String getID() {
        return EnvelopeStyleFeature.class.getName();
    }
}
