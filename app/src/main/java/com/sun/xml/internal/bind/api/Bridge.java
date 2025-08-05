package com.sun.xml.internal.bind.api;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.bind.v2.runtime.BridgeContextImpl;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.MarshallerImpl;
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

/* loaded from: rt.jar:com/sun/xml/internal/bind/api/Bridge.class */
public abstract class Bridge<T> {
    protected final JAXBContextImpl context;

    public abstract void marshal(@NotNull Marshaller marshaller, T t2, XMLStreamWriter xMLStreamWriter) throws JAXBException;

    public abstract void marshal(@NotNull Marshaller marshaller, T t2, OutputStream outputStream, NamespaceContext namespaceContext) throws JAXBException;

    public abstract void marshal(@NotNull Marshaller marshaller, T t2, Node node) throws JAXBException;

    public abstract void marshal(@NotNull Marshaller marshaller, T t2, ContentHandler contentHandler) throws JAXBException;

    public abstract void marshal(@NotNull Marshaller marshaller, T t2, Result result) throws JAXBException;

    @NotNull
    public abstract T unmarshal(@NotNull Unmarshaller unmarshaller, @NotNull XMLStreamReader xMLStreamReader) throws JAXBException;

    @NotNull
    public abstract T unmarshal(@NotNull Unmarshaller unmarshaller, @NotNull Source source) throws JAXBException;

    @NotNull
    public abstract T unmarshal(@NotNull Unmarshaller unmarshaller, @NotNull InputStream inputStream) throws JAXBException;

    @NotNull
    public abstract T unmarshal(@NotNull Unmarshaller unmarshaller, @NotNull Node node) throws JAXBException;

    public abstract TypeReference getTypeReference();

    protected Bridge(JAXBContextImpl context) {
        this.context = context;
    }

    @NotNull
    public JAXBRIContext getContext() {
        return this.context;
    }

    public final void marshal(T object, XMLStreamWriter output) throws JAXBException {
        marshal((Bridge<T>) object, output, (AttachmentMarshaller) null);
    }

    public final void marshal(T object, XMLStreamWriter output, AttachmentMarshaller am2) throws JAXBException {
        Marshaller m2 = this.context.marshallerPool.take();
        m2.setAttachmentMarshaller(am2);
        marshal(m2, (Marshaller) object, output);
        m2.setAttachmentMarshaller(null);
        this.context.marshallerPool.recycle(m2);
    }

    public final void marshal(@NotNull BridgeContext context, T object, XMLStreamWriter output) throws JAXBException {
        marshal((Marshaller) ((BridgeContextImpl) context).marshaller, (MarshallerImpl) object, output);
    }

    public void marshal(T object, OutputStream output, NamespaceContext nsContext) throws JAXBException {
        marshal((Bridge<T>) object, output, nsContext, (AttachmentMarshaller) null);
    }

    public void marshal(T object, OutputStream output, NamespaceContext nsContext, AttachmentMarshaller am2) throws JAXBException {
        Marshaller m2 = this.context.marshallerPool.take();
        m2.setAttachmentMarshaller(am2);
        marshal(m2, (Marshaller) object, output, nsContext);
        m2.setAttachmentMarshaller(null);
        this.context.marshallerPool.recycle(m2);
    }

    public final void marshal(@NotNull BridgeContext context, T object, OutputStream output, NamespaceContext nsContext) throws JAXBException {
        marshal((Marshaller) ((BridgeContextImpl) context).marshaller, (MarshallerImpl) object, output, nsContext);
    }

    public final void marshal(T object, Node output) throws JAXBException {
        Marshaller m2 = this.context.marshallerPool.take();
        marshal(m2, (Marshaller) object, output);
        this.context.marshallerPool.recycle(m2);
    }

    public final void marshal(@NotNull BridgeContext context, T object, Node output) throws JAXBException {
        marshal((Marshaller) ((BridgeContextImpl) context).marshaller, (MarshallerImpl) object, output);
    }

    public final void marshal(T object, ContentHandler contentHandler) throws JAXBException {
        marshal((Bridge<T>) object, contentHandler, (AttachmentMarshaller) null);
    }

