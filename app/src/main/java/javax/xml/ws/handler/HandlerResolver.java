package javax.xml.ws.handler;

import java.util.List;

/* loaded from: rt.jar:javax/xml/ws/handler/HandlerResolver.class */
public interface HandlerResolver {
    List<Handler> getHandlerChain(PortInfo portInfo);
}
