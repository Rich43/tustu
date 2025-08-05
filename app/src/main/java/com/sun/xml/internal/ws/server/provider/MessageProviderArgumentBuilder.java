package com.sun.xml.internal.ws.server.provider;

import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
import com.sun.xml.internal.ws.model.CheckedExceptionImpl;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/provider/MessageProviderArgumentBuilder.class */
final class MessageProviderArgumentBuilder extends ProviderArgumentsBuilder<Message> {
    private final SOAPVersion soapVersion;

    public MessageProviderArgumentBuilder(SOAPVersion soapVersion) {
        this.soapVersion = soapVersion;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.internal.ws.server.provider.ProviderArgumentsBuilder
    public Message getParameter(Packet packet) {
        return packet.getMessage();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.xml.internal.ws.server.provider.ProviderArgumentsBuilder
    public Message getResponseMessage(Message returnValue) {
        return returnValue;
    }

    @Override // com.sun.xml.internal.ws.server.provider.ProviderArgumentsBuilder
    protected Message getResponseMessage(Exception e2) {
        return SOAPFaultBuilder.createSOAPFaultMessage(this.soapVersion, (CheckedExceptionImpl) null, e2);
    }
}
