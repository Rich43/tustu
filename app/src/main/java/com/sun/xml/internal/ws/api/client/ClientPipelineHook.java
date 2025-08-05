package com.sun.xml.internal.ws.api.client;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.pipe.ClientPipeAssemblerContext;
import com.sun.xml.internal.ws.api.pipe.Pipe;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/client/ClientPipelineHook.class */
public abstract class ClientPipelineHook {
    @NotNull
    public Pipe createSecurityPipe(ClientPipeAssemblerContext ctxt, @NotNull Pipe tail) {
        return tail;
    }
}
