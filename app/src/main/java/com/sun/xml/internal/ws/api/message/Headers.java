package com.sun.xml.internal.ws.api.message;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.db.glassfish.BridgeWrapper;
import com.sun.xml.internal.ws.message.DOMHeader;
import com.sun.xml.internal.ws.message.StringHeader;
import com.sun.xml.internal.ws.message.jaxb.JAXBHeader;
import com.sun.xml.internal.ws.message.saaj.SAAJHeader;
import com.sun.xml.internal.ws.message.stream.StreamHeader11;
import com.sun.xml.internal.ws.message.stream.StreamHeader12;
import com.sun.xml.internal.ws.spi.db.BindingContext;
import com.sun.xml.internal.ws.spi.db.BindingContextFactory;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/Headers.class */
public abstract class Headers {
    private Headers() {
    }

    public static Header create(SOAPVersion soapVersion, Marshaller m2, Object o2) {
        return new JAXBHeader(BindingContextFactory.getBindingContext(m2), o2);
    }

    public static Header create(JAXBContext context, Object o2) {
        return new JAXBHeader(BindingContextFactory.create(context), o2);
    }

    public static Header create(BindingContext context, Object o2) {
        return new JAXBHeader(context, o2);
    }

    public static Header create(SOAPVersion soapVersion, Marshaller m2, QName tagName, Object o2) {
        return create(soapVersion, m2, new JAXBElement(tagName, o2.getClass(), o2));
    }

    public static Header create(Bridge bridge, Object jaxbObject) {
        return new JAXBHeader(new BridgeWrapper(null, bridge), jaxbObject);
    }

    public static Header create(XMLBridge bridge, Object jaxbObject) {
        return new JAXBHeader(bridge, jaxbObject);
    }

    public static Header create(SOAPHeaderElement header) {
        return new SAAJHeader(header);
    }

    public static Header create(Element node) {
        return new DOMHeader(node);
    }

    public static Header create(SOAPVersion soapVersion, Element node) {
        return create(node);
    }

    public static Header create(SOAPVersion soapVersion, XMLStreamReader reader) throws XMLStreamException {
        switch (soapVersion) {
            case SOAP_11:
                return new StreamHeader11(reader);
            case SOAP_12:
                return new StreamHeader12(reader);
            default:
                throw new AssertionError();
        }
    }

    public static Header create(QName name, String value) {
        return new StringHeader(name, value);
    }

    public static Header createMustUnderstand(@NotNull SOAPVersion soapVersion, @NotNull QName name, @NotNull String value) {
        return new StringHeader(name, value, soapVersion, true);
    }
}
