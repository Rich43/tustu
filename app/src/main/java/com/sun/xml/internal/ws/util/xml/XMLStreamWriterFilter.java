package com.sun.xml.internal.ws.util.xml;

import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/xml/XMLStreamWriterFilter.class */
public class XMLStreamWriterFilter implements XMLStreamWriter, XMLStreamWriterFactory.RecycleAware {
    protected XMLStreamWriter writer;

    public XMLStreamWriterFilter(XMLStreamWriter writer) {
        this.writer = writer;
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void close() throws XMLStreamException {
        this.writer.close();
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void flush() throws XMLStreamException {
        this.writer.flush();
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEndDocument() throws XMLStreamException {
        this.writer.writeEndDocument();
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEndElement() throws XMLStreamException {
        this.writer.writeEndElement();
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartDocument() throws XMLStreamException {
        this.writer.writeStartDocument();
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
        this.writer.writeCharacters(text, start, len);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void setDefaultNamespace(String uri) throws XMLStreamException {
        this.writer.setDefaultNamespace(uri);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeCData(String data) throws XMLStreamException {
        this.writer.writeCData(data);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeCharacters(String text) throws XMLStreamException {
        this.writer.writeCharacters(text);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeComment(String data) throws XMLStreamException {
        this.writer.writeComment(data);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeDTD(String dtd) throws XMLStreamException {
        this.writer.writeDTD(dtd);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
        this.writer.writeDefaultNamespace(namespaceURI);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEmptyElement(String localName) throws XMLStreamException {
        this.writer.writeEmptyElement(localName);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEntityRef(String name) throws XMLStreamException {
        this.writer.writeEntityRef(name);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeProcessingInstruction(String target) throws XMLStreamException {
        this.writer.writeProcessingInstruction(target);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartDocument(String version) throws XMLStreamException {
        this.writer.writeStartDocument(version);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartElement(String localName) throws XMLStreamException {
        this.writer.writeStartElement(localName);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public NamespaceContext getNamespaceContext() {
        return this.writer.getNamespaceContext();
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
        this.writer.setNamespaceContext(context);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public Object getProperty(String name) throws IllegalArgumentException {
        return this.writer.getProperty(name);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public String getPrefix(String uri) throws XMLStreamException {
        return this.writer.getPrefix(uri);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void setPrefix(String prefix, String uri) throws XMLStreamException {
        this.writer.setPrefix(prefix, uri);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeAttribute(String localName, String value) throws XMLStreamException {
        this.writer.writeAttribute(localName, value);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
        this.writer.writeEmptyElement(namespaceURI, localName);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
        this.writer.writeNamespace(prefix, namespaceURI);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
        this.writer.writeProcessingInstruction(target, data);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartDocument(String encoding, String version) throws XMLStreamException {
        this.writer.writeStartDocument(encoding, version);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
        this.writer.writeStartElement(namespaceURI, localName);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
        this.writer.writeAttribute(namespaceURI, localName, value);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        this.writer.writeEmptyElement(prefix, localName, namespaceURI);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        this.writer.writeStartElement(prefix, localName, namespaceURI);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
        this.writer.writeAttribute(prefix, namespaceURI, localName, value);
    }

    @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory.RecycleAware
    public void onRecycled() {
        XMLStreamWriterFactory.recycle(this.writer);
        this.writer = null;
    }
}
