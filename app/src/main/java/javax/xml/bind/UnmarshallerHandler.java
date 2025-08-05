package javax.xml.bind;

import org.xml.sax.ContentHandler;

/* loaded from: rt.jar:javax/xml/bind/UnmarshallerHandler.class */
public interface UnmarshallerHandler extends ContentHandler {
    Object getResult() throws IllegalStateException, JAXBException;
}
