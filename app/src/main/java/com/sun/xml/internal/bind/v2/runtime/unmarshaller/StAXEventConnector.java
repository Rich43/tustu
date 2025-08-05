package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/StAXEventConnector.class */
final class StAXEventConnector extends StAXConnector {
    private final XMLEventReader staxEventReader;
    private XMLEvent event;
    private final AttributesImpl attrs;
    private final StringBuilder buffer;
    private boolean seenText;

    public StAXEventConnector(XMLEventReader staxCore, XmlVisitor visitor) {
        super(visitor);
        this.attrs = new AttributesImpl();
        this.buffer = new StringBuilder();
        this.staxEventReader = staxCore;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.StAXConnector
    public void bridge() throws XMLStreamException {
        try {
            int depth = 0;
            this.event = this.staxEventReader.peek();
            if (!this.event.isStartDocument() && !this.event.isStartElement()) {
                throw new IllegalStateException();
            }
            do {
                this.event = this.staxEventReader.nextEvent();
            } while (!this.event.isStartElement());
            handleStartDocument(this.event.asStartElement().getNamespaceContext());
            while (true) {
                switch (this.event.getEventType()) {
                    case 1:
                        handleStartElement(this.event.asStartElement());
                        depth++;
                        break;
                    case 2:
                        depth--;
                        handleEndElement(this.event.asEndElement());
                        if (depth != 0) {
                            break;
                        } else {
                            handleEndDocument();
                            this.event = null;
                            return;
                        }
                    case 4:
                    case 6:
                    case 12:
                        handleCharacters(this.event.asCharacters());
                        break;
                }
                this.event = this.staxEventReader.nextEvent();
            }
        } catch (SAXException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.StAXConnector
    protected Location getCurrentLocation() {
        return this.event.getLocation();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.StAXConnector
    protected String getCurrentQName() {
        QName qName;
        if (this.event.isEndElement()) {
            qName = this.event.asEndElement().getName();
        } else {
            qName = this.event.asStartElement().getName();
        }
        return getQName(qName.getPrefix(), qName.getLocalPart());
    }

    private void handleCharacters(Characters event) throws XMLStreamException, SAXException {
        XMLEvent next;
        if (!this.predictor.expectText()) {
            return;
        }
        this.seenText = true;
        while (true) {
            next = this.staxEventReader.peek();
            if (!isIgnorable(next)) {
                break;
            } else {
                this.staxEventReader.nextEvent();
            }
        }
        if (isTag(next)) {
            this.visitor.text(event.getData());
            return;
        }
        this.buffer.append(event.getData());
        while (true) {
            XMLEvent next2 = this.staxEventReader.peek();
            if (isIgnorable(next2)) {
                this.staxEventReader.nextEvent();
            } else if (isTag(next2)) {
                this.visitor.text(this.buffer);
                this.buffer.setLength(0);
                return;
            } else {
                this.buffer.append(next2.asCharacters().getData());
                this.staxEventReader.nextEvent();
            }
        }
    }

    private boolean isTag(XMLEvent event) {
        int eventType = event.getEventType();
        return eventType == 1 || eventType == 2;
    }

    private boolean isIgnorable(XMLEvent event) {
        int eventType = event.getEventType();
        return eventType == 5 || eventType == 3;
    }

    private void handleEndElement(EndElement event) throws SAXException {
        if (!this.seenText && this.predictor.expectText()) {
            this.visitor.text("");
        }
        QName qName = event.getName();
        this.tagName.uri = fixNull(qName.getNamespaceURI());
        this.tagName.local = qName.getLocalPart();
        this.visitor.endElement(this.tagName);
        Iterator<Namespace> i2 = event.getNamespaces();
        while (i2.hasNext()) {
            String prefix = fixNull(i2.next().getPrefix());
            this.visitor.endPrefixMapping(prefix);
        }
        this.seenText = false;
    }

    private void handleStartElement(StartElement event) throws SAXException {
        Iterator i2 = event.getNamespaces();
        while (i2.hasNext()) {
            Namespace ns = (Namespace) i2.next();
            this.visitor.startPrefixMapping(fixNull(ns.getPrefix()), fixNull(ns.getNamespaceURI()));
        }
        QName qName = event.getName();
        this.tagName.uri = fixNull(qName.getNamespaceURI());
        String localName = qName.getLocalPart();
        this.tagName.uri = fixNull(qName.getNamespaceURI());
        this.tagName.local = localName;
        this.tagName.atts = getAttributes(event);
        this.visitor.startElement(this.tagName);
        this.seenText = false;
    }

    private Attributes getAttributes(StartElement event) {
        String str;
        this.attrs.clear();
        Iterator i2 = event.getAttributes();
        while (i2.hasNext()) {
            Attribute staxAttr = (Attribute) i2.next();
            QName name = staxAttr.getName();
            String uri = fixNull(name.getNamespaceURI());
            String localName = name.getLocalPart();
            String prefix = name.getPrefix();
            if (prefix == null || prefix.length() == 0) {
                str = localName;
            } else {
                str = prefix + ':' + localName;
            }
            String qName = str;
            String type = staxAttr.getDTDType();
            String value = staxAttr.getValue();
            this.attrs.addAttribute(uri, localName, qName, type, value);
        }
        return this.attrs;
    }
}
