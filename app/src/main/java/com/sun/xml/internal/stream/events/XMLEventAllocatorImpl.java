package com.sun.xml.internal.stream.events;

import com.sun.org.apache.xerces.internal.impl.PropertyManager;
import com.sun.org.apache.xerces.internal.util.NamespaceContextWrapper;
import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.XMLEventAllocator;
import javax.xml.stream.util.XMLEventConsumer;

/* loaded from: rt.jar:com/sun/xml/internal/stream/events/XMLEventAllocatorImpl.class */
public class XMLEventAllocatorImpl implements XMLEventAllocator {
    @Override // javax.xml.stream.util.XMLEventAllocator
    public XMLEvent allocate(XMLStreamReader xMLStreamReader) throws XMLStreamException {
        if (xMLStreamReader == null) {
            throw new XMLStreamException("Reader cannot be null");
        }
        return getXMLEvent(xMLStreamReader);
    }

    @Override // javax.xml.stream.util.XMLEventAllocator
    public void allocate(XMLStreamReader xMLStreamReader, XMLEventConsumer xMLEventConsumer) throws XMLStreamException {
        XMLEvent currentEvent = getXMLEvent(xMLStreamReader);
        if (currentEvent != null) {
            xMLEventConsumer.add(currentEvent);
        }
    }

    @Override // javax.xml.stream.util.XMLEventAllocator
    public XMLEventAllocator newInstance() {
        return new XMLEventAllocatorImpl();
    }

    XMLEvent getXMLEvent(XMLStreamReader streamReader) {
        XMLEvent event = null;
        int eventType = streamReader.getEventType();
        switch (eventType) {
            case 1:
                StartElementEvent startElementEvent = new StartElementEvent(getQName(streamReader));
                fillAttributes(startElementEvent, streamReader);
                if (((Boolean) streamReader.getProperty(XMLInputFactory.IS_NAMESPACE_AWARE)).booleanValue()) {
                    fillNamespaceAttributes(startElementEvent, streamReader);
                    setNamespaceContext(startElementEvent, streamReader);
                }
                startElementEvent.setLocation(streamReader.getLocation());
                event = startElementEvent;
                break;
            case 2:
                EndElementEvent endElementEvent = new EndElementEvent(getQName(streamReader));
                endElementEvent.setLocation(streamReader.getLocation());
                if (((Boolean) streamReader.getProperty(XMLInputFactory.IS_NAMESPACE_AWARE)).booleanValue()) {
                    fillNamespaceAttributes(endElementEvent, streamReader);
                }
                event = endElementEvent;
                break;
            case 3:
                ProcessingInstructionEvent piEvent = new ProcessingInstructionEvent(streamReader.getPITarget(), streamReader.getPIData());
                piEvent.setLocation(streamReader.getLocation());
                event = piEvent;
                break;
            case 4:
                CharacterEvent cDataEvent = new CharacterEvent(streamReader.getText());
                cDataEvent.setLocation(streamReader.getLocation());
                event = cDataEvent;
                break;
            case 5:
                CommentEvent commentEvent = new CommentEvent(streamReader.getText());
                commentEvent.setLocation(streamReader.getLocation());
                event = commentEvent;
                break;
            case 6:
                CharacterEvent spaceEvent = new CharacterEvent(streamReader.getText(), false, true);
                spaceEvent.setLocation(streamReader.getLocation());
                event = spaceEvent;
                break;
            case 7:
                StartDocumentEvent sdEvent = new StartDocumentEvent();
                sdEvent.setVersion(streamReader.getVersion());
                sdEvent.setEncoding(streamReader.getEncoding());
                if (streamReader.getCharacterEncodingScheme() != null) {
                    sdEvent.setDeclaredEncoding(true);
                } else {
                    sdEvent.setDeclaredEncoding(false);
                }
                sdEvent.setStandalone(streamReader.isStandalone());
                sdEvent.setLocation(streamReader.getLocation());
                event = sdEvent;
                break;
            case 8:
                EndDocumentEvent endDocumentEvent = new EndDocumentEvent();
                endDocumentEvent.setLocation(streamReader.getLocation());
                event = endDocumentEvent;
                break;
            case 9:
                EntityReferenceEvent entityEvent = new EntityReferenceEvent(streamReader.getLocalName(), new EntityDeclarationImpl(streamReader.getLocalName(), streamReader.getText()));
                entityEvent.setLocation(streamReader.getLocation());
                event = entityEvent;
                break;
            case 10:
                event = null;
                break;
            case 11:
                DTDEvent dtdEvent = new DTDEvent(streamReader.getText());
                dtdEvent.setLocation(streamReader.getLocation());
                List entities = (List) streamReader.getProperty(PropertyManager.STAX_ENTITIES);
                if (entities != null && entities.size() != 0) {
                    dtdEvent.setEntities(entities);
                }
                List notations = (List) streamReader.getProperty(PropertyManager.STAX_NOTATIONS);
                if (notations != null && notations.size() != 0) {
                    dtdEvent.setNotations(notations);
                }
                event = dtdEvent;
                break;
            case 12:
                CharacterEvent cDataEvent2 = new CharacterEvent(streamReader.getText(), true);
                cDataEvent2.setLocation(streamReader.getLocation());
                event = cDataEvent2;
                break;
        }
        return event;
    }

    protected XMLEvent getNextEvent(XMLStreamReader streamReader) throws XMLStreamException {
        streamReader.next();
        return getXMLEvent(streamReader);
    }

    protected void fillAttributes(StartElementEvent event, XMLStreamReader xmlr) {
        int len = xmlr.getAttributeCount();
        for (int i2 = 0; i2 < len; i2++) {
            QName qname = xmlr.getAttributeName(i2);
            AttributeImpl attr = new AttributeImpl();
            attr.setName(qname);
            attr.setAttributeType(xmlr.getAttributeType(i2));
            attr.setSpecified(xmlr.isAttributeSpecified(i2));
            attr.setValue(xmlr.getAttributeValue(i2));
            event.addAttribute(attr);
        }
    }

    protected void fillNamespaceAttributes(StartElementEvent event, XMLStreamReader xmlr) {
        int count = xmlr.getNamespaceCount();
        for (int i2 = 0; i2 < count; i2++) {
            String uri = xmlr.getNamespaceURI(i2);
            String prefix = xmlr.getNamespacePrefix(i2);
            if (prefix == null) {
                prefix = "";
            }
            NamespaceImpl attr = new NamespaceImpl(prefix, uri);
            event.addNamespaceAttribute(attr);
        }
    }

    protected void fillNamespaceAttributes(EndElementEvent event, XMLStreamReader xmlr) {
        int count = xmlr.getNamespaceCount();
        for (int i2 = 0; i2 < count; i2++) {
            String uri = xmlr.getNamespaceURI(i2);
            String prefix = xmlr.getNamespacePrefix(i2);
            if (prefix == null) {
                prefix = "";
            }
            NamespaceImpl attr = new NamespaceImpl(prefix, uri);
            event.addNamespace(attr);
        }
    }

    private void setNamespaceContext(StartElementEvent event, XMLStreamReader xmlr) {
        NamespaceContextWrapper contextWrapper = (NamespaceContextWrapper) xmlr.getNamespaceContext();
        NamespaceSupport ns = new NamespaceSupport(contextWrapper.getNamespaceContext());
        event.setNamespaceContext(new NamespaceContextWrapper(ns));
    }

    private QName getQName(XMLStreamReader xmlr) {
        return new QName(xmlr.getNamespaceURI(), xmlr.getLocalName(), xmlr.getPrefix());
    }
}
