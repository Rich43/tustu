package com.sun.xml.internal.ws.api.pipe;

import com.sun.istack.internal.NotNull;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/PipelineAssembler.class */
public interface PipelineAssembler {
    @NotNull
    Pipe createClient(@NotNull ClientPipeAssemblerContext clientPipeAssemblerContext);

    @NotNull
    Pipe createServer(@NotNull ServerPipeAssemblerContext serverPipeAssemblerContext);
}
