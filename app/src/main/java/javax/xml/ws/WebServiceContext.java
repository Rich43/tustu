package javax.xml.ws;

import java.security.Principal;
import javax.xml.ws.handler.MessageContext;
import org.w3c.dom.Element;

/* loaded from: rt.jar:javax/xml/ws/WebServiceContext.class */
public interface WebServiceContext {
    MessageContext getMessageContext();

    Principal getUserPrincipal();

    boolean isUserInRole(String str);

    EndpointReference getEndpointReference(Element... elementArr);

    <T extends EndpointReference> T getEndpointReference(Class<T> cls, Element... elementArr);
}
