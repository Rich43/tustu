package com.sun.xml.internal.ws.server.provider;

import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Messages;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.encoding.xml.XMLMessage;
import com.sun.xml.internal.ws.resources.ServerMessages;
import javax.activation.DataSource;
import javax.xml.transform.Source;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/provider/XMLProviderArgumentBuilder.class */
abstract class XMLProviderArgumentBuilder<T> extends ProviderArgumentsBuilder<T> {
    XMLProviderArgumentBuilder() {
    }

    @Override // com.sun.xml.internal.ws.server.provider.ProviderArgumentsBuilder
    protected Packet getResponse(Packet request, Exception e2, WSDLPort port, WSBinding binding) {
        Packet response = super.getResponse(request, e2, port, binding);
        if ((e2 instanceof HTTPException) && response.supports(MessageContext.HTTP_RESPONSE_CODE)) {
            response.put(MessageContext.HTTP_RESPONSE_CODE, Integer.valueOf(((HTTPException) e2).getStatusCode()));
        }
        return response;
    }

    static XMLProviderArgumentBuilder createBuilder(ProviderEndpointModel model, WSBinding binding) {
        if (model.mode == Service.Mode.PAYLOAD) {
            return new PayloadSource();
        }
        if (model.datatype == Source.class) {
            return new PayloadSource();
        }
        if (model.datatype == DataSource.class) {
            return new DataSourceParameter(binding);
        }
        throw new WebServiceException(ServerMessages.PROVIDER_INVALID_PARAMETER_TYPE(model.implClass, model.datatype));
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/provider/XMLProviderArgumentBuilder$PayloadSource.class */
    private static final class PayloadSource extends XMLProviderArgumentBuilder<Source> {
        private PayloadSource() {
        }

        @Override // com.sun.xml.internal.ws.server.provider.ProviderArgumentsBuilder
        public Source getParameter(Packet packet) {
            return packet.getMessage().readPayloadAsSource();
        }

        @Override // com.sun.xml.internal.ws.server.provider.ProviderArgumentsBuilder
        public Message getResponseMessage(Source source) {
            return Messages.createUsingPayload(source, SOAPVersion.SOAP_11);
        }

        @Override // com.sun.xml.internal.ws.server.provider.ProviderArgumentsBuilder
        protected Message getResponseMessage(Exception e2) {
            return XMLMessage.create(e2);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/provider/XMLProviderArgumentBuilder$DataSourceParameter.class */
    private static final class DataSourceParameter extends XMLProviderArgumentBuilder<DataSource> {
        private final WSBinding binding;

        DataSourceParameter(WSBinding binding) {
            this.binding = binding;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.internal.ws.server.provider.ProviderArgumentsBuilder
        public DataSource getParameter(Packet packet) {
            Message internalMessage = packet.getInternalMessage();
            if (internalMessage instanceof XMLMessage.MessageDataSource) {
                return ((XMLMessage.MessageDataSource) internalMessage).getDataSource();
            }
            return XMLMessage.getDataSource(internalMessage, this.binding.getFeatures());
        }

        @Override // com.sun.xml.internal.ws.server.provider.ProviderArgumentsBuilder
        public Message getResponseMessage(DataSource ds) {
            return XMLMessage.create(ds, this.binding.getFeatures());
        }

        @Override // com.sun.xml.internal.ws.server.provider.ProviderArgumentsBuilder
        protected Message getResponseMessage(Exception e2) {
            return XMLMessage.create(e2);
        }
    }
}
