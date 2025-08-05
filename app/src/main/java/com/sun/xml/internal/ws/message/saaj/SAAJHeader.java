package com.sun.xml.internal.ws.message.saaj;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.message.DOMHeader;
import javax.xml.soap.SOAPHeaderElement;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/saaj/SAAJHeader.class */
public final class SAAJHeader extends DOMHeader<SOAPHeaderElement> {
    public SAAJHeader(SOAPHeaderElement header) {
        super(header);
    }

    @Override // com.sun.xml.internal.ws.message.AbstractHeaderImpl, com.sun.xml.internal.ws.api.message.Header
    @NotNull
    public String getRole(@NotNull SOAPVersion soapVersion) {
        String v2 = getAttribute(soapVersion.nsUri, soapVersion.roleAttributeName);
        if (v2 == null || v2.equals("")) {
            v2 = soapVersion.implicitRole;
        }
        return v2;
    }
}
