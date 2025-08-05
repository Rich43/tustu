package com.sun.xml.internal.ws.client.dispatch;

import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.client.WSPortInfo;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Messages;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.client.WSServiceDelegate;
import com.sun.xml.internal.ws.encoding.xml.XMLMessage;
import com.sun.xml.internal.ws.message.source.PayloadSourceMessage;
import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Service;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/dispatch/RESTSourceDispatch.class */
final class RESTSourceDispatch extends DispatchImpl<Source> {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !RESTSourceDispatch.class.desiredAssertionStatus();
    }

    @Deprecated
    public RESTSourceDispatch(QName port, Service.Mode mode, WSServiceDelegate owner, Tube pipe, BindingImpl binding, WSEndpointReference epr) {
        super(port, mode, owner, pipe, binding, epr);
        if (!$assertionsDisabled && !isXMLHttp(binding)) {
            throw new AssertionError();
        }
    }

    public RESTSourceDispatch(WSPortInfo portInfo, Service.Mode mode, BindingImpl binding, WSEndpointReference epr) {
        super(portInfo, mode, binding, epr);
        if (!$assertionsDisabled && !isXMLHttp(binding)) {
            throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.internal.ws.client.dispatch.DispatchImpl
    public Source toReturnValue(Packet response) {
        Message msg = response.getMessage();
        try {
            return new StreamSource(XMLMessage.getDataSource(msg, this.binding.getFeatures()).getInputStream());
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.xml.internal.ws.client.dispatch.DispatchImpl
    public Packet createPacket(Source msg) {
        Message message;
        if (msg == null) {
            message = Messages.createEmpty(this.soapVersion);
        } else {
            message = new PayloadSourceMessage(null, msg, setOutboundAttachments(), this.soapVersion);
        }
        return new Packet(message);
    }
}
