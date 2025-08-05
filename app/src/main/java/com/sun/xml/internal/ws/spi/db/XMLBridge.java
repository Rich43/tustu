package com.sun.xml.internal.ws.spi.db;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
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

/* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/XMLBridge.class */
public interface XMLBridge<T> {
    @NotNull
    BindingContext context();

    void marshal(T t2, XMLStreamWriter xMLStreamWriter, AttachmentMarshaller attachmentMarshaller) throws JAXBException;

    void marshal(T t2, OutputStream outputStream, NamespaceContext namespaceContext, AttachmentMarshaller attachmentMarshaller) throws JAXBException;

    void marshal(T t2, Node node) throws JAXBException;

    void marshal(T t2, ContentHandler contentHandler, AttachmentMarshaller attachmentMarshaller) throws JAXBException;

    void marshal(T t2, Result result) throws JAXBException;

    @NotNull
    T unmarshal(@NotNull XMLStreamReader xMLStreamReader, @Nullable AttachmentUnmarshaller attachmentUnmarshaller) throws JAXBException;

    @NotNull
    T unmarshal(@NotNull Source source, @Nullable AttachmentUnmarshaller attachmentUnmarshaller) throws JAXBException;

    @NotNull
    T unmarshal(@NotNull InputStream inputStream) throws JAXBException;

    @NotNull
    T unmarshal(@NotNull Node node, @Nullable AttachmentUnmarshaller attachmentUnmarshaller) throws JAXBException;

    TypeInfo getTypeInfo();

    boolean supportOutputStream();
}
