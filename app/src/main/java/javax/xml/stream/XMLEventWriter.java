package javax.xml.stream;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.XMLEventConsumer;

/* loaded from: rt.jar:javax/xml/stream/XMLEventWriter.class */
public interface XMLEventWriter extends XMLEventConsumer {
    void flush() throws XMLStreamException;

    void close() throws XMLStreamException;

    @Override // javax.xml.stream.util.XMLEventConsumer
    void add(XMLEvent xMLEvent) throws XMLStreamException;

    void add(XMLEventReader xMLEventReader) throws XMLStreamException;

    String getPrefix(String str) throws XMLStreamException;

    void setPrefix(String str, String str2) throws XMLStreamException;

    void setDefaultNamespace(String str) throws XMLStreamException;

    void setNamespaceContext(NamespaceContext namespaceContext) throws XMLStreamException;

    NamespaceContext getNamespaceContext();
}
