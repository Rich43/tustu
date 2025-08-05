package com.sun.xml.internal.ws.handler;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.Fiber;
import com.sun.xml.internal.ws.api.pipe.NextAction;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.client.HandlerConfiguration;
import java.util.List;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;

/* loaded from: rt.jar:com/sun/xml/internal/ws/handler/HandlerTube.class */
public abstract class HandlerTube extends AbstractFilterTubeImpl {
    HandlerTube cousinTube;
    protected List<Handler> handlers;
    HandlerProcessor processor;
    boolean remedyActionTaken;

    @Nullable
    protected final WSDLPort port;
    boolean requestProcessingSucessful;
    private WSBinding binding;
    private HandlerConfiguration hc;
    private HandlerTubeExchange exchange;

    abstract void closeHandlers(MessageContext messageContext);

    abstract void callHandlersOnResponse(MessageUpdatableContext messageUpdatableContext, boolean z2);

    abstract boolean callHandlersOnRequest(MessageUpdatableContext messageUpdatableContext, boolean z2);

    abstract void setUpProcessor();

    abstract MessageUpdatableContext getContext(Packet packet);

    public HandlerTube(Tube next, WSDLPort port, WSBinding binding) {
        super(next);
        this.remedyActionTaken = false;
        this.requestProcessingSucessful = false;
        this.port = port;
        this.binding = binding;
    }

    public HandlerTube(Tube next, HandlerTube cousinTube, WSBinding binding) {
        super(next);
        this.remedyActionTaken = false;
        this.requestProcessingSucessful = false;
        this.cousinTube = cousinTube;
        this.binding = binding;
        if (cousinTube != null) {
            this.port = cousinTube.port;
        } else {
            this.port = null;
        }
    }

    protected HandlerTube(HandlerTube that, TubeCloner cloner) {
        super(that, cloner);
        this.remedyActionTaken = false;
        this.requestProcessingSucessful = false;
        if (that.cousinTube != null) {
            this.cousinTube = (HandlerTube) cloner.copy(that.cousinTube);
        }
        this.port = that.port;
        this.binding = that.binding;
    }

