package com.sun.xml.internal.ws.api.pipe;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.message.Packet;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/Tube.class */
public interface Tube {
    @NotNull
    NextAction processRequest(@NotNull Packet packet);

    @NotNull
    NextAction processResponse(@NotNull Packet packet);

    @NotNull
    NextAction processException(@NotNull Throwable th);

    void preDestroy();

    Tube copy(TubeCloner tubeCloner);
}
