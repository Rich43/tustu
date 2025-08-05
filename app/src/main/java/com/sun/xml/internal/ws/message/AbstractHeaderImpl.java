package com.sun.xml.internal.ws.message;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.bind.api.BridgeContext;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.message.Header;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import java.util.Set;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.attachment.AttachmentUnmarshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.xml.sax.helpers.AttributesImpl;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/AbstractHeaderImpl.class */
public abstract class AbstractHeaderImpl implements Header {
    protected static final AttributesImpl EMPTY_ATTS = new AttributesImpl();

    protected AbstractHeaderImpl() {
    }

    public final <T> T readAsJAXB(Bridge<T> bridge, BridgeContext bridgeContext) throws JAXBException {
        return (T) readAsJAXB(bridge);
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public <T> T readAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
        try {
            return (T) unmarshaller.unmarshal(readHeader());
        } catch (Exception e2) {
            throw new JAXBException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public <T> T readAsJAXB(Bridge<T> bridge) throws JAXBException {
        try {
            return bridge.unmarshal(readHeader());
        } catch (XMLStreamException e2) {
            throw new JAXBException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public <T> T readAsJAXB(XMLBridge<T> bridge) throws JAXBException {
        try {
            return bridge.unmarshal(readHeader(), (AttachmentUnmarshaller) null);
        } catch (XMLStreamException e2) {
            throw new JAXBException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public WSEndpointReference readAsEPR(AddressingVersion expected) throws XMLStreamException {
        XMLStreamReader xsr = readHeader();
        WSEndpointReference epr = new WSEndpointReference(xsr, expected);
        XMLStreamReaderFactory.recycle(xsr);
        return epr;
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public boolean isIgnorable(@NotNull SOAPVersion soapVersion, @NotNull Set<String> roles) {
        String v2 = getAttribute(soapVersion.nsUri, "mustUnderstand");
        return v2 == null || !parseBool(v2) || roles == null || !roles.contains(getRole(soapVersion));
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    @NotNull
    public String getRole(@NotNull SOAPVersion soapVersion) {
        String v2 = getAttribute(soapVersion.nsUri, soapVersion.roleAttributeName);
        if (v2 == null) {
            v2 = soapVersion.implicitRole;
        }
        return v2;
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public boolean isRelay() {
        String v2 = getAttribute(SOAPVersion.SOAP_12.nsUri, "relay");
        if (v2 == null) {
            return false;
        }
        return parseBool(v2);
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public String getAttribute(QName name) {
        return getAttribute(name.getNamespaceURI(), name.getLocalPart());
    }

    protected final boolean parseBool(String value) {
        if (value.length() == 0) {
            return false;
        }
        char ch = value.charAt(0);
        return ch == 't' || ch == '1';
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public String getStringContent() {
        try {
            XMLStreamReader xsr = readHeader();
            xsr.nextTag();
            return xsr.getElementText();
        } catch (XMLStreamException e2) {
            return null;
        }
    }
}
