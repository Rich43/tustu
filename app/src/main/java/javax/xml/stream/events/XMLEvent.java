package javax.xml.stream.events;

import java.io.Writer;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

/* loaded from: rt.jar:javax/xml/stream/events/XMLEvent.class */
public interface XMLEvent extends XMLStreamConstants {
    int getEventType();

    Location getLocation();

    boolean isStartElement();

    boolean isAttribute();

    boolean isNamespace();

    boolean isEndElement();

    boolean isEntityReference();

    boolean isProcessingInstruction();

    boolean isCharacters();

    boolean isStartDocument();

    boolean isEndDocument();

    StartElement asStartElement();

    EndElement asEndElement();

    Characters asCharacters();

    QName getSchemaType();

    void writeAsEncodedUnicode(Writer writer) throws XMLStreamException;
}
