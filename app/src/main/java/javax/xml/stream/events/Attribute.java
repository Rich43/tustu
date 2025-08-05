package javax.xml.stream.events;

import javax.xml.namespace.QName;

/* loaded from: rt.jar:javax/xml/stream/events/Attribute.class */
public interface Attribute extends XMLEvent {
    QName getName();

    String getValue();

    String getDTDType();

    boolean isSpecified();
}
