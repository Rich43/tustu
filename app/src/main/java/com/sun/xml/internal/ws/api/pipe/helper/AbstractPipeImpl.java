package com.sun.xml.internal.ws.api.pipe.helper;

import com.sun.xml.internal.ws.api.pipe.Pipe;
import com.sun.xml.internal.ws.api.pipe.PipeCloner;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/helper/AbstractPipeImpl.class */
public abstract class AbstractPipeImpl implements Pipe {
    protected AbstractPipeImpl() {
    }

    protected AbstractPipeImpl(Pipe that, PipeCloner cloner) {
        cloner.add(that, this);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Pipe
    public void preDestroy() {
    }
}
