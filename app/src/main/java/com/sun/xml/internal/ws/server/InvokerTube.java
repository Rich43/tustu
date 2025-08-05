package com.sun.xml.internal.ws.server;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.internal.ws.api.server.AsyncProviderCallback;
import com.sun.xml.internal.ws.api.server.Invoker;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.api.server.WSWebServiceContext;
import com.sun.xml.internal.ws.resources.ServerMessages;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/InvokerTube.class */
public abstract class InvokerTube<T> extends com.sun.xml.internal.ws.server.sei.InvokerTube<Invoker> implements EndpointAwareTube {
    private WSEndpoint endpoint;
    private static final ThreadLocal<Packet> packets = new ThreadLocal<>();
    private final Invoker wrapper;

    protected InvokerTube(Invoker invoker) {
        super(invoker);
        this.wrapper = new Invoker() { // from class: com.sun.xml.internal.ws.server.InvokerTube.2
            @Override // com.sun.xml.internal.ws.server.sei.Invoker
            public Object invoke(Packet p2, Method m2, Object... args) throws IllegalAccessException, InvocationTargetException {
                Packet old = set(p2);
                try {
                    Object objInvoke = ((Invoker) InvokerTube.this.invoker).invoke(p2, m2, args);
                    set(old);
                    return objInvoke;
                } catch (Throwable th) {
                    set(old);
                    throw th;
                }
            }

            @Override // com.sun.xml.internal.ws.api.server.Invoker
            public <T> T invokeProvider(Packet packet, T t2) throws IllegalAccessException, InvocationTargetException {
                Packet packet2 = set(packet);
                try {
                    T t3 = (T) ((Invoker) InvokerTube.this.invoker).invokeProvider(packet, t2);
                    set(packet2);
                    return t3;
                } catch (Throwable th) {
                    set(packet2);
                    throw th;
                }
            }

            @Override // com.sun.xml.internal.ws.api.server.Invoker
            public <T> void invokeAsyncProvider(Packet p2, T arg, AsyncProviderCallback cbak, WebServiceContext ctxt) throws IllegalAccessException, InvocationTargetException {
                Packet old = set(p2);
                try {
                    ((Invoker) InvokerTube.this.invoker).invokeAsyncProvider(p2, arg, cbak, ctxt);
                    set(old);
                } catch (Throwable th) {
                    set(old);
                    throw th;
                }
            }

            private Packet set(Packet p2) {
                Packet old = (Packet) InvokerTube.packets.get();
                InvokerTube.packets.set(p2);
                return old;
            }
        };
    }

    @Override // com.sun.xml.internal.ws.server.EndpointAwareTube
    public void setEndpoint(WSEndpoint endpoint) {
        this.endpoint = endpoint;
        WSWebServiceContext webServiceContext = new AbstractWebServiceContext(endpoint) { // from class: com.sun.xml.internal.ws.server.InvokerTube.1
            @Override // com.sun.xml.internal.ws.api.server.WSWebServiceContext
            @Nullable
            public Packet getRequestPacket() {
                Packet p2 = (Packet) InvokerTube.packets.get();
                return p2;
            }
        };
        ((Invoker) this.invoker).start(webServiceContext, endpoint);
    }

    protected WSEndpoint getEndpoint() {
        return this.endpoint;
    }

    @Override // com.sun.xml.internal.ws.server.sei.InvokerTube, com.sun.xml.internal.ws.server.sei.InvokerSource
    @NotNull
    public final Invoker getInvoker(Packet request) {
        return this.wrapper;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public final AbstractTubeImpl copy(TubeCloner cloner) {
        cloner.add(this, this);
        return this;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube, com.sun.xml.internal.ws.api.pipe.Pipe
    public void preDestroy() {
        ((Invoker) this.invoker).dispose();
    }

    @NotNull
    public static Packet getCurrentPacket() {
        Packet packet = packets.get();
        if (packet == null) {
            throw new WebServiceException(ServerMessages.NO_CURRENT_PACKET());
        }
        return packet;
    }
}
