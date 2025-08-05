package javax.xml.stream.events;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:javax/xml/stream/events/StartElement.class */
public interface StartElement extends XMLEvent {
    QName getName();

    Iterator getAttributes();

    Iterator getNamespaces();

    Attribute getAttributeByName(QName qName);

    NamespaceContext getNamespaceContext();

    String getNamespaceURI(String str);
}
