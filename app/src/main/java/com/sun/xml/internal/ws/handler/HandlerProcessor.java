package com.sun.xml.internal.ws.handler;

import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.handler.MessageUpdatableContext;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;

/* loaded from: rt.jar:com/sun/xml/internal/ws/handler/HandlerProcessor.class */
abstract class HandlerProcessor<C extends MessageUpdatableContext> {
    boolean isClient;
    static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.handler");
    private List<? extends Handler> handlers;
    WSBinding binding;
    private int index = -1;
    private HandlerTube owner;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/handler/HandlerProcessor$Direction.class */
    public enum Direction {
        OUTBOUND,
        INBOUND
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/handler/HandlerProcessor$RequestOrResponse.class */
    public enum RequestOrResponse {
        REQUEST,
        RESPONSE
    }

    abstract void insertFaultMessage(C c2, ProtocolException protocolException);

    protected HandlerProcessor(HandlerTube owner, WSBinding binding, List<? extends Handler> chain) {
        this.owner = owner;
        this.handlers = chain == null ? new ArrayList() : chain;
        this.binding = binding;
    }

    int getIndex() {
        return this.index;
    }

    void setIndex(int i2) {
        this.index = i2;
    }

    public boolean callHandlersRequest(Direction direction, C context, boolean responseExpected) {
        boolean result;
        setDirection(direction, context);
        try {
            if (direction == Direction.OUTBOUND) {
                result = callHandleMessage(context, 0, this.handlers.size() - 1);
            } else {
                result = callHandleMessage(context, this.handlers.size() - 1, 0);
            }
            if (!result) {
                if (responseExpected) {
                    reverseDirection(direction, context);
                    if (direction == Direction.OUTBOUND) {
                        callHandleMessageReverse(context, getIndex() - 1, 0);
                        return false;
                    }
                    callHandleMessageReverse(context, getIndex() + 1, this.handlers.size() - 1);
                    return false;
                }
                setHandleFalseProperty();
                return false;
            }
            return result;
        } catch (ProtocolException pe) {
            logger.log(Level.FINER, "exception in handler chain", (Throwable) pe);
            if (responseExpected) {
                insertFaultMessage(context, pe);
                reverseDirection(direction, context);
                setHandleFaultProperty();
                if (direction == Direction.OUTBOUND) {
                    callHandleFault(context, getIndex() - 1, 0);
                    return false;
                }
                callHandleFault(context, getIndex() + 1, this.handlers.size() - 1);
                return false;
            }
            throw pe;
        } catch (RuntimeException re) {
            logger.log(Level.FINER, "exception in handler chain", (Throwable) re);
            throw re;
        }
    }

    public void callHandlersResponse(Direction direction, C context, boolean isFault) {
        setDirection(direction, context);
        try {
            if (isFault) {
                if (direction == Direction.OUTBOUND) {
                    callHandleFault(context, 0, this.handlers.size() - 1);
                } else {
                    callHandleFault(context, this.handlers.size() - 1, 0);
                }
            } else if (direction == Direction.OUTBOUND) {
                callHandleMessageReverse(context, 0, this.handlers.size() - 1);
            } else {
                callHandleMessageReverse(context, this.handlers.size() - 1, 0);
            }
        } catch (RuntimeException re) {
            logger.log(Level.FINER, "exception in handler chain", (Throwable) re);
            throw re;
        }
    }

    private void reverseDirection(Direction origDirection, C context) {
        if (origDirection == Direction.OUTBOUND) {
            context.put(MessageContext.MESSAGE_OUTBOUND_PROPERTY, false);
        } else {
            context.put(MessageContext.MESSAGE_OUTBOUND_PROPERTY, true);
        }
    }

    private void setDirection(Direction direction, C context) {
        if (direction == Direction.OUTBOUND) {
            context.put(MessageContext.MESSAGE_OUTBOUND_PROPERTY, true);
        } else {
            context.put(MessageContext.MESSAGE_OUTBOUND_PROPERTY, false);
        }
    }

    private void setHandleFaultProperty() {
        this.owner.setHandleFault();
    }

    private void setHandleFalseProperty() {
        this.owner.setHandleFalse();
    }

    private boolean callHandleMessage(C context, int start, int end) {
        int i2 = start;
        try {
            if (start > end) {
                while (i2 >= end) {
                    if (!this.handlers.get(i2).handleMessage(context)) {
                        setIndex(i2);
                        return false;
                    }
                    i2--;
                }
            } else {
                while (i2 <= end) {
                    if (!this.handlers.get(i2).handleMessage(context)) {
                        setIndex(i2);
                        return false;
                    }
                    i2++;
                }
            }
            return true;
        } catch (RuntimeException e2) {
            setIndex(i2);
            throw e2;
        }
    }

    private boolean callHandleMessageReverse(C context, int start, int end) {
        if (this.handlers.isEmpty() || start == -1 || start == this.handlers.size()) {
            return false;
        }
        int i2 = start;
        if (start > end) {
            while (i2 >= end) {
                if (!this.handlers.get(i2).handleMessage(context)) {
                    setHandleFalseProperty();
                    return false;
                }
                i2--;
            }
            return true;
        }
        while (i2 <= end) {
            if (!this.handlers.get(i2).handleMessage(context)) {
                setHandleFalseProperty();
                return false;
            }
            i2++;
        }
        return true;
    }

    private boolean callHandleFault(C context, int start, int end) {
        if (this.handlers.isEmpty() || start == -1 || start == this.handlers.size()) {
            return false;
        }
        int i2 = start;
        if (start > end) {
            while (i2 >= end) {
                try {
                    if (!this.handlers.get(i2).handleFault(context)) {
                        return false;
                    }
                    i2--;
                } catch (RuntimeException re) {
                    logger.log(Level.FINER, "exception in handler chain", (Throwable) re);
                    throw re;
                }
            }
            return true;
        }
        while (i2 <= end) {
            try {
                if (!this.handlers.get(i2).handleFault(context)) {
                    return false;
                }
                i2++;
            } catch (RuntimeException re2) {
                logger.log(Level.FINER, "exception in handler chain", (Throwable) re2);
                throw re2;
            }
        }
        return true;
    }

    void closeHandlers(MessageContext context, int start, int end) {
        if (this.handlers.isEmpty() || start == -1) {
            return;
        }
        if (start > end) {
            for (int i2 = start; i2 >= end; i2--) {
                try {
                    this.handlers.get(i2).close(context);
                } catch (RuntimeException re) {
                    logger.log(Level.INFO, "Exception ignored during close", (Throwable) re);
                }
            }
            return;
        }
        for (int i3 = start; i3 <= end; i3++) {
            try {
                this.handlers.get(i3).close(context);
            } catch (RuntimeException re2) {
                logger.log(Level.INFO, "Exception ignored during close", (Throwable) re2);
            }
        }
    }
}
