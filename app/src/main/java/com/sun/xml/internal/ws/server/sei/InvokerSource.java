package com.sun.xml.internal.ws.server.sei;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.server.sei.Invoker;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/InvokerSource.class */
public interface InvokerSource<T extends Invoker> {
    @NotNull
    T getInvoker(Packet packet);
}
