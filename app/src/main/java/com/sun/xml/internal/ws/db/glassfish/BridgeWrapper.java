package com.sun.xml.internal.ws.db.glassfish;

import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.bind.api.JAXBRIContext;
import com.sun.xml.internal.ws.spi.db.BindingContext;
import com.sun.xml.internal.ws.spi.db.DatabindingException;
import com.sun.xml.internal.ws.spi.db.TypeInfo;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.bind.attachment.AttachmentUnmarshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;

/* loaded from: rt.jar:com/sun/xml/internal/ws/db/glassfish/BridgeWrapper.class */
public class BridgeWrapper<T> implements XMLBridge<T> {
    private JAXBRIContextWrapper parent;
    private Bridge<T> bridge;

    public BridgeWrapper(JAXBRIContextWrapper p2, Bridge<T> b2) {
        this.parent = p2;
        this.bridge = b2;
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public BindingContext context() {
        return this.parent;
    }

    Bridge getBridge() {
        return this.bridge;
    }

    public boolean equals(Object obj) {
        return this.bridge.equals(obj);
    }

    public JAXBRIContext getContext() {
        return this.bridge.getContext();
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public TypeInfo getTypeInfo() {
        return this.parent.typeInfo(this.bridge.getTypeReference());
    }

    public int hashCode() {
        return this.bridge.hashCode();
    }

    public void marshal(Marshaller m2, T object, ContentHandler contentHandler) throws JAXBException {
        this.bridge.marshal(m2, (Marshaller) object, contentHandler);
    }

    public void marshal(Marshaller m2, T object, Node output) throws JAXBException {
        this.bridge.marshal(m2, (Marshaller) object, output);
    }

    public void marshal(Marshaller m2, T object, OutputStream output, NamespaceContext nsContext) throws JAXBException {
        this.bridge.marshal(m2, (Marshaller) object, output, nsContext);
    }

    public void marshal(Marshaller m2, T object, Result result) throws JAXBException {
        this.bridge.marshal(m2, (Marshaller) object, result);
    }

    public void marshal(Marshaller m2, T object, XMLStreamWriter output) throws JAXBException {
        this.bridge.marshal(m2, (Marshaller) object, output);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public final void marshal(T object, ContentHandler contentHandler, AttachmentMarshaller am2) throws JAXBException {
        this.bridge.marshal((Bridge<T>) object, contentHandler, am2);
    }

    public void marshal(T object, ContentHandler contentHandler) throws JAXBException {
        this.bridge.marshal((Bridge<T>) object, contentHandler);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public void marshal(T object, Node output) throws JAXBException {
        this.bridge.marshal((Bridge<T>) object, output);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public void marshal(T object, OutputStream output, NamespaceContext nsContext, AttachmentMarshaller am2) throws JAXBException {
        this.bridge.marshal((Bridge<T>) object, output, nsContext, am2);
    }

    public void marshal(T object, OutputStream output, NamespaceContext nsContext) throws JAXBException {
        this.bridge.marshal((Bridge<T>) object, output, nsContext);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public final void marshal(T object, Result result) throws JAXBException {
        this.bridge.marshal((Bridge<T>) object, result);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public final void marshal(T object, XMLStreamWriter output, AttachmentMarshaller am2) throws JAXBException {
        this.bridge.marshal((Bridge<T>) object, output, am2);
    }

    public final void marshal(T object, XMLStreamWriter output) throws JAXBException {
        this.bridge.marshal((Bridge<T>) object, output);
    }

    public String toString() {
        return BridgeWrapper.class.getName() + " : " + this.bridge.toString();
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public final T unmarshal(InputStream in) throws JAXBException {
        return this.bridge.unmarshal(in);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public final T unmarshal(Node n2, AttachmentUnmarshaller au2) throws JAXBException {
        return this.bridge.unmarshal(n2, au2);
    }

    public final T unmarshal(Node n2) throws JAXBException {
        return this.bridge.unmarshal(n2);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public final T unmarshal(Source in, AttachmentUnmarshaller au2) throws JAXBException {
        return this.bridge.unmarshal(in, au2);
    }

    public final T unmarshal(Source in) throws DatabindingException {
        try {
            return this.bridge.unmarshal(in);
        } catch (JAXBException e2) {
            throw new DatabindingException(e2);
        }
    }

    public T unmarshal(Unmarshaller u2, InputStream in) throws JAXBException {
        return this.bridge.unmarshal(u2, in);
    }

    public T unmarshal(Unmarshaller context, Node n2) throws JAXBException {
        return this.bridge.unmarshal(context, n2);
    }

    public T unmarshal(Unmarshaller u2, Source in) throws JAXBException {
        return this.bridge.unmarshal(u2, in);
    }

    public T unmarshal(Unmarshaller u2, XMLStreamReader in) throws JAXBException {
        return this.bridge.unmarshal(u2, in);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public final T unmarshal(XMLStreamReader in, AttachmentUnmarshaller au2) throws JAXBException {
        return this.bridge.unmarshal(in, au2);
    }

    public final T unmarshal(XMLStreamReader in) throws JAXBException {
        return this.bridge.unmarshal(in);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public boolean supportOutputStream() {
        return true;
    }
}
