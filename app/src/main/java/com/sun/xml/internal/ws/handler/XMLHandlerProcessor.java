package com.sun.xml.internal.ws.handler;

import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Messages;
import com.sun.xml.internal.ws.handler.MessageUpdatableContext;
import java.util.List;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/handler/XMLHandlerProcessor.class */
final class XMLHandlerProcessor<C extends MessageUpdatableContext> extends HandlerProcessor<C> {
    public XMLHandlerProcessor(HandlerTube owner, WSBinding binding, List<? extends Handler> chain) {
        super(owner, binding, chain);
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerProcessor
    final void insertFaultMessage(C context, ProtocolException exception) {
        if (exception instanceof HTTPException) {
            context.put(MessageContext.HTTP_RESPONSE_CODE, Integer.valueOf(((HTTPException) exception).getStatusCode()));
        }
        if (context != null) {
            context.setPacketMessage(Messages.createEmpty(this.binding.getSOAPVersion()));
        }
    }
}
