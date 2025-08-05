package javax.xml.stream.util;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/* loaded from: rt.jar:javax/xml/stream/util/XMLEventConsumer.class */
public interface XMLEventConsumer {
    void add(XMLEvent xMLEvent) throws XMLStreamException;
}
