package com.sun.xml.internal.fastinfoset.stax.events;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.XMLEventAllocator;
import javax.xml.stream.util.XMLEventConsumer;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/events/StAXEventAllocatorBase.class */
public class StAXEventAllocatorBase implements XMLEventAllocator {
    XMLEventFactory factory;

    public StAXEventAllocatorBase() {
        if (System.getProperty("javax.xml.stream.XMLEventFactory") == null) {
            System.setProperty("javax.xml.stream.XMLEventFactory", "com.sun.xml.internal.fastinfoset.stax.factory.StAXEventFactory");
        }
        this.factory = XMLEventFactory.newInstance();
    }

    @Override // javax.xml.stream.util.XMLEventAllocator
    public XMLEventAllocator newInstance() {
        return new StAXEventAllocatorBase();
    }

    @Override // javax.xml.stream.util.XMLEventAllocator
    public XMLEvent allocate(XMLStreamReader streamReader) throws XMLStreamException {
        if (streamReader == null) {
            throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.nullReader"));
        }
        return getXMLEvent(streamReader);
    }

    @Override // javax.xml.stream.util.XMLEventAllocator
    public void allocate(XMLStreamReader streamReader, XMLEventConsumer consumer) throws XMLStreamException {
        consumer.add(getXMLEvent(streamReader));
    }

    XMLEvent getXMLEvent(XMLStreamReader reader) {
        XMLEvent event = null;
        int eventType = reader.getEventType();
        this.factory.setLocation(reader.getLocation());
        switch (eventType) {
            case 1:
                StartElementEvent startElement = (StartElementEvent) this.factory.createStartElement(reader.getPrefix(), reader.getNamespaceURI(), reader.getLocalName());
                addAttributes(startElement, reader);
                addNamespaces(startElement, reader);
                event = startElement;
                break;
            case 2:
                EndElementEvent endElement = (EndElementEvent) this.factory.createEndElement(reader.getPrefix(), reader.getNamespaceURI(), reader.getLocalName());
                addNamespaces(endElement, reader);
                event = endElement;
                break;
            case 3:
                event = this.factory.createProcessingInstruction(reader.getPITarget(), reader.getPIData());
                break;
            case 4:
                if (reader.isWhiteSpace()) {
                    event = this.factory.createSpace(reader.getText());
                    break;
                } else {
                    event = this.factory.createCharacters(reader.getText());
                    break;
                }
            case 5:
                event = this.factory.createComment(reader.getText());
                break;
            case 6:
                event = this.factory.createSpace(reader.getText());
                break;
            case 7:
                StartDocumentEvent docEvent = (StartDocumentEvent) this.factory.createStartDocument(reader.getVersion(), reader.getEncoding(), reader.isStandalone());
                if (reader.getCharacterEncodingScheme() != null) {
                    docEvent.setDeclaredEncoding(true);
                } else {
                    docEvent.setDeclaredEncoding(false);
                }
                event = docEvent;
                break;
            case 8:
                EndDocumentEvent endDocumentEvent = new EndDocumentEvent();
                event = endDocumentEvent;
                break;
            case 9:
                event = this.factory.createEntityReference(reader.getLocalName(), new EntityDeclarationImpl(reader.getLocalName(), reader.getText()));
                break;
            case 10:
                event = null;
                break;
            case 11:
                event = this.factory.createDTD(reader.getText());
                break;
            case 12:
                event = this.factory.createCData(reader.getText());
                break;
        }
        return event;
    }

    protected void addAttributes(StartElementEvent event, XMLStreamReader streamReader) {
        for (int i2 = 0; i2 < streamReader.getAttributeCount(); i2++) {
            AttributeBase attr = (AttributeBase) this.factory.createAttribute(streamReader.getAttributeName(i2), streamReader.getAttributeValue(i2));
            attr.setAttributeType(streamReader.getAttributeType(i2));
            attr.setSpecified(streamReader.isAttributeSpecified(i2));
            event.addAttribute(attr);
        }
    }

    protected void addNamespaces(StartElementEvent event, XMLStreamReader streamReader) {
        for (int i2 = 0; i2 < streamReader.getNamespaceCount(); i2++) {
            Namespace namespace = this.factory.createNamespace(streamReader.getNamespacePrefix(i2), streamReader.getNamespaceURI(i2));
            event.addNamespace(namespace);
        }
    }

    protected void addNamespaces(EndElementEvent event, XMLStreamReader streamReader) {
        for (int i2 = 0; i2 < streamReader.getNamespaceCount(); i2++) {
            Namespace namespace = this.factory.createNamespace(streamReader.getNamespacePrefix(i2), streamReader.getNamespaceURI(i2));
            event.addNamespace(namespace);
        }
    }
}
