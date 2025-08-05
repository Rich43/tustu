package javax.xml.bind;

import javax.xml.namespace.QName;

/* loaded from: rt.jar:javax/xml/bind/JAXBIntrospector.class */
public abstract class JAXBIntrospector {
    public abstract boolean isElement(Object obj);

    public abstract QName getElementName(Object obj);

    public static Object getValue(Object jaxbElement) {
        if (jaxbElement instanceof JAXBElement) {
            return ((JAXBElement) jaxbElement).getValue();
        }
        return jaxbElement;
    }
}
