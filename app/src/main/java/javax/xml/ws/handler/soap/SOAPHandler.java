package javax.xml.ws.handler.soap;

import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/* loaded from: rt.jar:javax/xml/ws/handler/soap/SOAPHandler.class */
public interface SOAPHandler<T extends SOAPMessageContext> extends Handler<T> {
    Set<QName> getHeaders();
}
