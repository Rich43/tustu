package com.sun.xml.internal.ws.transport;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.EndpointAddress;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.internal.ws.api.pipe.NextAction;
import com.sun.xml.internal.ws.api.pipe.TransportTubeFactory;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.internal.ws.developer.HttpConfigFeature;
import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/transport/DeferredTransportPipe.class */
public final class DeferredTransportPipe extends AbstractTubeImpl {
    private Tube transport;
    private EndpointAddress address;
    private final ClassLoader classLoader;
    private final ClientTubeAssemblerContext context;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DeferredTransportPipe.class.desiredAssertionStatus();
    }

    public DeferredTransportPipe(ClassLoader classLoader, ClientTubeAssemblerContext context) {
        this.classLoader = classLoader;
        this.context = context;
        if (context.getBinding().getFeature(HttpConfigFeature.class) == null) {
            context.getBinding().getFeatures().mergeFeatures(new WebServiceFeature[]{new HttpConfigFeature()}, false);
        }
        try {
            this.transport = TransportTubeFactory.create(classLoader, context);
            this.address = context.getAddress();
        } catch (Exception e2) {
        }
    }

    public DeferredTransportPipe(DeferredTransportPipe that, TubeCloner cloner) {
        super(that, cloner);
        this.classLoader = that.classLoader;
        this.context = that.context;
        if (that.transport != null) {
            this.transport = cloner.copy(that.transport);
            this.address = that.address;
        }
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube
    public NextAction processException(@NotNull Throwable t2) {
        return this.transport.processException(t2);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube
    public NextAction processRequest(@NotNull Packet request) {
        if (request.endpointAddress == this.address) {
            return this.transport.processRequest(request);
        }
        if (this.transport != null) {
            this.transport.preDestroy();
            this.transport = null;
            this.address = null;
        }
        ClientTubeAssemblerContext newContext = new ClientTubeAssemblerContext(request.endpointAddress, this.context.getWsdlModel(), this.context.getBindingProvider(), this.context.getBinding(), this.context.getContainer(), this.context.getCodec().copy(), this.context.getSEIModel(), this.context.getSEI());
        this.address = request.endpointAddress;
        this.transport = TransportTubeFactory.create(this.classLoader, newContext);
        if ($assertionsDisabled || this.transport != null) {
            return this.transport.processRequest(request);
        }
        throw new AssertionError();
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube
    public NextAction processResponse(@NotNull Packet response) {
        if (this.transport != null) {
            return this.transport.processResponse(response);
        }
        return doReturnWith(response);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube, com.sun.xml.internal.ws.api.pipe.Pipe
    public void preDestroy() {
        if (this.transport != null) {
            this.transport.preDestroy();
            this.transport = null;
            this.address = null;
        }
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public DeferredTransportPipe copy(TubeCloner cloner) {
        return new DeferredTransportPipe(this, cloner);
    }
}
