package com.sun.xml.internal.ws.spi.db;

import com.sun.xml.internal.ws.db.glassfish.BridgeWrapper;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import javax.xml.bind.JAXBException;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.bind.attachment.AttachmentUnmarshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/WrapperBridge.class */
public class WrapperBridge<T> implements XMLBridge<T> {
    BindingContext parent;
    TypeInfo typeInfo;
    static final String WrapperPrefix = "w";
    static final String WrapperPrefixColon = "w:";

    public WrapperBridge(BindingContext p2, TypeInfo ti) {
        this.parent = p2;
        this.typeInfo = ti;
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public BindingContext context() {
        return this.parent;
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public TypeInfo getTypeInfo() {
        return this.typeInfo;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public final void marshal(T t2, ContentHandler contentHandler, AttachmentMarshaller am2) throws JAXBException {
        WrapperComposite w2 = (WrapperComposite) t2;
        Attributes att = new Attributes() { // from class: com.sun.xml.internal.ws.spi.db.WrapperBridge.1
            @Override // org.xml.sax.Attributes
            public int getLength() {
                return 0;
            }

            @Override // org.xml.sax.Attributes
            public String getURI(int index) {
                return null;
            }

            @Override // org.xml.sax.Attributes
            public String getLocalName(int index) {
                return null;
            }

            @Override // org.xml.sax.Attributes
            public String getQName(int index) {
                return null;
            }

            @Override // org.xml.sax.Attributes
            public String getType(int index) {
                return null;
            }

            @Override // org.xml.sax.Attributes
            public String getValue(int index) {
                return null;
            }

            @Override // org.xml.sax.Attributes
            public int getIndex(String uri, String localName) {
                return 0;
            }

            @Override // org.xml.sax.Attributes
            public int getIndex(String qName) {
                return 0;
            }

            @Override // org.xml.sax.Attributes
            public String getType(String uri, String localName) {
                return null;
            }

            @Override // org.xml.sax.Attributes
            public String getType(String qName) {
                return null;
            }

            @Override // org.xml.sax.Attributes
            public String getValue(String uri, String localName) {
                return null;
            }

            @Override // org.xml.sax.Attributes
            public String getValue(String qName) {
                return null;
            }
        };
        try {
            contentHandler.startPrefixMapping("w", this.typeInfo.tagName.getNamespaceURI());
            contentHandler.startElement(this.typeInfo.tagName.getNamespaceURI(), this.typeInfo.tagName.getLocalPart(), WrapperPrefixColon + this.typeInfo.tagName.getLocalPart(), att);
            if (w2.bridges != null) {
                for (int i2 = 0; i2 < w2.bridges.length; i2++) {
                    if (w2.bridges[i2] instanceof RepeatedElementBridge) {
                        RepeatedElementBridge repeatedElementBridge = (RepeatedElementBridge) w2.bridges[i2];
                        Iterator itr = repeatedElementBridge.collectionHandler().iterator(w2.values[i2]);
                        while (itr.hasNext()) {
                            repeatedElementBridge.marshal((RepeatedElementBridge) itr.next(), contentHandler, am2);
                        }
                    } else {
                        w2.bridges[i2].marshal((BridgeWrapper) w2.values[i2], contentHandler, am2);
                    }
                }
            }
            try {
                contentHandler.endElement(this.typeInfo.tagName.getNamespaceURI(), this.typeInfo.tagName.getLocalPart(), null);
                contentHandler.endPrefixMapping("w");
            } catch (SAXException e2) {
                throw new JAXBException(e2);
            }
        } catch (SAXException e3) {
            throw new JAXBException(e3);
        }
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public void marshal(T object, Node output) throws JAXBException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public void marshal(T object, OutputStream output, NamespaceContext nsContext, AttachmentMarshaller am2) throws JAXBException {
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public final void marshal(T object, Result result) throws JAXBException {
        throw new UnsupportedOperationException();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public final void marshal(T t2, XMLStreamWriter output, AttachmentMarshaller am2) throws JAXBException {
        WrapperComposite w2 = (WrapperComposite) t2;
        try {
            String prefix = output.getPrefix(this.typeInfo.tagName.getNamespaceURI());
            if (prefix == null) {
                prefix = "w";
            }
            output.writeStartElement(prefix, this.typeInfo.tagName.getLocalPart(), this.typeInfo.tagName.getNamespaceURI());
            output.writeNamespace(prefix, this.typeInfo.tagName.getNamespaceURI());
            if (w2.bridges != null) {
                for (int i2 = 0; i2 < w2.bridges.length; i2++) {
                    if (w2.bridges[i2] instanceof RepeatedElementBridge) {
                        RepeatedElementBridge repeatedElementBridge = (RepeatedElementBridge) w2.bridges[i2];
                        Iterator itr = repeatedElementBridge.collectionHandler().iterator(w2.values[i2]);
                        while (itr.hasNext()) {
                            repeatedElementBridge.marshal((RepeatedElementBridge) itr.next(), output, am2);
                        }
                    } else {
                        w2.bridges[i2].marshal((BridgeWrapper) w2.values[i2], output, am2);
                    }
                }
            }
            try {
                output.writeEndElement();
            } catch (XMLStreamException e2) {
                throw new DatabindingException(e2);
            }
        } catch (XMLStreamException e3) {
            e3.printStackTrace();
            throw new DatabindingException(e3);
        }
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
        return false;
    }
}
