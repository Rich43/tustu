package com.sun.xml.internal.stream.writers;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/* loaded from: rt.jar:com/sun/xml/internal/stream/writers/XMLEventWriterImpl.class */
public class XMLEventWriterImpl implements XMLEventWriter {
    private XMLStreamWriter fStreamWriter;
    private static final boolean DEBUG = false;

    public XMLEventWriterImpl(XMLStreamWriter streamWriter) {
        this.fStreamWriter = streamWriter;
    }

    @Override // javax.xml.stream.XMLEventWriter
    public void add(XMLEventReader xMLEventReader) throws XMLStreamException {
        if (xMLEventReader == null) {
            throw new XMLStreamException("Event reader shouldn't be null");
        }
        while (xMLEventReader.hasNext()) {
            add(xMLEventReader.nextEvent());
        }
    }

    @Override // javax.xml.stream.XMLEventWriter, javax.xml.stream.util.XMLEventConsumer
    public void add(XMLEvent xMLEvent) throws XMLStreamException {
        int type = xMLEvent.getEventType();
        switch (type) {
            case 1:
                StartElement startElement = xMLEvent.asStartElement();
                QName qname = startElement.getName();
                this.fStreamWriter.writeStartElement(qname.getPrefix(), qname.getLocalPart(), qname.getNamespaceURI());
                Iterator iterator = startElement.getNamespaces();
                while (iterator.hasNext()) {
                    Namespace namespace = (Namespace) iterator.next();
                    this.fStreamWriter.writeNamespace(namespace.getPrefix(), namespace.getNamespaceURI());
                }
                Iterator attributes = startElement.getAttributes();
                while (attributes.hasNext()) {
                    Attribute attribute = (Attribute) attributes.next();
                    QName aqname = attribute.getName();
                    this.fStreamWriter.writeAttribute(aqname.getPrefix(), aqname.getNamespaceURI(), aqname.getLocalPart(), attribute.getValue());
                }
                break;
            case 2:
                this.fStreamWriter.writeEndElement();
                break;
            case 3:
                ProcessingInstruction processingInstruction = (ProcessingInstruction) xMLEvent;
                this.fStreamWriter.writeProcessingInstruction(processingInstruction.getTarget(), processingInstruction.getData());
                break;
            case 4:
                Characters characters = xMLEvent.asCharacters();
                if (characters.isCData()) {
                    this.fStreamWriter.writeCData(characters.getData());
                    break;
                } else {
                    this.fStreamWriter.writeCharacters(characters.getData());
                    break;
                }
            case 5:
                Comment comment = (Comment) xMLEvent;
                this.fStreamWriter.writeComment(comment.getText());
                break;
            case 7:
                StartDocument startDocument = (StartDocument) xMLEvent;
                try {
                    this.fStreamWriter.writeStartDocument(startDocument.getCharacterEncodingScheme(), startDocument.getVersion());
                    break;
                } catch (XMLStreamException e2) {
                    this.fStreamWriter.writeStartDocument(startDocument.getVersion());
                    return;
                }
            case 8:
                this.fStreamWriter.writeEndDocument();
                break;
            case 9:
                EntityReference entityReference = (EntityReference) xMLEvent;
                this.fStreamWriter.writeEntityRef(entityReference.getName());
                break;
            case 10:
                Attribute attribute2 = (Attribute) xMLEvent;
                QName qname2 = attribute2.getName();
                this.fStreamWriter.writeAttribute(qname2.getPrefix(), qname2.getNamespaceURI(), qname2.getLocalPart(), attribute2.getValue());
                break;
            case 11:
                DTD dtd = (DTD) xMLEvent;
                this.fStreamWriter.writeDTD(dtd.getDocumentTypeDeclaration());
                break;
            case 12:
                Characters characters2 = (Characters) xMLEvent;
                if (characters2.isCData()) {
                    this.fStreamWriter.writeCData(characters2.getData());
                    break;
                }
                break;
            case 13:
                Namespace namespace2 = (Namespace) xMLEvent;
                this.fStreamWriter.writeNamespace(namespace2.getPrefix(), namespace2.getNamespaceURI());
                break;
        }
    }

    @Override // javax.xml.stream.XMLEventWriter
    public void close() throws XMLStreamException {
        this.fStreamWriter.close();
    }

    @Override // javax.xml.stream.XMLEventWriter
    public void flush() throws XMLStreamException {
        this.fStreamWriter.flush();
    }

    @Override // javax.xml.stream.XMLEventWriter
    public NamespaceContext getNamespaceContext() {
        return this.fStreamWriter.getNamespaceContext();
    }

    @Override // javax.xml.stream.XMLEventWriter
    public String getPrefix(String namespaceURI) throws XMLStreamException {
        return this.fStreamWriter.getPrefix(namespaceURI);
    }

    @Override // javax.xml.stream.XMLEventWriter
    public void setDefaultNamespace(String uri) throws XMLStreamException {
        this.fStreamWriter.setDefaultNamespace(uri);
    }

    @Override // javax.xml.stream.XMLEventWriter
    public void setNamespaceContext(NamespaceContext namespaceContext) throws XMLStreamException {
        this.fStreamWriter.setNamespaceContext(namespaceContext);
    }

    @Override // javax.xml.stream.XMLEventWriter
    public void setPrefix(String prefix, String uri) throws XMLStreamException {
        this.fStreamWriter.setPrefix(prefix, uri);
    }
}
