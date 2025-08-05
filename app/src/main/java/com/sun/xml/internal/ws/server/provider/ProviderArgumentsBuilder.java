package com.sun.xml.internal.ws.server.provider;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
import com.sun.xml.internal.ws.model.CheckedExceptionImpl;
import javax.xml.ws.soap.SOAPBinding;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/provider/ProviderArgumentsBuilder.class */
public abstract class ProviderArgumentsBuilder<T> {
    protected abstract Message getResponseMessage(Exception exc);

    public abstract T getParameter(Packet packet);

    protected abstract Message getResponseMessage(T t2);

    protected Packet getResponse(Packet request, Exception e2, WSDLPort port, WSBinding binding) {
        Message message = getResponseMessage(e2);
        Packet response = request.createServerResponse(message, port, (SEIModel) null, binding);
        return response;
    }

    protected Packet getResponse(Packet request, @Nullable T returnValue, WSDLPort port, WSBinding binding) {
        Message message = null;
        if (returnValue != null) {
            message = getResponseMessage((ProviderArgumentsBuilder<T>) returnValue);
        }
        Packet response = request.createServerResponse(message, port, (SEIModel) null, binding);
        return response;
    }

    public static ProviderArgumentsBuilder<?> create(ProviderEndpointModel model, WSBinding binding) {
        if (model.datatype == Packet.class) {
            return new PacketProviderArgumentsBuilder(binding.getSOAPVersion());
        }
        return binding instanceof SOAPBinding ? SOAPProviderArgumentBuilder.create(model, binding.getSOAPVersion()) : XMLProviderArgumentBuilder.createBuilder(model, binding);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/provider/ProviderArgumentsBuilder$PacketProviderArgumentsBuilder.class */
    private static class PacketProviderArgumentsBuilder extends ProviderArgumentsBuilder<Packet> {
        private final SOAPVersion soapVersion;

        public PacketProviderArgumentsBuilder(SOAPVersion soapVersion) {
            this.soapVersion = soapVersion;
        }

        @Override // com.sun.xml.internal.ws.server.provider.ProviderArgumentsBuilder
        protected Message getResponseMessage(Exception e2) {
            return SOAPFaultBuilder.createSOAPFaultMessage(this.soapVersion, (CheckedExceptionImpl) null, e2);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sun.xml.internal.ws.server.provider.ProviderArgumentsBuilder
        public Packet getParameter(Packet packet) {
            return packet;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.xml.internal.ws.server.provider.ProviderArgumentsBuilder
        public Message getResponseMessage(Packet returnValue) {
            throw new IllegalStateException();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.xml.internal.ws.server.provider.ProviderArgumentsBuilder
        public Packet getResponse(Packet request, @Nullable Packet returnValue, WSDLPort port, WSBinding binding) {
            return returnValue;
        }
    }
}
