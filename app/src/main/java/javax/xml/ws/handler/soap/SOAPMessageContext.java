package javax.xml.ws.handler.soap;

import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;

/* loaded from: rt.jar:javax/xml/ws/handler/soap/SOAPMessageContext.class */
public interface SOAPMessageContext extends MessageContext {
    SOAPMessage getMessage();

    void setMessage(SOAPMessage sOAPMessage);

    Object[] getHeaders(QName qName, JAXBContext jAXBContext, boolean z2);

    Set<String> getRoles();
}
