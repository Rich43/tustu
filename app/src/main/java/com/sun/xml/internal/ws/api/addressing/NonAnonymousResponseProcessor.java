package com.sun.xml.internal.ws.api.addressing;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.WSService;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.internal.ws.api.pipe.Fiber;
import com.sun.xml.internal.ws.api.pipe.TransportTubeFactory;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.binding.BindingImpl;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/addressing/NonAnonymousResponseProcessor.class */
public class NonAnonymousResponseProcessor {
    private static final NonAnonymousResponseProcessor DEFAULT = new NonAnonymousResponseProcessor();

    public static NonAnonymousResponseProcessor getDefault() {
        return DEFAULT;
    }

    protected NonAnonymousResponseProcessor() {
    }

    public Packet process(Packet packet) {
        final Fiber.CompletionCallback currentFiberCallback;
        Fiber.CompletionCallback fiberCallback = null;
        Fiber currentFiber = Fiber.getCurrentIfSet();
        if (currentFiber != null && (currentFiberCallback = currentFiber.getCompletionCallback()) != null) {
            fiberCallback = new Fiber.CompletionCallback() { // from class: com.sun.xml.internal.ws.api.addressing.NonAnonymousResponseProcessor.1
                @Override // com.sun.xml.internal.ws.api.pipe.Fiber.CompletionCallback
                public void onCompletion(@NotNull Packet response) {
                    currentFiberCallback.onCompletion(response);
                }

                @Override // com.sun.xml.internal.ws.api.pipe.Fiber.CompletionCallback
                public void onCompletion(@NotNull Throwable error) {
                    currentFiberCallback.onCompletion(error);
                }
            };
            currentFiber.setCompletionCallback(null);
        }
        WSEndpoint<?> endpoint = packet.endpoint;
        WSBinding binding = endpoint.getBinding();
        Tube transport = TransportTubeFactory.create(Thread.currentThread().getContextClassLoader(), new ClientTubeAssemblerContext(packet.endpointAddress, endpoint.getPort(), (WSService) null, binding, endpoint.getContainer(), ((BindingImpl) binding).createCodec(), (SEIModel) null, (Class) null));
        Fiber fiber = endpoint.getEngine().createFiber();
        fiber.start(transport, packet, fiberCallback);
        Packet copy = packet.copy(false);
        copy.endpointAddress = null;
        return copy;
    }
}