    protected WSBinding getBinding() {
        return this.binding;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public NextAction processRequest(Packet request) {
        setupExchange();
        if (isHandleFalse()) {
            this.remedyActionTaken = true;
            return doInvoke(this.next, request);
        }
        setUpProcessorInternal();
        MessageUpdatableContext context = getContext(request);
        boolean isOneWay = checkOneWay(request);
        try {
            try {
                if (!isHandlerChainEmpty()) {
                    boolean handlerResult = callHandlersOnRequest(context, isOneWay);
                    context.updatePacket();
                    if (!isOneWay && !handlerResult) {
                        NextAction nextActionDoReturnWith = doReturnWith(request);
                        if (!this.requestProcessingSucessful) {
                            initiateClosing(context.getMessageContext());
                        }
                        return nextActionDoReturnWith;
                    }
                }
                this.requestProcessingSucessful = true;
                NextAction nextActionDoInvoke = doInvoke(this.next, request);
                if (!this.requestProcessingSucessful) {
                    initiateClosing(context.getMessageContext());
                }
                return nextActionDoInvoke;
            } catch (RuntimeException re) {
                if (!isOneWay) {
                    throw re;
                }
                if (request.transportBackChannel != null) {
                    request.transportBackChannel.close();
                }
                request.setMessage(null);
                NextAction nextActionDoReturnWith2 = doReturnWith(request);
                if (!this.requestProcessingSucessful) {
                    initiateClosing(context.getMessageContext());
                }
                return nextActionDoReturnWith2;
            }
        } catch (Throwable th) {
            if (!this.requestProcessingSucessful) {
                initiateClosing(context.getMessageContext());
            }
            throw th;
        }
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public NextAction processResponse(Packet response) {
        setupExchange();
        MessageUpdatableContext context = getContext(response);
        try {
            if (isHandleFalse() || response.getMessage() == null) {
                NextAction nextActionDoReturnWith = doReturnWith(response);
                initiateClosing(context.getMessageContext());
                return nextActionDoReturnWith;
            }
            setUpProcessorInternal();
            boolean isFault = isHandleFault(response);
            if (!isHandlerChainEmpty()) {
                callHandlersOnResponse(context, isFault);
            }
            context.updatePacket();
            return doReturnWith(response);
        } finally {
            initiateClosing(context.getMessageContext());
        }
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public NextAction processException(Throwable t2) {
        try {
            NextAction nextActionDoThrow = doThrow(t2);
            Packet packet = Fiber.current().getPacket();
            MessageUpdatableContext context = getContext(packet);
            initiateClosing(context.getMessageContext());
            return nextActionDoThrow;
        } catch (Throwable th) {
            Packet packet2 = Fiber.current().getPacket();
            MessageUpdatableContext context2 = getContext(packet2);
            initiateClosing(context2.getMessageContext());
            throw th;
        }
    }

    protected void initiateClosing(MessageContext mc) {
    }

    public final void close(MessageContext msgContext) {
        if (this.requestProcessingSucessful && this.cousinTube != null) {
            this.cousinTube.close(msgContext);
        }
        if (this.processor != null) {
            closeHandlers(msgContext);
        }
        this.exchange = null;
        this.requestProcessingSucessful = false;
    }

    protected void closeClientsideHandlers(MessageContext msgContext) {
        if (this.processor == null) {
            return;
        }
        if (this.remedyActionTaken) {
            this.processor.closeHandlers(msgContext, this.processor.getIndex(), 0);
            this.processor.setIndex(-1);
            this.remedyActionTaken = false;
            return;
        }
        this.processor.closeHandlers(msgContext, this.handlers.size() - 1, 0);
    }

    protected void closeServersideHandlers(MessageContext msgContext) {
        if (this.processor == null) {
            return;
        }
        if (this.remedyActionTaken) {
            this.processor.closeHandlers(msgContext, this.processor.getIndex(), this.handlers.size() - 1);
            this.processor.setIndex(-1);
            this.remedyActionTaken = false;
            return;
        }
        this.processor.closeHandlers(msgContext, 0, this.handlers.size() - 1);
    }

    private boolean checkOneWay(Packet packet) {
        if (this.port != null) {
            return packet.getMessage().isOneWay(this.port);
        }
        return packet.expectReply == null || !packet.expectReply.booleanValue();
    }

    private void setUpProcessorInternal() {
        HandlerConfiguration hc = ((BindingImpl) this.binding).getHandlerConfig();
        if (hc != this.hc) {
            resetProcessor();
        }
        this.hc = hc;
        setUpProcessor();
    }

    protected void resetProcessor() {
        this.handlers = null;
    }

    public final boolean isHandlerChainEmpty() {
        return this.handlers.isEmpty();
    }

    private boolean isHandleFault(Packet packet) {
        if (this.cousinTube != null) {
            return this.exchange.isHandleFault();
        }
        boolean isFault = packet.getMessage().isFault();
        this.exchange.setHandleFault(isFault);
        return isFault;
    }

    final void setHandleFault() {
        this.exchange.setHandleFault(true);
    }

    private boolean isHandleFalse() {
        return this.exchange.isHandleFalse();
    }

    final void setHandleFalse() {
        this.exchange.setHandleFalse();
    }

    private void setupExchange() {
        if (this.exchange == null) {
            this.exchange = new HandlerTubeExchange();
            if (this.cousinTube != null) {
                this.cousinTube.exchange = this.exchange;
                return;
            }
            return;
        }
        if (this.cousinTube != null) {
            this.cousinTube.exchange = this.exchange;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/handler/HandlerTube$HandlerTubeExchange.class */
    static final class HandlerTubeExchange {
        private boolean handleFalse;
        private boolean handleFault;

        HandlerTubeExchange() {
        }

        boolean isHandleFault() {
            return this.handleFault;
        }

        void setHandleFault(boolean isFault) {
            this.handleFault = isFault;
        }

        public boolean isHandleFalse() {
            return this.handleFalse;
        }

        void setHandleFalse() {
            this.handleFalse = true;
        }
    }
}
