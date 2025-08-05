package com.sun.xml.internal.stream;

import java.util.NoSuchElementException;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.EventReaderDelegate;

/* loaded from: rt.jar:com/sun/xml/internal/stream/EventFilterSupport.class */
public class EventFilterSupport extends EventReaderDelegate {
    EventFilter fEventFilter;

    public EventFilterSupport(XMLEventReader eventReader, EventFilter eventFilter) {
        setParent(eventReader);
        this.fEventFilter = eventFilter;
    }

    @Override // javax.xml.stream.util.EventReaderDelegate, java.util.Iterator
    public Object next() {
        try {
            return nextEvent();
        } catch (XMLStreamException e2) {
            throw new NoSuchElementException();
        }
    }

    @Override // javax.xml.stream.util.EventReaderDelegate, javax.xml.stream.XMLEventReader, java.util.Iterator
    public boolean hasNext() {
        try {
            return peek() != null;
        } catch (XMLStreamException e2) {
            return false;
        }
    }

    @Override // javax.xml.stream.util.EventReaderDelegate, javax.xml.stream.XMLEventReader
    public XMLEvent nextEvent() throws XMLStreamException {
        if (super.hasNext()) {
            XMLEvent event = super.nextEvent();
            if (this.fEventFilter.accept(event)) {
                return event;
            }
            return nextEvent();
        }
        throw new NoSuchElementException();
    }

    @Override // javax.xml.stream.util.EventReaderDelegate, javax.xml.stream.XMLEventReader
    public XMLEvent nextTag() throws XMLStreamException {
        if (super.hasNext()) {
            XMLEvent event = super.nextTag();
            if (this.fEventFilter.accept(event)) {
                return event;
            }
            return nextTag();
        }
        throw new NoSuchElementException();
    }

    @Override // javax.xml.stream.util.EventReaderDelegate, javax.xml.stream.XMLEventReader
    public XMLEvent peek() throws XMLStreamException {
        while (true) {
            XMLEvent event = super.peek();
            if (event == null) {
                return null;
            }
            if (this.fEventFilter.accept(event)) {
                return event;
            }
            super.next();
        }
    }
}
