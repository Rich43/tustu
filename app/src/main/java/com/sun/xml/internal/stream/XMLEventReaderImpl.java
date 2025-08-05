package com.sun.xml.internal.stream;

import com.sun.xml.internal.stream.events.XMLEventAllocatorImpl;
import java.util.NoSuchElementException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.XMLEventAllocator;

/* loaded from: rt.jar:com/sun/xml/internal/stream/XMLEventReaderImpl.class */
public class XMLEventReaderImpl implements XMLEventReader {
    protected XMLStreamReader fXMLReader;
    protected XMLEventAllocator fXMLEventAllocator;
    private XMLEvent fPeekedEvent;
    private XMLEvent fLastEvent;

    public XMLEventReaderImpl(XMLStreamReader reader) throws XMLStreamException {
        this.fXMLReader = reader;
        this.fXMLEventAllocator = (XMLEventAllocator) reader.getProperty(XMLInputFactory.ALLOCATOR);
        if (this.fXMLEventAllocator == null) {
            this.fXMLEventAllocator = new XMLEventAllocatorImpl();
        }
        this.fPeekedEvent = this.fXMLEventAllocator.allocate(this.fXMLReader);
    }

    @Override // javax.xml.stream.XMLEventReader, java.util.Iterator
    public boolean hasNext() {
        if (this.fPeekedEvent != null) {
            return true;
        }
        try {
            boolean next = this.fXMLReader.hasNext();
            return next;
        } catch (XMLStreamException e2) {
            return false;
        }
    }

    @Override // javax.xml.stream.XMLEventReader
    public XMLEvent nextEvent() throws XMLStreamException {
        if (this.fPeekedEvent != null) {
            this.fLastEvent = this.fPeekedEvent;
            this.fPeekedEvent = null;
            return this.fLastEvent;
        }
        if (this.fXMLReader.hasNext()) {
            this.fXMLReader.next();
            XMLEvent xMLEventAllocate = this.fXMLEventAllocator.allocate(this.fXMLReader);
            this.fLastEvent = xMLEventAllocate;
            return xMLEventAllocate;
        }
        this.fLastEvent = null;
        throw new NoSuchElementException();
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override // javax.xml.stream.XMLEventReader
    public void close() throws XMLStreamException {
        this.fXMLReader.close();
    }

    @Override // javax.xml.stream.XMLEventReader
    public String getElementText() throws XMLStreamException {
        if (this.fLastEvent.getEventType() != 1) {
            throw new XMLStreamException("parser must be on START_ELEMENT to read next text", this.fLastEvent.getLocation());
        }
        String data = null;
        if (this.fPeekedEvent != null) {
            XMLEvent event = this.fPeekedEvent;
            this.fPeekedEvent = null;
            int type = event.getEventType();
            if (type == 4 || type == 6 || type == 12) {
                data = event.asCharacters().getData();
            } else if (type == 9) {
                data = ((EntityReference) event).getDeclaration().getReplacementText();
            } else if (type != 5 && type != 3) {
                if (type == 1) {
                    throw new XMLStreamException("elementGetText() function expects text only elment but START_ELEMENT was encountered.", event.getLocation());
                }
                if (type == 2) {
                    return "";
                }
            }
            StringBuffer buffer = new StringBuffer();
            if (data != null && data.length() > 0) {
                buffer.append(data);
            }
            XMLEvent xMLEventNextEvent = nextEvent();
            while (true) {
                XMLEvent event2 = xMLEventNextEvent;
                if (event2.getEventType() != 2) {
                    if (type == 4 || type == 6 || type == 12) {
                        data = event2.asCharacters().getData();
                    } else if (type == 9) {
                        data = ((EntityReference) event2).getDeclaration().getReplacementText();
                    } else if (type != 5 && type != 3) {
                        if (type == 8) {
                            throw new XMLStreamException("unexpected end of document when reading element text content");
                        }
                        if (type == 1) {
                            throw new XMLStreamException("elementGetText() function expects text only elment but START_ELEMENT was encountered.", event2.getLocation());
                        }
                        throw new XMLStreamException("Unexpected event type " + type, event2.getLocation());
                    }
                    if (data != null && data.length() > 0) {
                        buffer.append(data);
                    }
                    xMLEventNextEvent = nextEvent();
                } else {
                    return buffer.toString();
                }
            }
        } else {
            String data2 = this.fXMLReader.getElementText();
            this.fLastEvent = this.fXMLEventAllocator.allocate(this.fXMLReader);
            return data2;
        }
    }

    @Override // javax.xml.stream.XMLEventReader
    public Object getProperty(String name) throws IllegalArgumentException {
        return this.fXMLReader.getProperty(name);
    }

    @Override // javax.xml.stream.XMLEventReader
    public XMLEvent nextTag() throws XMLStreamException {
        if (this.fPeekedEvent != null) {
            XMLEvent event = this.fPeekedEvent;
            this.fPeekedEvent = null;
            int eventType = event.getEventType();
            if ((event.isCharacters() && event.asCharacters().isWhiteSpace()) || eventType == 3 || eventType == 5 || eventType == 7) {
                event = nextEvent();
                eventType = event.getEventType();
            }
            while (true) {
                if ((!event.isCharacters() || !event.asCharacters().isWhiteSpace()) && eventType != 3 && eventType != 5) {
                    break;
                }
                event = nextEvent();
                eventType = event.getEventType();
            }
            if (eventType != 1 && eventType != 2) {
                throw new XMLStreamException("expected start or end tag", event.getLocation());
            }
            return event;
        }
        this.fXMLReader.nextTag();
        XMLEvent xMLEventAllocate = this.fXMLEventAllocator.allocate(this.fXMLReader);
        this.fLastEvent = xMLEventAllocate;
        return xMLEventAllocate;
    }

    @Override // java.util.Iterator
    public Object next() {
        try {
            Object object = nextEvent();
            return object;
        } catch (XMLStreamException streamException) {
            this.fLastEvent = null;
            NoSuchElementException e2 = new NoSuchElementException(streamException.getMessage());
            e2.initCause(streamException.getCause());
            throw e2;
        }
    }

    @Override // javax.xml.stream.XMLEventReader
    public XMLEvent peek() throws XMLStreamException {
        if (this.fPeekedEvent != null) {
            return this.fPeekedEvent;
        }
        if (hasNext()) {
            this.fXMLReader.next();
            this.fPeekedEvent = this.fXMLEventAllocator.allocate(this.fXMLReader);
            return this.fPeekedEvent;
        }
        return null;
    }
}
