package com.sun.xml.internal.ws.model.soap;

import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.model.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/soap/SOAPBindingImpl.class */
public class SOAPBindingImpl extends SOAPBinding {
    public SOAPBindingImpl() {
    }

    public SOAPBindingImpl(SOAPBinding sb) {
        this.use = sb.getUse();
        this.style = sb.getStyle();
        this.soapVersion = sb.getSOAPVersion();
        this.soapAction = sb.getSOAPAction();
    }

    public void setStyle(SOAPBinding.Style style) {
        this.style = style;
    }

    public void setSOAPVersion(SOAPVersion version) {
        this.soapVersion = version;
    }

    public void setSOAPAction(String soapAction) {
        this.soapAction = soapAction;
    }
}
