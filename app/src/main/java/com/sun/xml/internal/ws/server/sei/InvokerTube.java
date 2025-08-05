package com.sun.xml.internal.ws.server.sei;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.internal.ws.server.sei.Invoker;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/InvokerTube.class */
public abstract class InvokerTube<T extends Invoker> extends AbstractTubeImpl implements InvokerSource<T> {
    protected final T invoker;

    protected InvokerTube(T invoker) {
        this.invoker = invoker;
    }

    protected InvokerTube(InvokerTube<T> that, TubeCloner cloner) {
        cloner.add(that, this);
        this.invoker = that.invoker;
    }

    @NotNull
    public T getInvoker(Packet request) {
        return this.invoker;
    }
}
