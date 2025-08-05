package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.message.Packet;
import javax.xml.ws.WebServiceContext;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/WSWebServiceContext.class */
public interface WSWebServiceContext extends WebServiceContext {
    @Nullable
    Packet getRequestPacket();
}
