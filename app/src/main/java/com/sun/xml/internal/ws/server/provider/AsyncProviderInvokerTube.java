package com.sun.xml.internal.ws.server.provider;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.Fiber;
import com.sun.xml.internal.ws.api.pipe.NextAction;
import com.sun.xml.internal.ws.api.pipe.ThrowableContainerPropertySet;
import com.sun.xml.internal.ws.api.server.AsyncProviderCallback;
import com.sun.xml.internal.ws.api.server.Invoker;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.server.AbstractWebServiceContext;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/provider/AsyncProviderInvokerTube.class */
public class AsyncProviderInvokerTube<T> extends ProviderInvokerTube<T> {
    private static final Logger LOGGER = Logger.getLogger("com.sun.xml.internal.ws.server.AsyncProviderInvokerTube");

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/provider/AsyncProviderInvokerTube$Resumer.class */
    private interface Resumer {
        void onResume(Packet packet);
    }

    public AsyncProviderInvokerTube(Invoker invoker, ProviderArgumentsBuilder<T> argsBuilder) {
        super(invoker, argsBuilder);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube
    @NotNull
    public NextAction processRequest(@NotNull Packet request) {
        T param = this.argsBuilder.getParameter(request);
        AsyncProviderInvokerTube<T>.NoSuspendResumer resumer = new NoSuspendResumer();
        AsyncProviderInvokerTube<T>.AsyncProviderCallbackImpl callback = new AsyncProviderCallbackImpl(request, resumer);
        AsyncProviderInvokerTube<T>.AsyncWebServiceContext ctxt = new AsyncWebServiceContext(getEndpoint(), request);
        LOGGER.fine("Invoking AsyncProvider Endpoint");
        try {
            getInvoker(request).invokeAsyncProvider(request, param, callback, ctxt);
            synchronized (callback) {
                if (resumer.response != null) {
                    ThrowableContainerPropertySet tc = (ThrowableContainerPropertySet) resumer.response.getSatellite(ThrowableContainerPropertySet.class);
                    Throwable t2 = tc != null ? tc.getThrowable() : null;
                    return t2 != null ? doThrow(resumer.response, t2) : doReturnWith(resumer.response);
                }
                ((AsyncProviderCallbackImpl) callback).resumer = new FiberResumer();
                return doSuspend();
            }
        } catch (Throwable e2) {
            LOGGER.log(Level.SEVERE, e2.getMessage(), e2);
            return doThrow(e2);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/provider/AsyncProviderInvokerTube$FiberResumer.class */
    public class FiberResumer implements Resumer {
        private final Fiber fiber = Fiber.current();

        public FiberResumer() {
        }

        @Override // com.sun.xml.internal.ws.server.provider.AsyncProviderInvokerTube.Resumer
        public void onResume(Packet response) {
            ThrowableContainerPropertySet tc = (ThrowableContainerPropertySet) response.getSatellite(ThrowableContainerPropertySet.class);
            Throwable t2 = tc != null ? tc.getThrowable() : null;
            this.fiber.resume(t2, response);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/provider/AsyncProviderInvokerTube$NoSuspendResumer.class */
    private class NoSuspendResumer implements Resumer {
        protected Packet response;

        private NoSuspendResumer() {
            this.response = null;
        }

        @Override // com.sun.xml.internal.ws.server.provider.AsyncProviderInvokerTube.Resumer
        public void onResume(Packet response) {
            this.response = response;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/provider/AsyncProviderInvokerTube$AsyncProviderCallbackImpl.class */
    public class AsyncProviderCallbackImpl implements AsyncProviderCallback<T> {
        private final Packet request;
        private Resumer resumer;

        public AsyncProviderCallbackImpl(Packet request, Resumer resumer) {
            this.request = request;
            this.resumer = resumer;
        }

        @Override // com.sun.xml.internal.ws.api.server.AsyncProviderCallback
        public void send(@Nullable T param) {
            if (param == null && this.request.transportBackChannel != null) {
                this.request.transportBackChannel.close();
            }
            Packet packet = AsyncProviderInvokerTube.this.argsBuilder.getResponse(this.request, (Packet) param, AsyncProviderInvokerTube.this.getEndpoint().getPort(), AsyncProviderInvokerTube.this.getEndpoint().getBinding());
            synchronized (this) {
                this.resumer.onResume(packet);
            }
        }

        @Override // com.sun.xml.internal.ws.api.server.AsyncProviderCallback
        public void sendError(@NotNull Throwable t2) {
            Exception e2;
            if (t2 instanceof Exception) {
                e2 = (Exception) t2;
            } else {
                e2 = new RuntimeException(t2);
            }
            Packet packet = AsyncProviderInvokerTube.this.argsBuilder.getResponse(this.request, e2, AsyncProviderInvokerTube.this.getEndpoint().getPort(), AsyncProviderInvokerTube.this.getEndpoint().getBinding());
            synchronized (this) {
                this.resumer.onResume(packet);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/provider/AsyncProviderInvokerTube$AsyncWebServiceContext.class */
    public class AsyncWebServiceContext extends AbstractWebServiceContext {
        final Packet packet;

        public AsyncWebServiceContext(WSEndpoint endpoint, Packet packet) {
            super(endpoint);
            this.packet = packet;
        }

        @Override // com.sun.xml.internal.ws.api.server.WSWebServiceContext
        @NotNull
        public Packet getRequestPacket() {
            return this.packet;
        }
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube
    @NotNull
    public NextAction processResponse(@NotNull Packet response) {
        return doReturnWith(response);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube
    @NotNull
    public NextAction processException(@NotNull Throwable t2) {
        return doThrow(t2);
    }
}
