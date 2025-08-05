package com.sun.xml.internal.ws.handler;

import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.handler.MessageHandler;
import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.client.HandlerConfiguration;
import com.sun.xml.internal.ws.handler.HandlerProcessor;
import com.sun.xml.internal.ws.message.DataHandlerAttachment;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.activation.DataHandler;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;

/* loaded from: rt.jar:com/sun/xml/internal/ws/handler/ServerMessageHandlerTube.class */
public class ServerMessageHandlerTube extends HandlerTube {
    private SEIModel seiModel;
    private Set<String> roles;

    public ServerMessageHandlerTube(SEIModel seiModel, WSBinding binding, Tube next, HandlerTube cousinTube) {
        super(next, cousinTube, binding);
        this.seiModel = seiModel;
        setUpHandlersOnce();
    }

    private ServerMessageHandlerTube(ServerMessageHandlerTube that, TubeCloner cloner) {
        super(that, cloner);
        this.seiModel = that.seiModel;
        this.handlers = that.handlers;
        this.roles = that.roles;
    }

    private void setUpHandlersOnce() {
        this.handlers = new ArrayList();
        HandlerConfiguration handlerConfig = ((BindingImpl) getBinding()).getHandlerConfig();
        List<MessageHandler> msgHandlersSnapShot = handlerConfig.getMessageHandlers();
        if (!msgHandlersSnapShot.isEmpty()) {
            this.handlers.addAll(msgHandlersSnapShot);
            this.roles = new HashSet();
            this.roles.addAll(handlerConfig.getRoles());
        }
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerTube
    void callHandlersOnResponse(MessageUpdatableContext context, boolean handleFault) {
        Map<String, DataHandler> atts = (Map) context.get(MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS);
        AttachmentSet attSet = context.packet.getMessage().getAttachments();
        for (Map.Entry<String, DataHandler> entry : atts.entrySet()) {
            String cid = entry.getKey();
            if (attSet.get(cid) == null) {
                Attachment att = new DataHandlerAttachment(cid, atts.get(cid));
                attSet.add(att);
            }
        }
        try {
            this.processor.callHandlersResponse(HandlerProcessor.Direction.OUTBOUND, context, handleFault);
        } catch (WebServiceException wse) {
            throw wse;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerTube
    boolean callHandlersOnRequest(MessageUpdatableContext context, boolean isOneWay) {
        try {
            boolean handlerResult = this.processor.callHandlersRequest(HandlerProcessor.Direction.INBOUND, context, !isOneWay);
            if (!handlerResult) {
                this.remedyActionTaken = true;
            }
            return handlerResult;
        } catch (RuntimeException re) {
            this.remedyActionTaken = true;
            throw re;
        }
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerTube
    protected void resetProcessor() {
        this.processor = null;
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerTube
    void setUpProcessor() {
        if (!this.handlers.isEmpty() && this.processor == null) {
            this.processor = new SOAPHandlerProcessor(false, this, getBinding(), this.handlers);
        }
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerTube
    void closeHandlers(MessageContext mc) {
        closeServersideHandlers(mc);
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerTube
    MessageUpdatableContext getContext(Packet packet) {
        MessageHandlerContextImpl context = new MessageHandlerContextImpl(this.seiModel, getBinding(), this.port, packet, this.roles);
        return context;
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerTube
    protected void initiateClosing(MessageContext mc) {
        close(mc);
        super.initiateClosing(mc);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public AbstractFilterTubeImpl copy(TubeCloner cloner) {
        return new ServerMessageHandlerTube(this, cloner);
    }
}
