package com.sun.xml.internal.fastinfoset.stax.events;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/events/StAXFilteredEvent.class */
public class StAXFilteredEvent implements XMLEventReader {
    private XMLEventReader eventReader;
    private EventFilter _filter;

    public StAXFilteredEvent() {
    }

    public StAXFilteredEvent(XMLEventReader reader, EventFilter filter) throws XMLStreamException {
        this.eventReader = reader;
        this._filter = filter;
    }

    public void setEventReader(XMLEventReader reader) {
        this.eventReader = reader;
    }

    public void setFilter(EventFilter filter) {
        this._filter = filter;
    }

    @Override // java.util.Iterator
    public Object next() {
        try {
            return nextEvent();
        } catch (XMLStreamException e2) {
            return null;
        }
    }

    @Override // javax.xml.stream.XMLEventReader
    public XMLEvent nextEvent() throws XMLStreamException {
        if (hasNext()) {
            return this.eventReader.nextEvent();
        }
        return null;
    }

    @Override // javax.xml.stream.XMLEventReader
    public String getElementText() throws XMLStreamException {
        StringBuffer buffer = new StringBuffer();
        if (!nextEvent().isStartElement()) {
            throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.mustBeOnSTART_ELEMENT"));
        }
        while (hasNext()) {
            XMLEvent e2 = nextEvent();
            if (e2.isStartElement()) {
                throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.getElementTextExpectTextOnly"));
            }
            if (e2.isCharacters()) {
                buffer.append(((Characters) e2).getData());
            }
            if (e2.isEndElement()) {
                return buffer.toString();
            }
        }
        throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.END_ELEMENTnotFound"));
    }

    @Override // javax.xml.stream.XMLEventReader
    public XMLEvent nextTag() throws XMLStreamException {
        while (hasNext()) {
            XMLEvent e2 = nextEvent();
            if (e2.isStartElement() || e2.isEndElement()) {
                return e2;
            }
        }
        throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.startOrEndNotFound"));
    }

    @Override // javax.xml.stream.XMLEventReader, java.util.Iterator
    public boolean hasNext() {
        while (this.eventReader.hasNext()) {
            try {
                if (this._filter.accept(this.eventReader.peek())) {
                    return true;
                }
                this.eventReader.nextEvent();
            } catch (XMLStreamException e2) {
                return false;
            }
        }
        return false;
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override // javax.xml.stream.XMLEventReader
    public XMLEvent peek() throws XMLStreamException {
        if (hasNext()) {
            return this.eventReader.peek();
        }
        return null;
    }

    @Override // javax.xml.stream.XMLEventReader
    public void close() throws XMLStreamException {
        this.eventReader.close();
    }

    @Override // javax.xml.stream.XMLEventReader
    public Object getProperty(String name) {
        return this.eventReader.getProperty(name);
    }
}
