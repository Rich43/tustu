package com.sun.xml.internal.ws.api.pipe.helper;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.Fiber;
import com.sun.xml.internal.ws.api.pipe.NextAction;
import com.sun.xml.internal.ws.api.pipe.Pipe;
import com.sun.xml.internal.ws.api.pipe.PipeCloner;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/helper/PipeAdapter.class */
public class PipeAdapter extends AbstractTubeImpl {
    private final Pipe next;

    public static Tube adapt(Pipe p2) {
        if (p2 instanceof Tube) {
            return (Tube) p2;
        }
        return new PipeAdapter(p2);
    }

    public static Pipe adapt(Tube p2) {
        if (p2 instanceof Pipe) {
            return (Pipe) p2;
        }
        return new C1TubeAdapter(p2);
    }

    /* renamed from: com.sun.xml.internal.ws.api.pipe.helper.PipeAdapter$1TubeAdapter, reason: invalid class name */
    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/helper/PipeAdapter$1TubeAdapter.class */
    class C1TubeAdapter extends AbstractPipeImpl {

        /* renamed from: t, reason: collision with root package name */
        private final Tube f12084t;

        public C1TubeAdapter(Tube t2) {
            this.f12084t = t2;
        }

        private C1TubeAdapter(C1TubeAdapter that, PipeCloner cloner) {
            super(that, cloner);
            this.f12084t = cloner.copy((PipeCloner) that.f12084t);
        }

        @Override // com.sun.xml.internal.ws.api.pipe.Pipe
        public Packet process(Packet request) {
            return Fiber.current().runSync(this.f12084t, request);
        }

        @Override // com.sun.xml.internal.ws.api.pipe.Pipe
        public Pipe copy(PipeCloner cloner) {
            return new C1TubeAdapter(this, cloner);
        }
    }

    private PipeAdapter(Pipe next) {
        this.next = next;
    }

    private PipeAdapter(PipeAdapter that, TubeCloner cloner) {
        super(that, cloner);
        this.next = ((PipeCloner) cloner).copy((PipeCloner) that.next);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube
    @NotNull
    public NextAction processRequest(@NotNull Packet p2) {
        return doReturnWith(this.next.process(p2));
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube
    @NotNull
    public NextAction processResponse(@NotNull Packet p2) {
        throw new IllegalStateException();
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube
    @NotNull
    public NextAction processException(@NotNull Throwable t2) {
        throw new IllegalStateException();
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube, com.sun.xml.internal.ws.api.pipe.Pipe
    public void preDestroy() {
        this.next.preDestroy();
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public PipeAdapter copy(TubeCloner cloner) {
        return new PipeAdapter(this, cloner);
    }

    public String toString() {
        return super.toString() + "[" + this.next.toString() + "]";
    }
}
