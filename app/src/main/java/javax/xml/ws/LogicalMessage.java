package javax.xml.ws;

import javax.xml.bind.JAXBContext;
import javax.xml.transform.Source;

/* loaded from: rt.jar:javax/xml/ws/LogicalMessage.class */
public interface LogicalMessage {
    Source getPayload();

    void setPayload(Source source);

    Object getPayload(JAXBContext jAXBContext);

    void setPayload(Object obj, JAXBContext jAXBContext);
}
