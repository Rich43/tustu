package com.sun.xml.internal.ws.api.pipe;

import com.sun.xml.internal.ws.api.message.Packet;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/Pipe.class */
public interface Pipe {
    Packet process(Packet packet);

    void preDestroy();

    Pipe copy(PipeCloner pipeCloner);
}
