package com.sun.xml.internal.ws.db.glassfish;

import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.bind.api.CompositeStructure;
import com.sun.xml.internal.ws.spi.db.BindingContext;
import com.sun.xml.internal.ws.spi.db.TypeInfo;
import com.sun.xml.internal.ws.spi.db.WrapperComposite;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBException;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.bind.attachment.AttachmentUnmarshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;

/* loaded from: rt.jar:com/sun/xml/internal/ws/db/glassfish/WrapperBridge.class */
public class WrapperBridge<T> implements XMLBridge<T> {
    private JAXBRIContextWrapper parent;
    private Bridge<T> bridge;

    public WrapperBridge(JAXBRIContextWrapper p2, Bridge<T> b2) {
        this.parent = p2;
        this.bridge = b2;
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public BindingContext context() {
        return this.parent;
    }

    public boolean equals(Object obj) {
        return this.bridge.equals(obj);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public TypeInfo getTypeInfo() {
        return this.parent.typeInfo(this.bridge.getTypeReference());
    }

    public int hashCode() {
        return this.bridge.hashCode();
    }

    static CompositeStructure convert(Object o2) {
        WrapperComposite w2 = (WrapperComposite) o2;
        CompositeStructure cs = new CompositeStructure();
        cs.values = w2.values;
        cs.bridges = new Bridge[w2.bridges.length];
        for (int i2 = 0; i2 < cs.bridges.length; i2++) {
            cs.bridges[i2] = ((BridgeWrapper) w2.bridges[i2]).getBridge();
        }
        return cs;
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public final void marshal(T t2, ContentHandler contentHandler, AttachmentMarshaller attachmentMarshaller) throws JAXBException {
        this.bridge.marshal((Bridge<T>) convert(t2), contentHandler, attachmentMarshaller);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public void marshal(T object, Node output) throws JAXBException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public void marshal(T t2, OutputStream outputStream, NamespaceContext namespaceContext, AttachmentMarshaller attachmentMarshaller) throws JAXBException {
        this.bridge.marshal((Bridge<T>) convert(t2), outputStream, namespaceContext, attachmentMarshaller);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public final void marshal(T object, Result result) throws JAXBException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public final void marshal(T t2, XMLStreamWriter xMLStreamWriter, AttachmentMarshaller attachmentMarshaller) throws JAXBException {
        this.bridge.marshal((Bridge<T>) convert(t2), xMLStreamWriter, attachmentMarshaller);
    }

    public String toString() {
        return BridgeWrapper.class.getName() + " : " + this.bridge.toString();
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public final T unmarshal(InputStream in) throws JAXBException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public final T unmarshal(Node n2, AttachmentUnmarshaller au2) throws JAXBException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public final T unmarshal(Source in, AttachmentUnmarshaller au2) throws JAXBException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public final T unmarshal(XMLStreamReader in, AttachmentUnmarshaller au2) throws JAXBException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public boolean supportOutputStream() {
        return true;
    }
}
