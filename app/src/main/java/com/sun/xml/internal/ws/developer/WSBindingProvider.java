package com.sun.xml.internal.ws.developer;

import com.sun.istack.internal.NotNull;
import com.sun.org.glassfish.gmbal.ManagedObjectManager;
import com.sun.xml.internal.ws.api.ComponentRegistry;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.client.WSPortInfo;
import com.sun.xml.internal.ws.api.message.Header;
import java.io.Closeable;
import java.util.List;
import javax.xml.ws.BindingProvider;

/* loaded from: rt.jar:com/sun/xml/internal/ws/developer/WSBindingProvider.class */
public interface WSBindingProvider extends BindingProvider, Closeable, ComponentRegistry {
    void setOutboundHeaders(List<Header> list);

    void setOutboundHeaders(Header... headerArr);

    void setOutboundHeaders(Object... objArr);

    List<Header> getInboundHeaders();

    void setAddress(String str);

    WSEndpointReference getWSEndpointReference();

    WSPortInfo getPortInfo();

    @NotNull
    ManagedObjectManager getManagedObjectManager();
}
