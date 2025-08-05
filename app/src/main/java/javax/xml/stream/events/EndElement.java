package javax.xml.stream.events;

import java.util.Iterator;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:javax/xml/stream/events/EndElement.class */
public interface EndElement extends XMLEvent {
    QName getName();

    Iterator getNamespaces();
}
