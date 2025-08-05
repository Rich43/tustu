package com.sun.xml.internal.ws.handler;

import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Header;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.message.saaj.SAAJFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/* loaded from: rt.jar:com/sun/xml/internal/ws/handler/SOAPMessageContextImpl.class */
public class SOAPMessageContextImpl extends MessageUpdatableContext implements SOAPMessageContext {
    private Set<String> roles;
    private SOAPMessage soapMsg;
    private WSBinding binding;

    public SOAPMessageContextImpl(WSBinding binding, Packet packet, Set<String> roles) {
        super(packet);
        this.soapMsg = null;
        this.binding = binding;
        this.roles = roles;
    }

    @Override // javax.xml.ws.handler.soap.SOAPMessageContext
    public SOAPMessage getMessage() {
        if (this.soapMsg == null) {
            try {
                Message m2 = this.packet.getMessage();
                this.soapMsg = m2 != null ? m2.readAsSOAPMessage() : null;
            } catch (SOAPException e2) {
                throw new WebServiceException(e2);
            }
        }
        return this.soapMsg;
    }

    @Override // javax.xml.ws.handler.soap.SOAPMessageContext
    public void setMessage(SOAPMessage soapMsg) {
        try {
            this.soapMsg = soapMsg;
        } catch (Exception e2) {
            throw new WebServiceException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.handler.MessageUpdatableContext
    void setPacketMessage(Message newMessage) {
        if (newMessage != null) {
            this.packet.setMessage(newMessage);
            this.soapMsg = null;
        }
    }

    @Override // com.sun.xml.internal.ws.handler.MessageUpdatableContext
    protected void updateMessage() {
        if (this.soapMsg != null) {
            this.packet.setMessage(SAAJFactory.create(this.soapMsg));
            this.soapMsg = null;
        }
    }

    @Override // javax.xml.ws.handler.soap.SOAPMessageContext
    public Object[] getHeaders(QName header, JAXBContext jaxbContext, boolean allRoles) {
        SOAPVersion soapVersion = this.binding.getSOAPVersion();
        List<Object> beanList = new ArrayList<>();
        try {
            Iterator<Header> itr = this.packet.getMessage().getHeaders().getHeaders(header, false);
            if (allRoles) {
                while (itr.hasNext()) {
                    beanList.add(itr.next().readAsJAXB(jaxbContext.createUnmarshaller()));
                }
            } else {
                while (itr.hasNext()) {
                    Header soapHeader = itr.next();
                    String role = soapHeader.getRole(soapVersion);
                    if (getRoles().contains(role)) {
                        beanList.add(soapHeader.readAsJAXB(jaxbContext.createUnmarshaller()));
                    }
                }
            }
            return beanList.toArray();
        } catch (Exception e2) {
            throw new WebServiceException(e2);
        }
    }

    @Override // javax.xml.ws.handler.soap.SOAPMessageContext
    public Set<String> getRoles() {
        return this.roles;
    }
}
