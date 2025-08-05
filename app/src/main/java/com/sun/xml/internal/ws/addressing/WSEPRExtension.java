package com.sun.xml.internal.ws.addressing;

import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/ws/addressing/WSEPRExtension.class */
public class WSEPRExtension extends WSEndpointReference.EPRExtension {
    XMLStreamBuffer xsb;
    final QName qname;

    public WSEPRExtension(XMLStreamBuffer xsb, QName qname) {
        this.xsb = xsb;
        this.qname = qname;
    }

    @Override // com.sun.xml.internal.ws.api.addressing.WSEndpointReference.EPRExtension
    public XMLStreamReader readAsXMLStreamReader() throws XMLStreamException {
        return this.xsb.readAsXMLStreamReader();
    }

    @Override // com.sun.xml.internal.ws.api.addressing.WSEndpointReference.EPRExtension
    public QName getQName() {
        return this.qname;
    }
}
