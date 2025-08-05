package com.sun.xml.internal.ws.binding;

import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.client.HandlerConfiguration;
import com.sun.xml.internal.ws.resources.ClientMessages;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.http.HTTPBinding;

/* loaded from: rt.jar:com/sun/xml/internal/ws/binding/HTTPBindingImpl.class */
public class HTTPBindingImpl extends BindingImpl implements HTTPBinding {
    HTTPBindingImpl() {
        this(EMPTY_FEATURES);
    }

    HTTPBindingImpl(WebServiceFeature... features) {
        super(BindingID.XML_HTTP, features);
    }

    @Override // javax.xml.ws.Binding
    public void setHandlerChain(List<Handler> chain) {
        for (Handler handler : chain) {
            if (!(handler instanceof LogicalHandler)) {
                throw new WebServiceException(ClientMessages.NON_LOGICAL_HANDLER_SET(handler.getClass()));
            }
        }
        setHandlerConfig(new HandlerConfiguration((Set<String>) Collections.emptySet(), chain));
    }
}
