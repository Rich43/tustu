package com.sun.xml.internal.ws.api.pipe.helper;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.NextAction;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/helper/AbstractFilterTubeImpl.class */
public abstract class AbstractFilterTubeImpl extends AbstractTubeImpl {
    protected final Tube next;

    protected AbstractFilterTubeImpl(Tube next) {
        this.next = next;
    }

    protected AbstractFilterTubeImpl(AbstractFilterTubeImpl that, TubeCloner cloner) {
        super(that, cloner);
        if (that.next != null) {
            this.next = cloner.copy(that.next);
        } else {
            this.next = null;
        }
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube
    @NotNull
    public NextAction processRequest(Packet request) {
        return doInvoke(this.next, request);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube
    @NotNull
    public NextAction processResponse(Packet response) {
        return doReturnWith(response);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube
    @NotNull
    public NextAction processException(Throwable t2) {
        return doThrow(t2);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube, com.sun.xml.internal.ws.api.pipe.Pipe
    public void preDestroy() {
        if (this.next != null) {
            this.next.preDestroy();
        }
    }
}
