package com.sun.xml.internal.bind.v2.runtime;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.bind.api.TypeReference;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/BridgeAdapter.class */
final class BridgeAdapter<OnWire, InMemory> extends InternalBridge<InMemory> {
    private final InternalBridge<OnWire> core;
    private final Class<? extends XmlAdapter<OnWire, InMemory>> adapter;

    public BridgeAdapter(InternalBridge<OnWire> core, Class<? extends XmlAdapter<OnWire, InMemory>> adapter) {
        super(core.getContext());
        this.core = core;
        this.adapter = adapter;
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public void marshal(Marshaller m2, InMemory inMemory, XMLStreamWriter output) throws JAXBException {
        this.core.marshal(m2, (Marshaller) adaptM(m2, inMemory), output);
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public void marshal(Marshaller m2, InMemory inMemory, OutputStream output, NamespaceContext nsc) throws JAXBException {
        this.core.marshal(m2, (Marshaller) adaptM(m2, inMemory), output, nsc);
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public void marshal(Marshaller m2, InMemory inMemory, Node output) throws JAXBException {
        this.core.marshal(m2, (Marshaller) adaptM(m2, inMemory), output);
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public void marshal(Marshaller context, InMemory inMemory, ContentHandler contentHandler) throws JAXBException {
        this.core.marshal(context, (Marshaller) adaptM(context, inMemory), contentHandler);
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public void marshal(Marshaller context, InMemory inMemory, Result result) throws JAXBException {
        this.core.marshal(context, (Marshaller) adaptM(context, inMemory), result);
    }

    private OnWire adaptM(Marshaller m2, InMemory v2) throws JAXBException {
        XMLSerializer serializer = ((MarshallerImpl) m2).serializer;
        serializer.pushCoordinator();
        try {
            OnWire onwire_adaptM = _adaptM(serializer, v2);
            serializer.popCoordinator();
            return onwire_adaptM;
        } catch (Throwable th) {
            serializer.popCoordinator();
            throw th;
        }
    }

    private OnWire _adaptM(XMLSerializer serializer, InMemory v2) throws MarshalException {
        XmlAdapter<OnWire, InMemory> a2 = serializer.getAdapter(this.adapter);
        try {
            return a2.marshal(v2);
        } catch (Exception e2) {
            serializer.handleError(e2, v2, null);
            throw new MarshalException(e2);
        }
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    @NotNull
    public InMemory unmarshal(Unmarshaller u2, XMLStreamReader in) throws JAXBException {
        return adaptU(u2, this.core.unmarshal(u2, in));
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    @NotNull
    public InMemory unmarshal(Unmarshaller u2, Source in) throws JAXBException {
        return adaptU(u2, this.core.unmarshal(u2, in));
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    @NotNull
    public InMemory unmarshal(Unmarshaller u2, InputStream in) throws JAXBException {
        return adaptU(u2, this.core.unmarshal(u2, in));
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    @NotNull
    public InMemory unmarshal(Unmarshaller u2, Node n2) throws JAXBException {
        return adaptU(u2, this.core.unmarshal(u2, n2));
    }

    @Override // com.sun.xml.internal.bind.api.Bridge
    public TypeReference getTypeReference() {
        return this.core.getTypeReference();
    }

    @NotNull
    private InMemory adaptU(Unmarshaller _u, OnWire v2) throws JAXBException {
        UnmarshallerImpl u2 = (UnmarshallerImpl) _u;
        XmlAdapter<OnWire, InMemory> a2 = u2.coordinator.getAdapter(this.adapter);
        u2.coordinator.pushCoordinator();
        try {
            try {
                InMemory inmemoryUnmarshal = a2.unmarshal(v2);
                u2.coordinator.popCoordinator();
                return inmemoryUnmarshal;
            } catch (Exception e2) {
                throw new UnmarshalException(e2);
            }
        } catch (Throwable th) {
            u2.coordinator.popCoordinator();
            throw th;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.InternalBridge
    void marshal(InMemory o2, XMLSerializer out) throws SAXException, XMLStreamException, IOException {
        try {
            this.core.marshal((InternalBridge<OnWire>) _adaptM(XMLSerializer.getInstance(), o2), out);
        } catch (MarshalException e2) {
        }
    }
}
