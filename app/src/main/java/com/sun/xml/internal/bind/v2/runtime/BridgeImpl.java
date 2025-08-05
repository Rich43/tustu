package com.sun.xml.internal.bind.v2.runtime;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.bind.api.TypeReference;
import com.sun.xml.internal.bind.marshaller.SAX2DOMEx;
import com.sun.xml.internal.bind.v2.runtime.output.SAXOutput;
import com.sun.xml.internal.bind.v2.runtime.output.XMLStreamWriterOutput;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/BridgeImpl.class */
final class BridgeImpl<T> extends InternalBridge<T> {
    private final Name tagName;

    /* renamed from: bi, reason: collision with root package name */
    private final JaxBeanInfo<T> f12067bi;
    private final TypeReference typeRef;

    public BridgeImpl(JAXBContextImpl context, Name tagName, JaxBeanInfo<T> bi2, TypeReference typeRef) {
        super(context);
        this.tagName = tagName;
        this.f12067bi = bi2;
        this.typeRef = typeRef;
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public void marshal(Marshaller _m, T t2, XMLStreamWriter output) throws JAXBException {
        MarshallerImpl m2 = (MarshallerImpl) _m;
        m2.write(this.tagName, this.f12067bi, t2, XMLStreamWriterOutput.create(output, this.context, m2.getEscapeHandler()), new StAXPostInitAction(output, m2.serializer));
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public void marshal(Marshaller _m, T t2, OutputStream output, NamespaceContext nsContext) throws JAXBException {
        MarshallerImpl m2 = (MarshallerImpl) _m;
        Runnable pia = null;
        if (nsContext != null) {
            pia = new StAXPostInitAction(nsContext, m2.serializer);
        }
        m2.write(this.tagName, this.f12067bi, t2, m2.createWriter(output), pia);
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public void marshal(Marshaller _m, T t2, Node output) throws JAXBException {
        MarshallerImpl m2 = (MarshallerImpl) _m;
        m2.write(this.tagName, this.f12067bi, t2, new SAXOutput(new SAX2DOMEx(output)), new DomPostInitAction(output, m2.serializer));
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public void marshal(Marshaller _m, T t2, ContentHandler contentHandler) throws JAXBException {
        MarshallerImpl m2 = (MarshallerImpl) _m;
        m2.write(this.tagName, this.f12067bi, t2, new SAXOutput(contentHandler), null);
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public void marshal(Marshaller _m, T t2, Result result) throws JAXBException {
        MarshallerImpl m2 = (MarshallerImpl) _m;
        m2.write(this.tagName, this.f12067bi, t2, m2.createXmlOutput(result), m2.createPostInitAction(result));
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    @NotNull
    public T unmarshal(Unmarshaller unmarshaller, XMLStreamReader xMLStreamReader) throws JAXBException {
        return (T) ((JAXBElement) ((UnmarshallerImpl) unmarshaller).unmarshal0(xMLStreamReader, this.f12067bi)).getValue();
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    @NotNull
    public T unmarshal(Unmarshaller unmarshaller, Source source) throws JAXBException {
        return (T) ((JAXBElement) ((UnmarshallerImpl) unmarshaller).unmarshal0(source, this.f12067bi)).getValue();
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    @NotNull
    public T unmarshal(Unmarshaller unmarshaller, InputStream inputStream) throws JAXBException {
        return (T) ((JAXBElement) ((UnmarshallerImpl) unmarshaller).unmarshal0(inputStream, this.f12067bi)).getValue();
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    @NotNull
    public T unmarshal(Unmarshaller unmarshaller, Node node) throws JAXBException {
        return (T) ((JAXBElement) ((UnmarshallerImpl) unmarshaller).unmarshal0(node, this.f12067bi)).getValue();
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public TypeReference getTypeReference() {
        return this.typeRef;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.InternalBridge
    public void marshal(T value, XMLSerializer out) throws XMLStreamException, SAXException, IOException {
        out.startElement(this.tagName, null);
        if (value == null) {
            out.writeXsiNilTrue();
        } else {
            out.childAsXsiType(value, null, this.f12067bi, false);
        }
        out.endElement();
    }
}
