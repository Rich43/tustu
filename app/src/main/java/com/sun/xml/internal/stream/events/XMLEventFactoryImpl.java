package com.sun.xml.internal.stream.events;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityDeclaration;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;

/* loaded from: rt.jar:com/sun/xml/internal/stream/events/XMLEventFactoryImpl.class */
public class XMLEventFactoryImpl extends XMLEventFactory {
    Location location = null;

    @Override // javax.xml.stream.XMLEventFactory
    public Attribute createAttribute(String localName, String value) {
        AttributeImpl attr = new AttributeImpl(localName, value);
        if (this.location != null) {
            attr.setLocation(this.location);
        }
        return attr;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public Attribute createAttribute(QName name, String value) {
        return createAttribute(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart(), value);
    }

    @Override // javax.xml.stream.XMLEventFactory
    public Attribute createAttribute(String prefix, String namespaceURI, String localName, String value) {
        AttributeImpl attr = new AttributeImpl(prefix, namespaceURI, localName, value, (String) null);
        if (this.location != null) {
            attr.setLocation(this.location);
        }
        return attr;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public Characters createCData(String content) {
        CharacterEvent charEvent = new CharacterEvent(content, true);
        if (this.location != null) {
            charEvent.setLocation(this.location);
        }
        return charEvent;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public Characters createCharacters(String content) {
        CharacterEvent charEvent = new CharacterEvent(content);
        if (this.location != null) {
            charEvent.setLocation(this.location);
        }
        return charEvent;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public Comment createComment(String text) {
        CommentEvent charEvent = new CommentEvent(text);
        if (this.location != null) {
            charEvent.setLocation(this.location);
        }
        return charEvent;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public DTD createDTD(String dtd) {
        DTDEvent dtdEvent = new DTDEvent(dtd);
        if (this.location != null) {
            dtdEvent.setLocation(this.location);
        }
        return dtdEvent;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public EndDocument createEndDocument() {
        EndDocumentEvent event = new EndDocumentEvent();
        if (this.location != null) {
            event.setLocation(this.location);
        }
        return event;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public EndElement createEndElement(QName name, Iterator namespaces) {
        return createEndElement(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart());
    }

    @Override // javax.xml.stream.XMLEventFactory
    public EndElement createEndElement(String prefix, String namespaceUri, String localName) {
        EndElementEvent event = new EndElementEvent(prefix, namespaceUri, localName);
        if (this.location != null) {
            event.setLocation(this.location);
        }
        return event;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public EndElement createEndElement(String prefix, String namespaceUri, String localName, Iterator namespaces) {
        EndElementEvent event = new EndElementEvent(prefix, namespaceUri, localName);
        if (namespaces != null) {
            while (namespaces.hasNext()) {
                event.addNamespace((Namespace) namespaces.next());
            }
        }
        if (this.location != null) {
            event.setLocation(this.location);
        }
        return event;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public EntityReference createEntityReference(String name, EntityDeclaration entityDeclaration) {
        EntityReferenceEvent event = new EntityReferenceEvent(name, entityDeclaration);
        if (this.location != null) {
            event.setLocation(this.location);
        }
        return event;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public Characters createIgnorableSpace(String content) {
        CharacterEvent event = new CharacterEvent(content, false, true);
        if (this.location != null) {
            event.setLocation(this.location);
        }
        return event;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public Namespace createNamespace(String namespaceURI) {
        NamespaceImpl event = new NamespaceImpl(namespaceURI);
        if (this.location != null) {
            event.setLocation(this.location);
        }
        return event;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public Namespace createNamespace(String prefix, String namespaceURI) {
        NamespaceImpl event = new NamespaceImpl(prefix, namespaceURI);
        if (this.location != null) {
            event.setLocation(this.location);
        }
        return event;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public ProcessingInstruction createProcessingInstruction(String target, String data) {
        ProcessingInstructionEvent event = new ProcessingInstructionEvent(target, data);
        if (this.location != null) {
            event.setLocation(this.location);
        }
        return event;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public Characters createSpace(String content) {
        CharacterEvent event = new CharacterEvent(content);
        if (this.location != null) {
            event.setLocation(this.location);
        }
        return event;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public StartDocument createStartDocument() {
        StartDocumentEvent event = new StartDocumentEvent();
        if (this.location != null) {
            event.setLocation(this.location);
        }
        return event;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public StartDocument createStartDocument(String encoding) {
        StartDocumentEvent event = new StartDocumentEvent(encoding);
        if (this.location != null) {
            event.setLocation(this.location);
        }
        return event;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public StartDocument createStartDocument(String encoding, String version) {
        StartDocumentEvent event = new StartDocumentEvent(encoding, version);
        if (this.location != null) {
            event.setLocation(this.location);
        }
        return event;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public StartDocument createStartDocument(String encoding, String version, boolean standalone) {
        StartDocumentEvent event = new StartDocumentEvent(encoding, version, standalone);
        if (this.location != null) {
            event.setLocation(this.location);
        }
        return event;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public StartElement createStartElement(QName name, Iterator attributes, Iterator namespaces) {
        return createStartElement(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart(), attributes, namespaces);
    }

    @Override // javax.xml.stream.XMLEventFactory
    public StartElement createStartElement(String prefix, String namespaceUri, String localName) {
        StartElementEvent event = new StartElementEvent(prefix, namespaceUri, localName);
        if (this.location != null) {
            event.setLocation(this.location);
        }
        return event;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public StartElement createStartElement(String prefix, String namespaceUri, String localName, Iterator attributes, Iterator namespaces) {
        return createStartElement(prefix, namespaceUri, localName, attributes, namespaces, null);
    }

    @Override // javax.xml.stream.XMLEventFactory
    public StartElement createStartElement(String prefix, String namespaceUri, String localName, Iterator attributes, Iterator namespaces, NamespaceContext context) {
        StartElementEvent elem = new StartElementEvent(prefix, namespaceUri, localName);
        elem.addAttributes(attributes);
        elem.addNamespaceAttributes(namespaces);
        elem.setNamespaceContext(context);
        if (this.location != null) {
            elem.setLocation(this.location);
        }
        return elem;
    }

    @Override // javax.xml.stream.XMLEventFactory
    public void setLocation(Location location) {
        this.location = location;
    }
}
