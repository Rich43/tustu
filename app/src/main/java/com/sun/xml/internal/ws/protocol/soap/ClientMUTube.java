package com.sun.xml.internal.ws.protocol.soap;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.NextAction;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import com.sun.xml.internal.ws.client.HandlerConfiguration;
import java.util.Set;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/protocol/soap/ClientMUTube.class */
public class ClientMUTube extends MUTube {
    public ClientMUTube(WSBinding binding, Tube next) {
        super(binding, next);
    }

    protected ClientMUTube(ClientMUTube that, TubeCloner cloner) {
        super(that, cloner);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    @NotNull
    public NextAction processResponse(Packet response) {
        if (response.getMessage() == null) {
            return super.processResponse(response);
        }
        HandlerConfiguration handlerConfig = response.handlerConfig;
        if (handlerConfig == null) {
            handlerConfig = this.binding.getHandlerConfig();
        }
        Set<QName> misUnderstoodHeaders = getMisUnderstoodHeaders(response.getMessage().getHeaders(), handlerConfig.getRoles(), this.binding.getKnownHeaders());
        if (misUnderstoodHeaders == null || misUnderstoodHeaders.isEmpty()) {
            return super.processResponse(response);
        }
        throw createMUSOAPFaultException(misUnderstoodHeaders);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public ClientMUTube copy(TubeCloner cloner) {
        return new ClientMUTube(this, cloner);
    }
}
