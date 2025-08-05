package javax.xml.ws.handler;

import javax.xml.ws.handler.MessageContext;

/* loaded from: rt.jar:javax/xml/ws/handler/Handler.class */
public interface Handler<C extends MessageContext> {
    boolean handleMessage(C c2);

    boolean handleFault(C c2);

    void close(MessageContext messageContext);
}
