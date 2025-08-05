package com.sun.xml.internal.ws.client;

import com.sun.xml.internal.ws.api.handler.MessageHandler;
import com.sun.xml.internal.ws.handler.HandlerException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.soap.SOAPHandler;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/HandlerConfiguration.class */
public class HandlerConfiguration {
    private final Set<String> roles;
    private final List<Handler> handlerChain;
    private final List<LogicalHandler> logicalHandlers;
    private final List<SOAPHandler> soapHandlers;
    private final List<MessageHandler> messageHandlers;
    private final Set<QName> handlerKnownHeaders;

    public HandlerConfiguration(Set<String> roles, List<Handler> handlerChain) {
        this.roles = roles;
        this.handlerChain = handlerChain;
        this.logicalHandlers = new ArrayList();
        this.soapHandlers = new ArrayList();
        this.messageHandlers = new ArrayList();
        Set<QName> modHandlerKnownHeaders = new HashSet<>();
        for (Handler handler : handlerChain) {
            if (handler instanceof LogicalHandler) {
                this.logicalHandlers.add((LogicalHandler) handler);
            } else if (handler instanceof SOAPHandler) {
                this.soapHandlers.add((SOAPHandler) handler);
                Set<QName> headers = ((SOAPHandler) handler).getHeaders();
                if (headers != null) {
                    modHandlerKnownHeaders.addAll(headers);
                }
            } else if (handler instanceof MessageHandler) {
                this.messageHandlers.add((MessageHandler) handler);
                Set<QName> headers2 = ((MessageHandler) handler).getHeaders();
                if (headers2 != null) {
                    modHandlerKnownHeaders.addAll(headers2);
                }
            } else {
                throw new HandlerException("handler.not.valid.type", handler.getClass());
            }
        }
        this.handlerKnownHeaders = Collections.unmodifiableSet(modHandlerKnownHeaders);
    }

    public HandlerConfiguration(Set<String> roles, HandlerConfiguration oldConfig) {
        this.roles = roles;
        this.handlerChain = oldConfig.handlerChain;
        this.logicalHandlers = oldConfig.logicalHandlers;
        this.soapHandlers = oldConfig.soapHandlers;
        this.messageHandlers = oldConfig.messageHandlers;
        this.handlerKnownHeaders = oldConfig.handlerKnownHeaders;
    }

    public Set<String> getRoles() {
        return this.roles;
    }

    public List<Handler> getHandlerChain() {
        if (this.handlerChain == null) {
            return Collections.emptyList();
        }
        return new ArrayList(this.handlerChain);
    }

    public List<LogicalHandler> getLogicalHandlers() {
        return this.logicalHandlers;
    }

    public List<SOAPHandler> getSoapHandlers() {
        return this.soapHandlers;
    }

    public List<MessageHandler> getMessageHandlers() {
        return this.messageHandlers;
    }

    public Set<QName> getHandlerKnownHeaders() {
        return this.handlerKnownHeaders;
    }
}
