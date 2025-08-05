package com.sun.xml.internal.fastinfoset.stax.events;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
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

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/events/StAXEventWriter.class */
public class StAXEventWriter implements XMLEventWriter {
    private XMLStreamWriter _streamWriter;

    public StAXEventWriter(XMLStreamWriter streamWriter) {
        this._streamWriter = streamWriter;
    }

    @Override // javax.xml.stream.XMLEventWriter
    public void flush() throws XMLStreamException {
        this._streamWriter.flush();
    }

    @Override // javax.xml.stream.XMLEventWriter
    public void close() throws XMLStreamException {
        this._streamWriter.close();
    }

    @Override // javax.xml.stream.XMLEventWriter
    public void add(XMLEventReader eventReader) throws XMLStreamException {
        if (eventReader == null) {
            throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.nullEventReader"));
        }
        while (eventReader.hasNext()) {
            add(eventReader.nextEvent());
        }
    }

    @Override // javax.xml.stream.XMLEventWriter, javax.xml.stream.util.XMLEventConsumer
    public void add(XMLEvent event) throws XMLStreamException {
        int type = event.getEventType();
        switch (type) {
            case 1:
                StartElement startElement = event.asStartElement();
                QName qname = startElement.getName();
                this._streamWriter.writeStartElement(qname.getPrefix(), qname.getLocalPart(), qname.getNamespaceURI());
                Iterator iterator = startElement.getNamespaces();
                while (iterator.hasNext()) {
                    Namespace namespace = (Namespace) iterator.next();
                    this._streamWriter.writeNamespace(namespace.getPrefix(), namespace.getNamespaceURI());
                }
                Iterator attributes = startElement.getAttributes();
                while (attributes.hasNext()) {
                    Attribute attribute = (Attribute) attributes.next();
                    QName name = attribute.getName();
                    this._streamWriter.writeAttribute(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart(), attribute.getValue());
                }
                return;
            case 2:
                this._streamWriter.writeEndElement();
                return;
            case 3:
                ProcessingInstruction processingInstruction = (ProcessingInstruction) event;
                this._streamWriter.writeProcessingInstruction(processingInstruction.getTarget(), processingInstruction.getData());
                return;
            case 4:
                Characters characters = event.asCharacters();
                if (characters.isCData()) {
                    this._streamWriter.writeCData(characters.getData());
                    return;
                } else {
                    this._streamWriter.writeCharacters(characters.getData());
                    return;
                }
            case 5:
                Comment comment = (Comment) event;
                this._streamWriter.writeComment(comment.getText());
                return;
            case 6:
            default:
                throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.eventTypeNotSupported", new Object[]{Util.getEventTypeString(type)}));
            case 7:
                StartDocument startDocument = (StartDocument) event;
                this._streamWriter.writeStartDocument(startDocument.getCharacterEncodingScheme(), startDocument.getVersion());
                return;
            case 8:
                this._streamWriter.writeEndDocument();
                return;
            case 9:
                EntityReference entityReference = (EntityReference) event;
                this._streamWriter.writeEntityRef(entityReference.getName());
                return;
            case 10:
                Attribute attribute2 = (Attribute) event;
                QName qname2 = attribute2.getName();
                this._streamWriter.writeAttribute(qname2.getPrefix(), qname2.getNamespaceURI(), qname2.getLocalPart(), attribute2.getValue());
                return;
            case 11:
                DTD dtd = (DTD) event;
                this._streamWriter.writeDTD(dtd.getDocumentTypeDeclaration());
                return;
            case 12:
                Characters characters2 = (Characters) event;
                if (characters2.isCData()) {
                    this._streamWriter.writeCData(characters2.getData());
                    return;
                }
                return;
            case 13:
                Namespace namespace2 = (Namespace) event;
                this._streamWriter.writeNamespace(namespace2.getPrefix(), namespace2.getNamespaceURI());
                return;
        }
    }

    @Override // javax.xml.stream.XMLEventWriter
    public String getPrefix(String uri) throws XMLStreamException {
        return this._streamWriter.getPrefix(uri);
    }

    @Override // javax.xml.stream.XMLEventWriter
    public NamespaceContext getNamespaceContext() {
        return this._streamWriter.getNamespaceContext();
    }

    @Override // javax.xml.stream.XMLEventWriter
    public void setDefaultNamespace(String uri) throws XMLStreamException {
        this._streamWriter.setDefaultNamespace(uri);
    }

    @Override // javax.xml.stream.XMLEventWriter
    public void setNamespaceContext(NamespaceContext namespaceContext) throws XMLStreamException {
        this._streamWriter.setNamespaceContext(namespaceContext);
    }

    @Override // javax.xml.stream.XMLEventWriter
    public void setPrefix(String prefix, String uri) throws XMLStreamException {
        this._streamWriter.setPrefix(prefix, uri);
    }
}
