package com.sun.xml.internal.ws.handler;

import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.handler.HandlerProcessor;
import com.sun.xml.internal.ws.message.DataHandlerAttachment;
import com.sun.xml.internal.ws.model.AbstractSEIModelImpl;
import com.sun.xml.internal.ws.spi.db.BindingContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.activation.DataHandler;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.MessageContext;

/* loaded from: rt.jar:com/sun/xml/internal/ws/handler/ServerLogicalHandlerTube.class */
public class ServerLogicalHandlerTube extends HandlerTube {
    private SEIModel seiModel;

    public ServerLogicalHandlerTube(WSBinding binding, SEIModel seiModel, WSDLPort port, Tube next) {
        super(next, port, binding);
        this.seiModel = seiModel;
        setUpHandlersOnce();
    }

    public ServerLogicalHandlerTube(WSBinding binding, SEIModel seiModel, Tube next, HandlerTube cousinTube) {
        super(next, cousinTube, binding);
        this.seiModel = seiModel;
        setUpHandlersOnce();
    }

    private ServerLogicalHandlerTube(ServerLogicalHandlerTube that, TubeCloner cloner) {
        super(that, cloner);
        this.seiModel = that.seiModel;
        this.handlers = that.handlers;
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerTube
    protected void initiateClosing(MessageContext mc) {
        if (getBinding().getSOAPVersion() != null) {
            super.initiateClosing(mc);
        } else {
            close(mc);
            super.initiateClosing(mc);
        }
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public AbstractFilterTubeImpl copy(TubeCloner cloner) {
        return new ServerLogicalHandlerTube(this, cloner);
    }

    private void setUpHandlersOnce() {
        this.handlers = new ArrayList();
        List<LogicalHandler> logicalSnapShot = ((BindingImpl) getBinding()).getHandlerConfig().getLogicalHandlers();
        if (!logicalSnapShot.isEmpty()) {
            this.handlers.addAll(logicalSnapShot);
        }
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerTube
    protected void resetProcessor() {
        this.processor = null;
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerTube
    void setUpProcessor() {
        if (!this.handlers.isEmpty() && this.processor == null) {
            if (getBinding().getSOAPVersion() == null) {
                this.processor = new XMLHandlerProcessor(this, getBinding(), this.handlers);
            } else {
                this.processor = new SOAPHandlerProcessor(false, this, getBinding(), this.handlers);
            }
        }
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerTube
    MessageUpdatableContext getContext(Packet packet) {
        return new LogicalMessageContextImpl(getBinding(), getBindingContext(), packet);
    }

    private BindingContext getBindingContext() {
        if (this.seiModel == null || !(this.seiModel instanceof AbstractSEIModelImpl)) {
            return null;
        }
        return ((AbstractSEIModelImpl) this.seiModel).getBindingContext();
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
    void callHandlersOnResponse(MessageUpdatableContext context, boolean handleFault) {
        Map<String, DataHandler> atts = (Map) context.get(MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS);
        AttachmentSet attSet = context.packet.getMessage().getAttachments();
        for (Map.Entry<String, DataHandler> entry : atts.entrySet()) {
            String cid = entry.getKey();
            Attachment att = new DataHandlerAttachment(cid, atts.get(cid));
            attSet.add(att);
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
    void closeHandlers(MessageContext mc) {
        closeServersideHandlers(mc);
    }
}
