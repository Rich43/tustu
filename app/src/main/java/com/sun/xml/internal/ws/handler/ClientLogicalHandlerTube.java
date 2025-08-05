package com.sun.xml.internal.ws.handler;

import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.handler.HandlerProcessor;
import com.sun.xml.internal.ws.model.AbstractSEIModelImpl;
import com.sun.xml.internal.ws.spi.db.BindingContext;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.MessageContext;

/* loaded from: rt.jar:com/sun/xml/internal/ws/handler/ClientLogicalHandlerTube.class */
public class ClientLogicalHandlerTube extends HandlerTube {
    private SEIModel seiModel;

    public ClientLogicalHandlerTube(WSBinding binding, SEIModel seiModel, WSDLPort port, Tube next) {
        super(next, port, binding);
        this.seiModel = seiModel;
    }

    public ClientLogicalHandlerTube(WSBinding binding, SEIModel seiModel, Tube next, HandlerTube cousinTube) {
        super(next, cousinTube, binding);
        this.seiModel = seiModel;
    }

    private ClientLogicalHandlerTube(ClientLogicalHandlerTube that, TubeCloner cloner) {
        super(that, cloner);
        this.seiModel = that.seiModel;
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerTube
    protected void initiateClosing(MessageContext mc) {
        close(mc);
        super.initiateClosing(mc);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public AbstractFilterTubeImpl copy(TubeCloner cloner) {
        return new ClientLogicalHandlerTube(this, cloner);
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerTube
    void setUpProcessor() {
        if (this.handlers == null) {
            this.handlers = new ArrayList();
            WSBinding binding = getBinding();
            List<LogicalHandler> logicalSnapShot = ((BindingImpl) binding).getHandlerConfig().getLogicalHandlers();
            if (!logicalSnapShot.isEmpty()) {
                this.handlers.addAll(logicalSnapShot);
                if (binding.getSOAPVersion() == null) {
                    this.processor = new XMLHandlerProcessor(this, binding, this.handlers);
                } else {
                    this.processor = new SOAPHandlerProcessor(true, this, binding, this.handlers);
                }
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
            boolean handlerResult = this.processor.callHandlersRequest(HandlerProcessor.Direction.OUTBOUND, context, !isOneWay);
            if (!handlerResult) {
                this.remedyActionTaken = true;
            }
            return handlerResult;
        } catch (WebServiceException wse) {
            this.remedyActionTaken = true;
            throw wse;
        } catch (RuntimeException re) {
            this.remedyActionTaken = true;
            throw new WebServiceException(re);
        }
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerTube
    void callHandlersOnResponse(MessageUpdatableContext context, boolean handleFault) {
        try {
            this.processor.callHandlersResponse(HandlerProcessor.Direction.INBOUND, context, handleFault);
        } catch (WebServiceException wse) {
            throw wse;
        } catch (RuntimeException re) {
            throw new WebServiceException(re);
        }
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerTube
    void closeHandlers(MessageContext mc) {
        closeClientsideHandlers(mc);
    }
}