    public final void marshal(T object, ContentHandler contentHandler, AttachmentMarshaller am2) throws JAXBException {
        Marshaller m2 = this.context.marshallerPool.take();
        m2.setAttachmentMarshaller(am2);
        marshal(m2, (Marshaller) object, contentHandler);
        m2.setAttachmentMarshaller(null);
        this.context.marshallerPool.recycle(m2);
    }

    public final void marshal(@NotNull BridgeContext context, T object, ContentHandler contentHandler) throws JAXBException {
        marshal((Marshaller) ((BridgeContextImpl) context).marshaller, (MarshallerImpl) object, contentHandler);
    }

    public final void marshal(T object, Result result) throws JAXBException {
        Marshaller m2 = this.context.marshallerPool.take();
        marshal(m2, (Marshaller) object, result);
        this.context.marshallerPool.recycle(m2);
    }

    public final void marshal(@NotNull BridgeContext context, T object, Result result) throws JAXBException {
        marshal((Marshaller) ((BridgeContextImpl) context).marshaller, (MarshallerImpl) object, result);
    }

    private T exit(T r2, Unmarshaller u2) {
        u2.setAttachmentUnmarshaller(null);
        this.context.unmarshallerPool.recycle(u2);
        return r2;
    }

    @NotNull
    public final T unmarshal(@NotNull XMLStreamReader in) throws JAXBException {
        return unmarshal(in, (AttachmentUnmarshaller) null);
    }

    @NotNull
    public final T unmarshal(@NotNull XMLStreamReader xMLStreamReader, @Nullable AttachmentUnmarshaller attachmentUnmarshaller) throws JAXBException {
        Unmarshaller unmarshallerTake = this.context.unmarshallerPool.take();
        unmarshallerTake.setAttachmentUnmarshaller(attachmentUnmarshaller);
        return exit(unmarshal(unmarshallerTake, xMLStreamReader), unmarshallerTake);
    }

    @NotNull
    public final T unmarshal(@NotNull BridgeContext context, @NotNull XMLStreamReader in) throws JAXBException {
        return unmarshal(((BridgeContextImpl) context).unmarshaller, in);
    }

    @NotNull
    public final T unmarshal(@NotNull Source in) throws JAXBException {
        return unmarshal(in, (AttachmentUnmarshaller) null);
    }

    @NotNull
    public final T unmarshal(@NotNull Source source, @Nullable AttachmentUnmarshaller attachmentUnmarshaller) throws JAXBException {
        Unmarshaller unmarshallerTake = this.context.unmarshallerPool.take();
        unmarshallerTake.setAttachmentUnmarshaller(attachmentUnmarshaller);
        return exit(unmarshal(unmarshallerTake, source), unmarshallerTake);
    }

    @NotNull
    public final T unmarshal(@NotNull BridgeContext context, @NotNull Source in) throws JAXBException {
        return unmarshal(((BridgeContextImpl) context).unmarshaller, in);
    }

    @NotNull
    public final T unmarshal(@NotNull InputStream inputStream) throws JAXBException {
        Unmarshaller unmarshallerTake = this.context.unmarshallerPool.take();
        return exit(unmarshal(unmarshallerTake, inputStream), unmarshallerTake);
    }

    @NotNull
    public final T unmarshal(@NotNull BridgeContext context, @NotNull InputStream in) throws JAXBException {
        return unmarshal(((BridgeContextImpl) context).unmarshaller, in);
    }

    @NotNull
    public final T unmarshal(@NotNull Node n2) throws JAXBException {
        return unmarshal(n2, (AttachmentUnmarshaller) null);
    }

    @NotNull
    public final T unmarshal(@NotNull Node node, @Nullable AttachmentUnmarshaller attachmentUnmarshaller) throws JAXBException {
        Unmarshaller unmarshallerTake = this.context.unmarshallerPool.take();
        unmarshallerTake.setAttachmentUnmarshaller(attachmentUnmarshaller);
        return exit(unmarshal(unmarshallerTake, node), unmarshallerTake);
    }

    @NotNull
    public final T unmarshal(@NotNull BridgeContext context, @NotNull Node n2) throws JAXBException {
        return unmarshal(((BridgeContextImpl) context).unmarshaller, n2);
    }
}
