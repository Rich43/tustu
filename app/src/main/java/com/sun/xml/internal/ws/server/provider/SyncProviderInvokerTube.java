package com.sun.xml.internal.ws.server.provider;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.NextAction;
import com.sun.xml.internal.ws.api.pipe.ThrowableContainerPropertySet;
import com.sun.xml.internal.ws.api.server.Invoker;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/provider/SyncProviderInvokerTube.class */
public class SyncProviderInvokerTube<T> extends ProviderInvokerTube<T> {
    private static final Logger LOGGER = Logger.getLogger("com.sun.xml.internal.ws.server.SyncProviderInvokerTube");

    public SyncProviderInvokerTube(Invoker invoker, ProviderArgumentsBuilder<T> argsBuilder) {
        super(invoker, argsBuilder);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.ws.api.pipe.Tube
    public NextAction processRequest(Packet packet) {
        WSDLPort port = getEndpoint().getPort();
        WSBinding binding = getEndpoint().getBinding();
        T parameter = this.argsBuilder.getParameter(packet);
        LOGGER.fine("Invoking Provider Endpoint");
        try {
            Object objInvokeProvider = getInvoker(packet).invokeProvider(packet, parameter);
            if (objInvokeProvider == null && packet.transportBackChannel != null) {
                packet.transportBackChannel.close();
            }
            Packet response = this.argsBuilder.getResponse(packet, (Packet) objInvokeProvider, port, binding);
            ThrowableContainerPropertySet throwableContainerPropertySet = (ThrowableContainerPropertySet) response.getSatellite(ThrowableContainerPropertySet.class);
            Throwable throwable = throwableContainerPropertySet != null ? throwableContainerPropertySet.getThrowable() : null;
            return throwable != null ? doThrow(response, throwable) : doReturnWith(response);
        } catch (Exception e2) {
            LOGGER.log(Level.SEVERE, e2.getMessage(), (Throwable) e2);
            return doReturnWith(this.argsBuilder.getResponse(packet, e2, port, binding));
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
