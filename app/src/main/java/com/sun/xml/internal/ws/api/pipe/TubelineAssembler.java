package com.sun.xml.internal.ws.api.pipe;

import com.sun.istack.internal.NotNull;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/TubelineAssembler.class */
public interface TubelineAssembler {
    @NotNull
    Tube createClient(@NotNull ClientTubeAssemblerContext clientTubeAssemblerContext);

    @NotNull
    Tube createServer(@NotNull ServerTubeAssemblerContext serverTubeAssemblerContext);
}
