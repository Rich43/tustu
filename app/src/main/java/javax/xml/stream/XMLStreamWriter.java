package javax.xml.stream;

import javax.xml.namespace.NamespaceContext;

/* loaded from: rt.jar:javax/xml/stream/XMLStreamWriter.class */
public interface XMLStreamWriter {
    void writeStartElement(String str) throws XMLStreamException;

    void writeStartElement(String str, String str2) throws XMLStreamException;

    void writeStartElement(String str, String str2, String str3) throws XMLStreamException;

    void writeEmptyElement(String str, String str2) throws XMLStreamException;

    void writeEmptyElement(String str, String str2, String str3) throws XMLStreamException;

    void writeEmptyElement(String str) throws XMLStreamException;

    void writeEndElement() throws XMLStreamException;

    void writeEndDocument() throws XMLStreamException;

    void close() throws XMLStreamException;

    void flush() throws XMLStreamException;

    void writeAttribute(String str, String str2) throws XMLStreamException;

    void writeAttribute(String str, String str2, String str3, String str4) throws XMLStreamException;

    void writeAttribute(String str, String str2, String str3) throws XMLStreamException;

    void writeNamespace(String str, String str2) throws XMLStreamException;

    void writeDefaultNamespace(String str) throws XMLStreamException;

    void writeComment(String str) throws XMLStreamException;

    void writeProcessingInstruction(String str) throws XMLStreamException;

    void writeProcessingInstruction(String str, String str2) throws XMLStreamException;

    void writeCData(String str) throws XMLStreamException;

    void writeDTD(String str) throws XMLStreamException;

    void writeEntityRef(String str) throws XMLStreamException;

    void writeStartDocument() throws XMLStreamException;

    void writeStartDocument(String str) throws XMLStreamException;

    void writeStartDocument(String str, String str2) throws XMLStreamException;

    void writeCharacters(String str) throws XMLStreamException;

    void writeCharacters(char[] cArr, int i2, int i3) throws XMLStreamException;

    String getPrefix(String str) throws XMLStreamException;

    void setPrefix(String str, String str2) throws XMLStreamException;

    void setDefaultNamespace(String str) throws XMLStreamException;

    void setNamespaceContext(NamespaceContext namespaceContext) throws XMLStreamException;

    NamespaceContext getNamespaceContext();

    Object getProperty(String str) throws IllegalArgumentException;
}
