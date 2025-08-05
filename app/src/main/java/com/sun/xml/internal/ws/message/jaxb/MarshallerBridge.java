package com.sun.xml.internal.ws.message.jaxb;

import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.bind.api.JAXBRIContext;
import com.sun.xml.internal.bind.api.TypeReference;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.MarshallerImpl;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/jaxb/MarshallerBridge.class */
final class MarshallerBridge extends Bridge {
    public MarshallerBridge(JAXBRIContext context) {
        super((JAXBContextImpl) context);
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public void marshal(Marshaller m2, Object object, XMLStreamWriter output) throws JAXBException {
        m2.setProperty(Marshaller.JAXB_FRAGMENT, true);
        try {
            m2.marshal(object, output);
            m2.setProperty(Marshaller.JAXB_FRAGMENT, false);
        } catch (Throwable th) {
            m2.setProperty(Marshaller.JAXB_FRAGMENT, false);
            throw th;
        }
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public void marshal(Marshaller m2, Object object, OutputStream output, NamespaceContext nsContext) throws JAXBException {
        m2.setProperty(Marshaller.JAXB_FRAGMENT, true);
        try {
            ((MarshallerImpl) m2).marshal(object, output, nsContext);
            m2.setProperty(Marshaller.JAXB_FRAGMENT, false);
        } catch (Throwable th) {
            m2.setProperty(Marshaller.JAXB_FRAGMENT, false);
            throw th;
        }
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public void marshal(Marshaller m2, Object object, Node output) throws JAXBException {
        m2.setProperty(Marshaller.JAXB_FRAGMENT, true);
        try {
            m2.marshal(object, output);
            m2.setProperty(Marshaller.JAXB_FRAGMENT, false);
        } catch (Throwable th) {
            m2.setProperty(Marshaller.JAXB_FRAGMENT, false);
            throw th;
        }
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public void marshal(Marshaller m2, Object object, ContentHandler contentHandler) throws JAXBException {
        m2.setProperty(Marshaller.JAXB_FRAGMENT, true);
        try {
            m2.marshal(object, contentHandler);
            m2.setProperty(Marshaller.JAXB_FRAGMENT, false);
        } catch (Throwable th) {
            m2.setProperty(Marshaller.JAXB_FRAGMENT, false);
            throw th;
        }
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public void marshal(Marshaller m2, Object object, Result result) throws JAXBException {
        m2.setProperty(Marshaller.JAXB_FRAGMENT, true);
        try {
            m2.marshal(object, result);
            m2.setProperty(Marshaller.JAXB_FRAGMENT, false);
        } catch (Throwable th) {
            m2.setProperty(Marshaller.JAXB_FRAGMENT, false);
            throw th;
        }
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public Object unmarshal(Unmarshaller u2, XMLStreamReader in) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public Object unmarshal(Unmarshaller u2, Source in) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public Object unmarshal(Unmarshaller u2, InputStream in) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public Object unmarshal(Unmarshaller u2, Node n2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public TypeReference getTypeReference() {
        throw new UnsupportedOperationException();
    }
}
