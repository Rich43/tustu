package com.sun.xml.internal.ws.server;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.api.server.WSWebServiceContext;
import java.security.Principal;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/AbstractWebServiceContext.class */
public abstract class AbstractWebServiceContext implements WSWebServiceContext {
    private final WSEndpoint endpoint;

    public AbstractWebServiceContext(@NotNull WSEndpoint endpoint) {
        this.endpoint = endpoint;
    }

    @Override // javax.xml.ws.WebServiceContext
    public MessageContext getMessageContext() {
        Packet packet = getRequestPacket();
        if (packet == null) {
            throw new IllegalStateException("getMessageContext() can only be called while servicing a request");
        }
        return new EndpointMessageContextImpl(packet);
    }

    @Override // javax.xml.ws.WebServiceContext
    public Principal getUserPrincipal() {
        Packet packet = getRequestPacket();
        if (packet == null) {
            throw new IllegalStateException("getUserPrincipal() can only be called while servicing a request");
        }
        return packet.webServiceContextDelegate.getUserPrincipal(packet);
    }

    @Override // javax.xml.ws.WebServiceContext
    public boolean isUserInRole(String role) {
        Packet packet = getRequestPacket();
        if (packet == null) {
            throw new IllegalStateException("isUserInRole() can only be called while servicing a request");
        }
        return packet.webServiceContextDelegate.isUserInRole(packet, role);
    }

    @Override // javax.xml.ws.WebServiceContext
    public EndpointReference getEndpointReference(Element... referenceParameters) {
        return getEndpointReference(W3CEndpointReference.class, referenceParameters);
    }

    @Override // javax.xml.ws.WebServiceContext
    public <T extends EndpointReference> T getEndpointReference(Class<T> clazz, Element... referenceParameters) {
        Packet packet = getRequestPacket();
        if (packet == null) {
            throw new IllegalStateException("getEndpointReference() can only be called while servicing a request");
        }
        String address = packet.webServiceContextDelegate.getEPRAddress(packet, this.endpoint);
        String wsdlAddress = null;
        if (this.endpoint.getServiceDefinition() != null) {
            wsdlAddress = packet.webServiceContextDelegate.getWSDLAddress(packet, this.endpoint);
        }
        return clazz.cast(this.endpoint.getEndpointReference(clazz, address, wsdlAddress, referenceParameters));
    }
}
