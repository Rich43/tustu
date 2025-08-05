package com.sun.xml.internal.ws.api.handler;

import com.sun.xml.internal.ws.api.handler.MessageHandlerContext;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.Handler;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/handler/MessageHandler.class */
public interface MessageHandler<C extends MessageHandlerContext> extends Handler<C> {
    Set<QName> getHeaders();
}
