package com.sun.xml.internal.ws.api.pipe.helper;

import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.Pipe;
import com.sun.xml.internal.ws.api.pipe.PipeCloner;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/helper/AbstractFilterPipeImpl.class */
public abstract class AbstractFilterPipeImpl extends AbstractPipeImpl {
    protected final Pipe next;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AbstractFilterPipeImpl.class.desiredAssertionStatus();
    }

    protected AbstractFilterPipeImpl(Pipe next) {
        this.next = next;
        if (!$assertionsDisabled && next == null) {
            throw new AssertionError();
        }
    }

    protected AbstractFilterPipeImpl(AbstractFilterPipeImpl that, PipeCloner cloner) {
        super(that, cloner);
        this.next = cloner.copy((PipeCloner) that.next);
        if (!$assertionsDisabled && this.next == null) {
            throw new AssertionError();
        }
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Pipe
    public Packet process(Packet packet) {
        return this.next.process(packet);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractPipeImpl, com.sun.xml.internal.ws.api.pipe.Pipe
    public void preDestroy() {
        this.next.preDestroy();
    }
}
