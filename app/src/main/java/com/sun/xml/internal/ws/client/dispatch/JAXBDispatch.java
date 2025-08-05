package com.sun.xml.internal.ws.client.dispatch;

import com.sun.xml.internal.bind.api.JAXBRIContext;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.client.WSPortInfo;
import com.sun.xml.internal.ws.api.message.Header;
import com.sun.xml.internal.ws.api.message.Headers;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Messages;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.client.WSServiceDelegate;
import com.sun.xml.internal.ws.message.jaxb.JAXBDispatchMessage;
import com.sun.xml.internal.ws.spi.db.BindingContextFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/dispatch/JAXBDispatch.class */
public class JAXBDispatch extends DispatchImpl<Object> {
    private final JAXBContext jaxbcontext;
    private final boolean isContextSupported;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !JAXBDispatch.class.desiredAssertionStatus();
    }

    @Deprecated
    public JAXBDispatch(QName port, JAXBContext jc, Service.Mode mode, WSServiceDelegate service, Tube pipe, BindingImpl binding, WSEndpointReference epr) {
        super(port, mode, service, pipe, binding, epr);
        this.jaxbcontext = jc;
        this.isContextSupported = BindingContextFactory.isContextSupported(jc);
    }

    public JAXBDispatch(WSPortInfo portInfo, JAXBContext jc, Service.Mode mode, BindingImpl binding, WSEndpointReference epr) {
        super(portInfo, mode, binding, epr);
        this.jaxbcontext = jc;
        this.isContextSupported = BindingContextFactory.isContextSupported(jc);
    }

    @Override // com.sun.xml.internal.ws.client.dispatch.DispatchImpl
    Object toReturnValue(Packet response) {
        try {
            Unmarshaller unmarshaller = this.jaxbcontext.createUnmarshaller();
            Message msg = response.getMessage();
            switch (this.mode) {
                case PAYLOAD:
                    return msg.readPayloadAsJAXB(unmarshaller);
                case MESSAGE:
                    Source result = msg.readEnvelopeAsSource();
                    return unmarshaller.unmarshal(result);
                default:
                    throw new WebServiceException("Unrecognized dispatch mode");
            }
        } catch (JAXBException e2) {
            throw new WebServiceException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.client.dispatch.DispatchImpl
    Packet createPacket(Object msg) {
        Message messageCreateRaw;
        Message message;
        if (!$assertionsDisabled && this.jaxbcontext == null) {
            throw new AssertionError();
        }
        if (this.mode == Service.Mode.MESSAGE) {
            message = this.isContextSupported ? new JAXBDispatchMessage(BindingContextFactory.create(this.jaxbcontext), msg, this.soapVersion) : new JAXBDispatchMessage(this.jaxbcontext, msg, this.soapVersion);
        } else if (msg == null) {
            message = Messages.createEmpty(this.soapVersion);
        } else {
            if (this.isContextSupported) {
                messageCreateRaw = Messages.create(this.jaxbcontext, msg, this.soapVersion);
            } else {
                messageCreateRaw = Messages.createRaw(this.jaxbcontext, msg, this.soapVersion);
            }
            message = messageCreateRaw;
        }
        return new Packet(message);
    }

    @Override // com.sun.xml.internal.ws.client.dispatch.DispatchImpl, com.sun.xml.internal.ws.developer.WSBindingProvider
    public void setOutboundHeaders(Object... headers) {
        if (headers == null) {
            throw new IllegalArgumentException();
        }
        Header[] hl = new Header[headers.length];
        for (int i2 = 0; i2 < hl.length; i2++) {
            if (headers[i2] == null) {
                throw new IllegalArgumentException();
            }
            hl[i2] = Headers.create((JAXBRIContext) this.jaxbcontext, headers[i2]);
        }
        super.setOutboundHeaders(hl);
    }
}
