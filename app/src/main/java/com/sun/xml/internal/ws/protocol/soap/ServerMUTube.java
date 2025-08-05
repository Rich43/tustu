package com.sun.xml.internal.ws.protocol.soap;

import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.NextAction;
import com.sun.xml.internal.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import com.sun.xml.internal.ws.client.HandlerConfiguration;
import java.util.Set;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/protocol/soap/ServerMUTube.class */
public class ServerMUTube extends MUTube {
    private ServerTubeAssemblerContext tubeContext;
    private final Set<String> roles;
    private final Set<QName> handlerKnownHeaders;

    public ServerMUTube(ServerTubeAssemblerContext tubeContext, Tube next) {
        super(tubeContext.getEndpoint().getBinding(), next);
        this.tubeContext = tubeContext;
        HandlerConfiguration handlerConfig = this.binding.getHandlerConfig();
        this.roles = handlerConfig.getRoles();
        this.handlerKnownHeaders = this.binding.getKnownHeaders();
    }

    protected ServerMUTube(ServerMUTube that, TubeCloner cloner) {
        super(that, cloner);
        this.tubeContext = that.tubeContext;
        this.roles = that.roles;
        this.handlerKnownHeaders = that.handlerKnownHeaders;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public NextAction processRequest(Packet request) {
        Set<QName> misUnderstoodHeaders = getMisUnderstoodHeaders(request.getMessage().getHeaders(), this.roles, this.handlerKnownHeaders);
        if (misUnderstoodHeaders == null || misUnderstoodHeaders.isEmpty()) {
            return doInvoke(this.next, request);
        }
        return doReturnWith(request.createServerResponse(createMUSOAPFaultMessage(misUnderstoodHeaders), this.tubeContext.getWsdlModel(), this.tubeContext.getSEIModel(), this.tubeContext.getEndpoint().getBinding()));
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public ServerMUTube copy(TubeCloner cloner) {
        return new ServerMUTube(this, cloner);
    }
}
