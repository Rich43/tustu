package javax.xml.ws;

import java.util.List;
import javax.xml.ws.handler.Handler;

/* loaded from: rt.jar:javax/xml/ws/Binding.class */
public interface Binding {
    List<Handler> getHandlerChain();

    void setHandlerChain(List<Handler> list);

    String getBindingID();
}
