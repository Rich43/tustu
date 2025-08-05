package com.sun.xml.internal.ws.client.dispatch;

import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.client.WSPortInfo;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.client.WSServiceDelegate;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/dispatch/MessageDispatch.class */
public class MessageDispatch extends DispatchImpl<Message> {
    @Deprecated
    public MessageDispatch(QName port, WSServiceDelegate service, Tube pipe, BindingImpl binding, WSEndpointReference epr) {
        super(port, Service.Mode.MESSAGE, service, pipe, binding, epr);
    }

    public MessageDispatch(WSPortInfo portInfo, BindingImpl binding, WSEndpointReference epr) {
        super(portInfo, Service.Mode.MESSAGE, binding, epr, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.internal.ws.client.dispatch.DispatchImpl
    public Message toReturnValue(Packet response) {
        return response.getMessage();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.xml.internal.ws.client.dispatch.DispatchImpl
    public Packet createPacket(Message msg) {
        return new Packet(msg);
    }
}
