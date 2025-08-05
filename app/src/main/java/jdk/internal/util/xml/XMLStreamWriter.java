package jdk.internal.util.xml;

/* loaded from: rt.jar:jdk/internal/util/xml/XMLStreamWriter.class */
public interface XMLStreamWriter {
    public static final String DEFAULT_XML_VERSION = "1.0";
    public static final String DEFAULT_ENCODING = "UTF-8";

    void writeStartElement(String str) throws XMLStreamException;

    void writeEmptyElement(String str) throws XMLStreamException;

    void writeEndElement() throws XMLStreamException;

    void writeEndDocument() throws XMLStreamException;

    void close() throws XMLStreamException;

    void flush() throws XMLStreamException;

    void writeAttribute(String str, String str2) throws XMLStreamException;

    void writeCData(String str) throws XMLStreamException;

    void writeDTD(String str) throws XMLStreamException;

    void writeStartDocument() throws XMLStreamException;

    void writeStartDocument(String str) throws XMLStreamException;

    void writeStartDocument(String str, String str2) throws XMLStreamException;

    void writeCharacters(String str) throws XMLStreamException;

    void writeCharacters(char[] cArr, int i2, int i3) throws XMLStreamException;
}
