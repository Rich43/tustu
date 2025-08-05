package com.sun.xml.internal.ws.client.sei;

import com.oracle.webservices.internal.api.databinding.JavaCallInfo;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.client.RequestContext;
import com.sun.xml.internal.ws.client.ResponseContextReceiver;
import com.sun.xml.internal.ws.encoding.soap.DeserializationException;
import com.sun.xml.internal.ws.model.JavaMethodImpl;
import com.sun.xml.internal.ws.resources.DispatchMessages;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/SyncMethodHandler.class */
final class SyncMethodHandler extends MethodHandler {
    final boolean isVoid;
    final boolean isOneway;
    final JavaMethodImpl javaMethod;

    SyncMethodHandler(SEIStub owner, JavaMethodImpl jm) {
        super(owner, jm.getMethod());
        this.javaMethod = jm;
        this.isVoid = Void.TYPE.equals(jm.getMethod().getReturnType());
        this.isOneway = jm.getMEP().isOneWay();
    }

    @Override // com.sun.xml.internal.ws.client.sei.MethodHandler
    Object invoke(Object proxy, Object[] args) throws Throwable {
        return invoke(proxy, args, this.owner.requestContext, this.owner);
    }

    Object invoke(Object proxy, Object[] args, RequestContext rc, ResponseContextReceiver receiver) throws Throwable {
        JavaCallInfo call = this.owner.databinding.createJavaCallInfo(this.method, args);
        Packet req = (Packet) this.owner.databinding.serializeRequest(call);
        Packet reply = this.owner.doProcess(req, rc, receiver);
        Message msg = reply.getMessage();
        try {
            if (msg == null) {
                if (!this.isOneway || !this.isVoid) {
                    throw new WebServiceException(DispatchMessages.INVALID_RESPONSE());
                }
                return null;
            }
            try {
                try {
                    JavaCallInfo call2 = this.owner.databinding.deserializeResponse(reply, call);
                    if (call2.getException() != null) {
                        throw call2.getException();
                    }
                    Object returnValue = call2.getReturnValue();
                    if (reply.transportBackChannel != null) {
                        reply.transportBackChannel.close();
                    }
                    return returnValue;
                } catch (JAXBException e2) {
                    throw new DeserializationException(DispatchMessages.INVALID_RESPONSE_DESERIALIZATION(), e2);
                }
            } catch (XMLStreamException e3) {
                throw new DeserializationException(DispatchMessages.INVALID_RESPONSE_DESERIALIZATION(), e3);
            }
        } catch (Throwable th) {
            if (reply.transportBackChannel != null) {
                reply.transportBackChannel.close();
            }
            throw th;
        }
    }

    ValueGetterFactory getValueGetterFactory() {
        return ValueGetterFactory.SYNC;
    }
}
